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
import org.lappsgrid.json2json.Template;
import org.lappsgrid.json2json.jsonobject.JsonProxy;

import java.util.Map;

/**
 * Created by shi on 6/15/14.
 */
public class ProcedureUnit extends TemplateUnit{


    protected ProcedureUnit(JsonUnit ref) {
        super(ref);
    }

    public ProcedureUnit(Object obj) {
        super(obj);
    }

    /**
     * Initialization Unit Process all the initialization into Map.
     */
    public static class InitUnit {
        public static  Map<String, Object> init (JsonProxy.JsonObject jobj, Map<String, Object> map) throws Json2JsonException {
            for(String key : jobj.keys()) {
                Object val = jobj.get(key);
                /** In case of Template Unit, we will do Template.transform **/
                val = new JsonUnit(val).transform();
                String vname = getVarName(key);
                map.put(vname, val);
            }
            return map;
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

    public static class Step {

    }




    public static class ProcedureTransform implements Transform {


        public Object transform (String command, Object obj, Map<String, Object> map) throws Json2JsonException {
            return null;
        }

        @Override
        public Object transform(TemplateUnit obj) throws Json2JsonException {


            return null;
        }
    }


    @Override
    public Object transform() {
        return null;
    }
}
