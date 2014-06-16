/**********************************************************************************************************************
 Copyright [2014] [Chunqi SHI (chunqi.shi@hotmail.com)]

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 **********************************************************************************************************************/
package org.lappsgrid.json2json.template;

import org.lappsgrid.json2json.Json2JsonException;
import org.lappsgrid.json2json.engine.TemplateEngine;
import org.lappsgrid.json2json.jsonobject.JsonProxy.JsonObject;

import java.util.Collection;

/**
 * <p> TemplateUnit is the class to recognize Commands and Process </p>
 */
public class TemplateUnit extends JsonUnit{
    TemplateEngine.EvalEngine engine = null;

    protected TemplateUnit(JsonUnit ref) {
        super(ref);
        engine = TemplateEngine.newEngine();
    }

    public TemplateUnit(Object obj) {
        super(obj);
        engine = TemplateEngine.newEngine();
    }

    /**  generate from parent.  **/
    public TemplateUnit(JsonUnit parent, Object obj){
        super(parent, obj);
        engine = TemplateEngine.newEngine();
    }

    public static enum TemplateType {
        Command, Procedure, Expression
    }

    public static interface Transform {
        Object transform (TemplateUnit obj) throws Json2JsonException;
    }

    public boolean isTemplate() {
        if (obj != null && obj instanceof JsonObject) {
            Collection<String> keys = ((JsonObject)obj).keys();

            /** Only 1 key is allowed in the TemplateUnit, and the key must be a symbol or keyword 　**/
            if (keys.size() == 1) {
                String type = unitName();
//                logger.info(type + " " +TemplateNaming.hasSymbol(type)+ " " + TemplateNaming.hasKeyword(type));
                // if find it in the symbols or keywords
                if (TemplateNaming.hasSymbol(type) ||
                        TemplateNaming.hasKeyword(type)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Object transform () throws Json2JsonException {
        if(isTemplate()) {
            if ( getTemplateType() == TemplateType.Command) {
                CommandUnit cu = new CommandUnit(this);
                transformed = cu.transform();
            } else if (getTemplateType()  == TemplateType.Procedure) {
                ProcedureUnit pu = new ProcedureUnit(this);
                transformed = pu.transform();
            } else if (getTemplateType()  == TemplateType.Expression){
                ExpressionUnit eu = new ExpressionUnit(this);
                transformed = eu.transform();
            }
        }
        return transformed;
    }

    public TemplateType getTemplateType() {
        return TemplateNaming.templateTypeByCommand(unitName());
    }

    public String unitName(){
        return ((JsonObject)obj).keys().iterator().next().trim();
    }

    public Object unitContent() {
        return ((JsonObject)obj).get(unitName());
    }

}
