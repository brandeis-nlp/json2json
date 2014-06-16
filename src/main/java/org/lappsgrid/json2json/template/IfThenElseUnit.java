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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shi on 6/15/14.
 */
public class IfThenElseUnit extends ProcedureUnit {
    Logger logger = LoggerFactory.getLogger(IfThenElseUnit.class);
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
        return TemplateNaming.unitTypeByCommand(this.unitName()).equals(TemplateNaming.UnitType.if_then_else_process);
    }

    @Override
    public Object transform() throws Json2JsonException{
        transformed = null;
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
                for(String key: jobj.keys()) {
                    TemplateNaming.UnitType unitType = TemplateNaming.unitTypeByCommand(this.unitName(),key);
                    if(unitType.equals(TemplateNaming.UnitType.definitions_of_if_then_else_process)) {
                        initObj = jobj.get(key);
                    } else if(unitType.equals(TemplateNaming.UnitType.expression_of_if_then_else_process)) {
                        exprObj = jobj.get(key);
                    } else if(unitType.equals(TemplateNaming.UnitType.then_step_of_if_then_else_process)) {
                        thenObj = jobj.get(key);
                    } else if(unitType.equals(TemplateNaming.UnitType.else_step_of_if_then_else_process)) {
                        elseObj = jobj.get(key);
                    } else if(unitType.equals(TemplateNaming.UnitType.return_of_if_then_else_process)) {
                        retObj = jobj.get(key);
                    }
                }
            } else if(this.unitContent() instanceof JsonProxy.JsonArray) {
                JsonProxy.JsonArray jobj = (JsonProxy.JsonArray)this.unitContent();
                if(jobj.length() != 3) {
                    throw new Json2JsonException("Illegal procedure Json : " + jobj);
                }
                initObj = jobj.get(0);

                JsonProxy.JsonObject jsubobj = (JsonProxy.JsonObject)jobj.get(1);
                for(String key: jsubobj.keys()) {
                    TemplateNaming.UnitType unitType = TemplateNaming.unitTypeByCommand(this.unitName(),key);
                    if(unitType.equals(TemplateNaming.UnitType.expression_of_if_then_else_process)) {
                        exprObj = jsubobj.get(key);
                    } else if(unitType.equals(TemplateNaming.UnitType.then_step_of_if_then_else_process)) {
                        thenObj = jsubobj.get(key);
                    } else if(unitType.equals(TemplateNaming.UnitType.else_step_of_if_then_else_process)) {
                        elseObj = jsubobj.get(key);
                    }
                }
                retObj = jobj.get(2);
            }

            if(exprObj == null) {
                throw new Json2JsonException("Unit \"Expression (%<>)\" missing. ");
            }
            if(thenObj == null) {
                throw new Json2JsonException("Unit \"Then (%THEN)\" missing. ");
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
            if(procUnit != null) {
                procUnit.transform();
                this.transformed = procUnit.map.get(getVarName((String)retObj));
            }
        }
        return this.transformed;
    }
}
