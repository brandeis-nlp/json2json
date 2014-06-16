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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by shi on 6/15/14.
 */
public class ProcedureUnit extends TemplateUnit {
    Logger logger = LoggerFactory.getLogger(ProcedureUnit.class);
    /**  generate from parent.  **/
    public ProcedureUnit(JsonUnit parent, Object obj){
        super(parent, obj);
    }

    protected ProcedureUnit(JsonUnit ref) {
        super(ref);
    }

    public ProcedureUnit(Object obj) {
        super(obj);
    }

    public static class StepsUnit extends JsonUnit {
        StepUnit [] steps = null;
        boolean isSteps = false;
        public StepsUnit(Object obj) {
            super(obj);
            init();
        }
        public StepsUnit(JsonUnit parent, Object obj) {
            super(parent, obj);
            init();
        }

        public boolean isStepsUnit() {
            return isSteps;
        }

        protected void init() {
            if(obj != null && obj instanceof JsonProxy.JsonArray) {
                JsonProxy.JsonArray jobj = (JsonProxy.JsonArray) obj;
                isSteps = true;
                for(int i = 0; i < jobj.length(); i ++) {
                    isSteps = isSteps && new StepUnit(jobj.get(i)).isStepUnit();
                }
            }
        }

        @Override
        public Object transform () throws Json2JsonException {
            if(isStepsUnit()){
                JsonProxy.JsonArray jobj = (JsonProxy.JsonArray) obj;
                steps = new StepUnit[jobj.length()];
                for(int i = 0; i < jobj.length(); i ++) {
                    steps[i] = new StepUnit(this, jobj.get(i));
                    steps[i].transform();
                    this.map.put(steps[i].varName, steps[i].map.get(steps[i].varName));
                }
            }
            return null;
        }
    }

    public static boolean isStepUnit(Object obj) {
        return new StepUnit(obj).isStepUnit();
    }

    public static boolean isStepsUnit(Object obj) {
        return new StepsUnit(obj).isStepsUnit();
    }

    public static class StepUnit extends JsonUnit {
        String varName = null;
        boolean isStep = false;

        public StepUnit(Object obj) {
            super(obj);
            init();
        }

        public StepUnit(JsonUnit parent, Object obj) {
            super(parent, obj);
            init();
        }

        public String getVarName() {
            return varName;
        }

        @Override
        public Object transform () throws Json2JsonException {
            if(isStepUnit()) {
                JsonProxy.JsonObject jobj = (JsonProxy.JsonObject) obj;
                String key = jobj.keys().iterator().next().trim();
                this.map.put(varName, new JsonUnit(this, jobj.get(key)).transform());
            }
            return null;
        }

        protected void init() {
            if(obj != null && obj instanceof JsonProxy.JsonObject) {
                JsonProxy.JsonObject jobj = (JsonProxy.JsonObject) obj;
                if(jobj.length() == 1) {
                    String key = jobj.keys().iterator().next().trim();
                    if(key.startsWith(TemplateNaming.VariableMark)) {
                        varName = key.substring(TemplateNaming.VariableMark.length());
                        isStep = true;
                    }
                }
            }
        }

        public boolean isStepUnit() {
            return isStep;
        }
    }

    /**
     * Initialization Unit Process all the initialization into Map.
     */
    public static class InitUnit extends JsonUnit {
        public InitUnit(JsonUnit parent, Object obj) {
            super(parent, obj);
        }

        public boolean isInitUnit(){
            if(obj != null && obj instanceof JsonProxy.JsonObject) {
                return true;
            }
            return false;
        }

        public Object transform () throws Json2JsonException {
            if(isInitUnit()){
                JsonProxy.JsonObject jobj = (JsonProxy.JsonObject)obj;
                for(String key : jobj.keys()) {
                    Object val = jobj.get(key);
                    /** In case of Template Unit, we will do Template.transform **/
                    val = new JsonUnit(this, val).transform();
                    String vname = getVarName(key);
                    map.put(vname, val);
                }
            }
            return null;
        }

        public static boolean isVarDef(String vname) {
            if(vname != null && vname.trim().startsWith(TemplateNaming.ProcedureInitMark)) {
                return true;
            }
            return false;
        }

        public static String getVarName(String vname) {
            if(isVarDef(vname))
                return vname.trim().substring(TemplateNaming.ProcedureInitMark.length());
            return null;
        }

    }

    protected static String getVarName(String vname){
        return vname.trim().substring(TemplateNaming.VariableMark.length());
    }

//    protected Object getObject(JsonProxy.JsonObject procObj, TemplateNaming.UnitType type) {
//        Object obj = procObj.get(TemplateNaming.symbolByName(type.name()));
//        if(obj == null) {
//            obj = procObj.get(TemplateNaming.keywordByName(type.name()));
//        }
//        return obj;
//    }

    @Override
    public Object transform() throws Json2JsonException{
        if(isIfThenElse(obj)) {
            transformed = new IfThenElseUnit(this).transform();
        } else if(isForEach(obj)) {
            transformed = new ForEachUnit(this).transform();
        } else if(isDoWhile(obj)) {
            transformed = new DoWhileUnit(this).transform();
        }
        return transformed;
    }

    public static boolean isIfThenElse(Object obj) {
        return new IfThenElseUnit(obj).isIfThenElse();
    }

    public static boolean isForEach(Object obj) {
        return new ForEachUnit(obj).isForEach();
    }
    public static boolean isDoWhile(Object obj) {
        return new DoWhileUnit(obj).isDoWhile();
    }
}
