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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shi on 6/15/14.
 */
public class JsonUnit {
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
        this.map.putAll(parent.map);
    }

    public Object transform () throws Json2JsonException {
        if (isJsonPathRef(obj)) {
            /** JsonPath Reference **/
            JsonPathRefUnit unit = new JsonPathRefUnit(this);
            transformed = unit.transform();

        } else if(isTemplate(obj)) {
            /** Template **/
            TemplateUnit unit = new TemplateUnit(this);
            transformed = unit.transform();
        }
        return transformed;
    }

    public JsonUnit childUnit(Object obj) {
        return new JsonUnit(this, obj);
    }

    public static Object str2json(String json) {
        if (json == null)
            return null;
        if (json.startsWith("{")) {
            return JsonProxy.readObject(json);
        } else if (json.startsWith("[")) {
            return JsonProxy.readArray(json);
        } else {
            return json;
        }
    }

    public static boolean isJsonPathRef(Object obj) {
        return new JsonPathRefUnit(obj).isJsonPathRef();
    }

    public static boolean isTemplate(Object obj) {
        return new TemplateUnit(obj).isTemplate();
    }
}
