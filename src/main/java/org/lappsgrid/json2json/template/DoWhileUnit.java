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
import org.lappsgrid.json2json.jsonobject.JsonProxy;

/**
 * Created by shi on 6/15/14.
 */
public class DoWhileUnit extends ProcedureUnit {
    public DoWhileUnit(JsonUnit parent, Object obj){
        super(parent, obj);
    }

    protected DoWhileUnit(JsonUnit ref) {
        super(ref);
    }

    public DoWhileUnit(Object obj) {
        super(obj);
    }

    public boolean isDoWhile(){
        return TemplateNaming.unitTypeByCommand(this.unitName()).equals(TemplateNaming.UnitType.do_while_process);
    }



    @Override
    public Object transform() throws Json2JsonException {
        if (isDoWhile()) {
            Object initObj = null;
            Object exprobj = null;
            Object doobj = null;
            Object retObj = null;
            InitUnit initUnit = null;
            ExpressionUnit exprUnit = null;
            JsonUnit procUnit = null;

            if(this.unitContent() instanceof JsonProxy.JsonObject) {
                JsonProxy.JsonObject jobj = (JsonProxy.JsonObject)this.unitContent();
                for(String key: jobj.keys()) {
                    TemplateNaming.UnitType unitType = TemplateNaming.unitTypeByCommand(this.unitName(),key);
                    if(unitType.equals(TemplateNaming.UnitType.definitions_of_do_while_process)) {
                        initObj = jobj.get(key);
                    } else if(unitType.equals(TemplateNaming.UnitType.expression_of_do_while_process)) {
                        exprobj = jobj.get(key);
                    } else if(unitType.equals(TemplateNaming.UnitType.do_step_of_do_while_process)) {
                        doobj = jobj.get(key);
                    } else if(unitType.equals(TemplateNaming.UnitType.return_of_do_while_process)) {
                        retObj = jobj.get(key);
                    }
                }
            } else if(this.unitContent() instanceof JsonProxy.JsonArray) {
                JsonProxy.JsonArray jobj = (JsonProxy.JsonArray)this.unitContent();
                initObj = jobj.get(0);
                JsonProxy.JsonObject jsubobj = (JsonProxy.JsonObject)jobj.get(1);
                for(String key: jsubobj.keys()) {
                    TemplateNaming.UnitType unitType = TemplateNaming.unitTypeByCommand(this.unitName(),key);
                    if(unitType.equals(TemplateNaming.UnitType.expression_of_do_while_process)) {
                        exprobj = jsubobj.get(key);
                    } else if(unitType.equals(TemplateNaming.UnitType.do_step_of_do_while_process)) {
                        doobj = jsubobj.get(key);
                    }
                }
                retObj = jobj.get(2);
            }

            if(exprobj == null) {
                throw new Json2JsonException("Iterator unit missing. ");
            }
            if(doobj == null) {
                throw new Json2JsonException("Each unit missing. ");
            }

            initUnit = new InitUnit(this, initObj);
            initUnit.transform();
            exprUnit = new ExpressionUnit(initUnit,exprobj);
            procUnit = initUnit;
            while (exprUnit.transform().equals(true)) {
                if (isStepUnit(obj)) {
                    procUnit = new StepUnit(procUnit, doobj);
                } else if(isStepsUnit(obj)) {
                    procUnit = new StepsUnit(procUnit, doobj);
                }
                procUnit.transform();
                exprUnit = new ExpressionUnit(procUnit,exprobj);
            }
            this.transformed = procUnit.map.get(getVarName((String)retObj));
        }
        return this.transformed;
    }
}
