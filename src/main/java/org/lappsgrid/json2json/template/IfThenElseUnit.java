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

import org.lappsgrid.json2json.Json2Json;
import org.lappsgrid.json2json.Json2JsonException;
import org.lappsgrid.json2json.jsonobject.JsonProxy;

/**
 * Created by shi on 6/15/14.
 */
public class IfThenElseUnit extends ProcedureUnit {

    /**  generate from parent.  **/
    public IfThenElseUnit(JsonUnit parent, Object obj){
        super(parent, obj);
    }

    protected IfThenElseUnit(JsonUnit ref) {
        super(ref);
    }

    public IfThenElseUnit(Object obj) {
        super(obj);
    }

    public boolean isIfThenElse(){
       return TemplateNaming.templateTypeByCommand(this.unitName()).equals(TemplateNaming.UnitType.if_then_else_process);
    }

    @Override
    public Object transform() throws Json2JsonException{
        if (isIfThenElse()) {
            Object initObj = null;
            Object exprObj = null;
            Object thenObj = null;
            Object elseObj = null;
            Object retObj = null;
            InitUnit initUnit = null;
            JsonUnit procUnit = null;

            if(this.unitContent() instanceof JsonProxy.JsonObject) {
                JsonProxy.JsonObject jobj = (JsonProxy.JsonObject)this.unitContent();
                initObj = getObject(jobj, TemplateNaming.UnitType.definitions_of_if_then_else_process);
                exprObj = getObject(jobj, TemplateNaming.UnitType.expression_of_if_then_else_process);
                thenObj = getObject(jobj, TemplateNaming.UnitType.then_step_of_if_then_else_process);
                elseObj = getObject(jobj, TemplateNaming.UnitType.else_step_of_if_then_else_process);
                retObj =  getObject(jobj, TemplateNaming.UnitType.return_of_if_then_else_process);
            } else if(this.unitContent() instanceof JsonProxy.JsonArray) {
                JsonProxy.JsonArray jobj = (JsonProxy.JsonArray)this.unitContent();
                initObj = jobj.get(0);
                exprObj = getObject((JsonProxy.JsonObject)jobj.get(1), TemplateNaming.UnitType.expression_of_if_then_else_process);
                thenObj = getObject((JsonProxy.JsonObject)jobj.get(1), TemplateNaming.UnitType.then_step_of_if_then_else_process);
                elseObj = getObject((JsonProxy.JsonObject)jobj.get(1), TemplateNaming.UnitType.else_step_of_if_then_else_process);
                retObj = jobj.get(2);
            }

            if(exprObj == null) {
                throw new Json2JsonException("Expression unit missing. ");
            }
            if(thenObj == null) {
                throw new Json2JsonException("Then unit missing. ");
            }
            initUnit = new InitUnit(this, initObj);
            initUnit.transform();
            if (new ExpressionUnit(initUnit,exprObj).transform().equals(true)) {
                if (isStepUnit(obj)) {
                    procUnit = new StepUnit(initUnit, thenObj);
                } else if(isStepsUnit(obj)) {
                    procUnit = new StepsUnit(initUnit, thenObj);
                }
            } else if(elseObj != null){
                if (isStepUnit(obj)) {
                    procUnit = new StepUnit(initUnit, elseObj);
                } else if(isStepsUnit(obj)) {
                    procUnit = new StepsUnit(initUnit, elseObj);
                }
            }
            procUnit.transform();
            this.transformed = procUnit.map.get(getVarName((String)retObj));
        }
        return this.transformed;
    }
}
