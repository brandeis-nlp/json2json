package org.lappsgrid.json2json.template;

import org.lappsgrid.json2json.Json2JsonException;
import org.lappsgrid.json2json.jsonobject.JsonProxy;

/**
 * Created by shi on 6/15/14.
 */
public class ExpressionUnit extends TemplateUnit {
    public ExpressionUnit(JsonUnit parent, Object obj){
        super(parent, obj);
    }

    protected ExpressionUnit(JsonUnit ref) {
        super(ref);
    }

    public ExpressionUnit(Object obj) {
        super(obj);
    }

    public boolean isExpression () {
        if(getTemplateType()  == TemplateType.Expression){
            if(unitContent() instanceof JsonProxy.JsonArray ) {
                if(((JsonProxy.JsonArray) unitContent()).length() == 2)
                    return true;
            } else if(unitContent() instanceof JsonProxy.JsonObject) {
                if(((JsonProxy.JsonObject) unitContent()).length() == 1)
                    return true;
            }
        }
        return false;
    }

    @Override
    public Object transform () throws Json2JsonException {
        transformed = null;
        if(isExpression ()) {
            Object[] params = null;
            if(unitContent() instanceof JsonProxy.JsonObject) {
                Object ret = new JsonUnit(this, unitContent()).transform();
                if (ret instanceof JsonProxy.JsonArray) {
                    params = new Object[]{((JsonProxy.JsonArray) ret).get(0), ((JsonProxy.JsonArray) ret).get(1)};
                }
            } else if(unitContent() instanceof JsonProxy.JsonArray) {
                params = new Object[]{new JsonUnit(this, ((JsonProxy.JsonArray) unitContent()).get(0)).transform(),
                        new JsonUnit(this, ((JsonProxy.JsonArray) unitContent()).get(1)).transform()};
            }
            long id = System.currentTimeMillis();
            StringBuilder  sb = new StringBuilder();
            if (1 == params.length) {
                engine.bind("__a__" + id, params[0]);
                sb.append("(").append(unitName()).append(" (");
                sb.append("__a__" + id).append("))");
            } else if (2 == params.length) {
                engine.bind("__a__" + id, params[0]);
                engine.bind("__b__" + id, params[1]);
                sb.append("( (").append("__a__" + id);
                sb.append(") ").append(unitName()).append(" (");
                sb.append("__b__" + id).append(") )");
            }
            transformed = engine.eval("return " + sb.toString());
        }
        return transformed;
    }
}
