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

import com.fasterxml.jackson.databind.JsonSerializable;
import org.lappsgrid.json2json.Json2JsonException;
import org.lappsgrid.json2json.jsonobject.JsonProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shi on 6/15/14.
 */
public class ForEachUnit extends ProcedureUnit {
    Logger logger = LoggerFactory.getLogger(ProcedureUnit.class);

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
        return TemplateNaming.unitTypeByCommand(this.unitName()).equals(TemplateNaming.UnitType.for_each_process);
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
        transformed = null;
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
                for(String key: jobj.keys()) {
                    TemplateNaming.UnitType unitType = TemplateNaming.unitTypeByCommand(this.unitName(),key);
                    if(unitType.equals(TemplateNaming.UnitType.definitions_of_for_each_process)) {
                        initObj = jobj.get(key);
                    } else if(unitType.equals(TemplateNaming.UnitType.iterator_of_for_each_process)) {
                        iterObj = jobj.get(key);
                    } else if(unitType.equals(TemplateNaming.UnitType.each_step_of_for_each_process)) {
                        eachObj = jobj.get(key);
                    } else if(unitType.equals(TemplateNaming.UnitType.return_of_for_each_process)) {
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
                    if(unitType.equals(TemplateNaming.UnitType.iterator_of_for_each_process)) {
                        iterObj = jsubobj.get(key);
                    } else if(unitType.equals(TemplateNaming.UnitType.each_step_of_for_each_process)) {
                        eachObj = jsubobj.get(key);
                    }
                }
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
                procUnit.map.put(iterUnit.varNameIndex, i);
                procUnit.map.put(iterUnit.varNameEach, iterator.get(i));
                if (isStepUnit(obj)) {
                    procUnit = new StepUnit(procUnit, eachObj);
                } else if(isStepsUnit(obj)) {
                    procUnit = new StepsUnit(procUnit, eachObj);
                } else {
                    throw new Json2JsonException("Illegal \"Each (%EACH)\" process : " + obj);
                }
                procUnit.transform();
            }
            this.transformed = procUnit.map.get(getVarName((String)retObj));
        }
        return this.transformed;
    }
}
