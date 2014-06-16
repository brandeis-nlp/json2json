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
import org.lappsgrid.json2json.Template;
import org.lappsgrid.json2json.jsonobject.JsonProxy;

/**
 * Created by shi on 6/15/14.
 */
public class ForEachUnit extends ProcedureUnit {
    /**  generate from parent.  **/
    public ForEachUnit(JsonUnit parent, Object obj){
        super(parent, obj);
    }

    protected ForEachUnit(JsonUnit ref) {
        super(ref);
    }

    public ForEachUnit(Object obj) {
        super(obj);
    }

    public boolean isForEach(){
        return TemplateNaming.templateTypeByCommand(this.unitName()).equals(TemplateNaming.UnitType.for_each_process);
    }


    public static class IteratorUnit extends JsonUnit {
        boolean isIterator = false;
        String varNameIndex = null;
        String varNameEach = null;
        public IteratorUnit(JsonUnit parent, Object obj){
            super(parent, obj);
        }

        protected IteratorUnit(JsonUnit ref) {
            super(ref);
        }

        public IteratorUnit(Object obj) {
            super(obj);
        }

        public Object transform () throws Json2JsonException {
            if(obj != null && obj instanceof JsonProxy.JsonArray) {
                JsonProxy.JsonArray jarr = (JsonProxy.JsonArray)obj;

                transformed = new JsonUnit(this, jarr.get(0)).transform();
                if(transformed instanceof JsonProxy.JsonArray)
                {
                    isIterator = true;
                } else {
                    throw new Json2JsonException("Unknown iterator format. ");
                }

                if (jarr.length() == 1 ) {
                    varNameIndex = TemplateNaming.DefaultForEachIndex;
                    varNameEach = TemplateNaming.DefaultForEachIterator;
                } else if (jarr.length() == 3) {
                    varNameIndex = getVarName((String)jarr.get(1));
                    varNameEach = getVarName((String)jarr.get(2));
                } else {
                    throw new Json2JsonException("Wrong iterator format. ");
                }
            }
            return transformed;
        }

    }

    @Override
    public Object transform() throws Json2JsonException {
        if (isForEach()) {
            Object initObj = null;
            Object iterObj = null;
            Object eachObj = null;
            Object retObj = null;
            InitUnit initUnit = null;
            IteratorUnit iterUnit = null;
            JsonUnit procUnit = null;

            if(this.unitContent() instanceof JsonProxy.JsonObject) {
                JsonProxy.JsonObject jobj = (JsonProxy.JsonObject)this.unitContent();
                initObj = getObject(jobj, TemplateNaming.UnitType.definitions_of_for_each_process);
                iterObj = getObject(jobj, TemplateNaming.UnitType.iterator_of_for_each_process);
                eachObj = getObject(jobj, TemplateNaming.UnitType.each_step_of_for_each_process);
                retObj =  getObject(jobj, TemplateNaming.UnitType.return_of_if_then_else_process);
            } else if(this.unitContent() instanceof JsonProxy.JsonArray) {
                JsonProxy.JsonArray jobj = (JsonProxy.JsonArray)this.unitContent();
                initObj = jobj.get(0);
                iterObj = getObject((JsonProxy.JsonObject)jobj.get(1), TemplateNaming.UnitType.iterator_of_for_each_process);
                eachObj = getObject((JsonProxy.JsonObject)jobj.get(1), TemplateNaming.UnitType.each_step_of_for_each_process);
                retObj = jobj.get(2);
            }

            if(iterObj == null) {
                throw new Json2JsonException("Iterator unit missing. ");
            }
            if(eachObj == null) {
                throw new Json2JsonException("Each unit missing. ");
            }
            initUnit = new InitUnit(this, initObj);
            initUnit.transform();
            iterUnit = new IteratorUnit(initUnit,iterObj);
            JsonProxy.JsonArray iterator = (JsonProxy.JsonArray) iterUnit.transform();
            procUnit = initUnit;
            for (int i = 0; i < iterator.length(); i++) {
                if (isStepUnit(obj)) {
                    procUnit = new StepUnit(procUnit, eachObj);
                } else if(isStepsUnit(obj)) {
                    procUnit = new StepsUnit(procUnit, eachObj);
                }
                procUnit.transform();
            }
            this.transformed = procUnit.map.get(getVarName((String)retObj));
        }
        return this.transformed;
    }
}
