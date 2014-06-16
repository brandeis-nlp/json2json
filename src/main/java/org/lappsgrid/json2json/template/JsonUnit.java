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

import org.lappsgrid.json2json.jsonobject.JsonProxy.JsonObject;
import org.lappsgrid.json2json.Json2JsonException;
import org.lappsgrid.json2json.jsonobject.JsonProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shi on 6/15/14.
 */
public class JsonUnit {
    //    http://www.slf4j.org/manual.html
    Logger logger = LoggerFactory.getLogger(JsonUnit.class);

    List<String> jsons = null;
    Object obj = null;
    Object transformed = null;
    Map<String, Object> map = null;

    /** reference **/
    protected JsonUnit(JsonUnit ref) {
        this.obj = ref.obj;
        this.map = ref.map;
        this.jsons = ref.jsons;
        this.transformed = ref.transformed;
    }

    public JsonUnit(Object obj) {
        this.obj = obj;
        this.transformed = this.obj;
        map = new LinkedHashMap<String, Object>();
    }

    /**  generate from parent.  **/
    public JsonUnit(JsonUnit parent, Object obj){
        this(obj);
        this.jsons = parent.jsons;
        /** variable saving **/
        this.map.putAll(parent.map);
    }

    public Object transform () throws Json2JsonException{
        try {
            if (isJsonPathRef(obj)) {
                /** JsonPath Reference **/
                JsonPathRefUnit unit = new JsonPathRefUnit(this);
                transformed = unit.transform();

            } else if (isTemplate(obj)) {
                /** Template **/
                TemplateUnit unit = new TemplateUnit(this);
                transformed = unit.transform();
            } else if (isVariable(obj)) {
                /** Variable is loaded from map **/
                transformed = map.get(ProcedureUnit.getVarName((String) obj));
            } else if (obj instanceof JsonProxy.JsonObject) {
                JsonProxy.JsonObject trans = JsonProxy.newObject();
                for (String key : ((JsonProxy.JsonObject) obj).keys()) {
                    trans.put(key, new JsonUnit(this, ((JsonProxy.JsonObject) obj).get(key)).transform());
                }
                transformed = trans;
            } else if (obj instanceof JsonProxy.JsonArray) {
                JsonProxy.JsonArray trans = JsonProxy.newArray();
                for (int i = 0; i < ((JsonProxy.JsonArray) obj).length(); i++) {
                    trans.add(new JsonUnit(this, ((JsonProxy.JsonArray) obj).get(i)).transform());
                }
                transformed = trans;
            }

        }catch (Throwable th) {
            th.printStackTrace();
            transformed = exp2json(th);
        }
        return transformed;
    }

    public static JsonObject exp2json(Throwable th) {
        JsonObject obj = JsonProxy.newObject();
        StringWriter sw = new StringWriter();
        th.printStackTrace(new PrintWriter(sw));
        String exceptionAsString = sw.toString();
        obj.put("Exception", exceptionAsString);
        return obj;
    }

    public JsonUnit setJsons(String ... jsons) {
        this.jsons = new ArrayList<String>(jsons.length);
        for(String json:jsons)
            this.jsons.add(json);
        return this;
    }


    public JsonUnit setJsons(List<String> jsons) {
        this.jsons = new ArrayList<String>(jsons.size());
        for(String json:jsons)
            this.jsons.add(json);
        return this;
    }

    public JsonUnit childUnit(Object obj) {
        return new JsonUnit(this, obj);
    }



    public static boolean isJsonPathRef(Object obj) {
        return new JsonPathRefUnit(obj).isJsonPathRef();
    }

    public static boolean isTemplate(Object obj) {
        return new TemplateUnit(obj).isTemplate();
    }

    public static boolean isVariable(Object obj) {
        if(obj instanceof String) {
            if(((String) obj).trim().startsWith(TemplateNaming.VariableMark)){
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(JsonUnit.class);
        logger.info("Hello World");
    }
}
