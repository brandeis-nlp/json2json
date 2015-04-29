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
package org.lappsgrid.json2json.jsonobject;

import java.util.Collection;
import java.util.List;

/**
 * <p> Minimal Json provides very good implementation of Json functions</p>
 * <a href="https://github.com/ralfstx/minimal-json">Minimal Json</a>.
 *
 */
public class MinimalJsonProxy implements JsonProxy.NewProxy{
    
    public JsonProxy.JsonArray newArray() {
        return new MinimalJsonArray();
    }

    
    public JsonProxy.JsonObject newObject() {
        return new MinimalJsonObject();
    }

    public static class MinimalJsonObject implements JsonProxy.JsonObject {
        com.eclipsesource.json.JsonObject jsonObject = null;

        public JsonProxy.JsonObject read(String s) {
            jsonObject = com.eclipsesource.json.JsonObject.readFrom(s);
            return this;
        }

        public com.eclipsesource.json.JsonObject original() {
            return jsonObject;
        }

//        
//        public JsonObject clone() {
//            return new MinimalJsonObject(new com.eclipsesource.json.JsonObject(jsonObject));
//        }

        protected MinimalJsonObject() {
            jsonObject = new com.eclipsesource.json.JsonObject();
        }

        public MinimalJsonObject(com.eclipsesource.json.JsonObject obj){
            jsonObject = obj;
        }

        
        public Object get(String key) {
            com.eclipsesource.json.JsonValue obj = jsonObject.get(key);
            return value2object(obj);
        }

        public JsonProxy.JsonObject put(String key, Object obj) {
            if(obj != null) {
                jsonObject.add(key, valueOf(obj));
            }
            return this;
        }

        public JsonProxy.JsonObject remove(String key) {
            jsonObject.remove(key);
            return this;
        }

        
        public List<String> keys() {
            return jsonObject.names();
        }

        public int length(){
            return jsonObject.size();
        }


        public boolean has(String key) {
            return ! (jsonObject.get(key) == null);
        }

        public String toString(){
            return jsonObject.toString();
        }

    }

    public static com.eclipsesource.json.JsonValue valueOf(Object obj) {
        com.eclipsesource.json.JsonValue value = null;
        if(obj == null) {

        } else if (obj instanceof Integer) {
            value = com.eclipsesource.json.JsonValue.valueOf((Integer)obj);
        } else if (obj instanceof Boolean) {
            value = com.eclipsesource.json.JsonValue.valueOf((Boolean)obj);
        } else if (obj instanceof Long) {
            value = com.eclipsesource.json.JsonValue.valueOf((Long)obj);
        } else if (obj instanceof Double) {
            value = com.eclipsesource.json.JsonValue.valueOf((Double)obj);
        } else if (obj instanceof Float) {
            value = com.eclipsesource.json.JsonValue.valueOf((Float)obj);
        } else if (obj instanceof String) {
            value = com.eclipsesource.json.JsonValue.valueOf((String)obj);
        } else if (obj instanceof com.eclipsesource.json.JsonValue) {
            value = (com.eclipsesource.json.JsonValue)obj;
        } else if (obj instanceof JsonProxy.JsonArray) {
            value = (com.eclipsesource.json.JsonValue)((JsonProxy.JsonArray)obj).original();
        } else if (obj instanceof JsonProxy.JsonObject) {
            value = (com.eclipsesource.json.JsonValue)((JsonProxy.JsonObject)obj).original();
        } else {
            throw new RuntimeException("Unknown object type: " + obj.getClass());
        }
        return value;
    }

    public static Object value2object(com.eclipsesource.json.JsonValue obj ) {
        if (obj.isObject()) {
            return new MinimalJsonObject(obj.asObject());
        } else if (obj.isArray()) {
            return new MinimalJsonArray(obj.asArray());
        } else if (obj.isBoolean()) {
            return obj.asBoolean();
        } else if (obj.isNull()) {
            return null;
        } else if (obj.isFalse()) {
            return false;
        } else if (obj.isTrue()) {
            return true;
        } else if (obj.isNumber()) {
            if(obj.toString().contains(".")) {
                return obj.asFloat();
            } else {
                return obj.asInt();
            }
        } else if (obj.isString()) {
            return obj.asString();
        } else {
            throw new RuntimeException("Unknown object type: " + obj.getClass());
        }
    }

    public static class MinimalJsonArray implements JsonProxy.JsonArray {
        com.eclipsesource.json.JsonArray jsonArray = null;

        public com.eclipsesource.json.JsonArray original() {
            return jsonArray;
        }

        public String toString(){
            return jsonArray.toString();
        }

        public JsonProxy.JsonArray read(String s) {
            jsonArray = com.eclipsesource.json.JsonArray.readFrom(s);
            return this;
        }

        protected MinimalJsonArray() {
            jsonArray = new com.eclipsesource.json.JsonArray();
        }
        public MinimalJsonArray(com.eclipsesource.json.JsonArray arr){
            jsonArray = arr;
        }

        
        public Object get(int i) {
            com.eclipsesource.json.JsonValue obj = jsonArray.get(i);
            return value2object(obj);
        }

        
        public JsonProxy.JsonArray add(Object obj) {
            if(obj != null) {
                jsonArray.add(valueOf(obj));
            }
            return this;
        }

        
        public JsonProxy.JsonArray remove(int i) {
            jsonArray.remove(i);
            return this;
        }

        
        public JsonProxy.JsonArray set(int i, Object s) {
            jsonArray.set(i, valueOf(s));
            return this;
        }

        
        public int length() {
            return jsonArray.size();
        }

        public JsonProxy.JsonArray convert(String[] arr) {
            jsonArray = new com.eclipsesource.json.JsonArray();
            for(String s: arr) {
                jsonArray.add(s);
            }
            return this;
        }
        public JsonProxy.JsonArray convert(Collection<String> arr) {
            jsonArray = new com.eclipsesource.json.JsonArray();
            for(String s: arr) {
                jsonArray.add(s);
            }
            return this;
        }

//        
//        public JsonArray clone() {
//            return new MinimalJsonArray(new com.eclipsesource.json.JsonArray(jsonArray));
//        }

        public Object add(String s) {
            jsonArray.add(s);
            return this;
        }
    }
}
