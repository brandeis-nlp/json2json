package org.lappsgrid.json2json;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Created by lapps on 6/13/2014.
 */
public class JsonProxy {

    public static interface JsonObject {
        public String toString();
        public Object get(String key);
        public JsonObject remove(String key);
        public List<String> keys();
        public int length();
        public boolean has(String key);
        public JsonObject put(String key, Object val);
        public Object original();
    }

    public static interface JsonArray {
        public Object get(int i);
        public JsonArray add(Object s);
        public JsonArray remove(int i);
        public int length();
        public String toString();
        public JsonArray read(String s);
        public JsonArray convert(String [] arr);
        public Object original();
    }

    public static class MinimalJsonObject implements JsonObject{
        com.eclipsesource.json.JsonObject jsonObject = null;

        public JsonObject read(String s) {
            jsonObject = com.eclipsesource.json.JsonObject.readFrom(s);
            return this;
        }

        public com.eclipsesource.json.JsonObject original() {
            return jsonObject;
        }

        protected MinimalJsonObject() {
            jsonObject = new com.eclipsesource.json.JsonObject();
        }

        public MinimalJsonObject(com.eclipsesource.json.JsonObject obj){
            jsonObject = obj;
        }

        @Override
        public Object get(String key) {
            com.eclipsesource.json.JsonValue obj = jsonObject.get(key);
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
                return obj.asInt();
            } else if (obj.isString()) {
                return obj.asString();
            } else {
                throw new RuntimeException("Unknown object type: " + obj.getClass());
            }
        }

        public JsonObject put(String key, Object obj) {
            if(obj == null) {
            } else if (obj instanceof Integer) {
                jsonObject.add(key,(Integer)obj);
            } else if (obj instanceof Boolean) {
                jsonObject.add(key,(Boolean)obj);
            } else if (obj instanceof Long) {
                jsonObject.add(key,(Long)obj);
            } else if (obj instanceof Double) {
                jsonObject.add(key,(Double)obj);
            } else if (obj instanceof Float) {
                jsonObject.add(key,(Float)obj);
            } else if (obj instanceof String) {
                jsonObject.add(key,(String)obj);
            } else if (obj instanceof com.eclipsesource.json.JsonValue) {
                jsonObject.add(key,(com.eclipsesource.json.JsonValue)obj);
            } else if (obj instanceof JsonArray) {
                jsonObject.add(key,(com.eclipsesource.json.JsonValue)((JsonArray)obj).original());
            } else if (obj instanceof JsonObject) {
                jsonObject.add(key,(com.eclipsesource.json.JsonObject)((JsonObject)obj).original());
            } else {
                throw new RuntimeException("Unknown object type: " + obj.getClass());
            }
            return this;
        }

        public JsonObject remove(String key) {
            jsonObject.remove(key);
            return this;
        }

        @Override
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

    public static class MinimalJsonArray implements JsonArray{
        com.eclipsesource.json.JsonArray jsonArray = null;

        public com.eclipsesource.json.JsonArray original() {
            return jsonArray;
        }

        public String toString(){
            return jsonArray.toString();
        }

        public JsonArray read(String s) {
            jsonArray = com.eclipsesource.json.JsonArray.readFrom(s);
            return this;
        }

        protected MinimalJsonArray() {
            jsonArray = new com.eclipsesource.json.JsonArray();
        }
        public MinimalJsonArray(com.eclipsesource.json.JsonArray arr){
            jsonArray = arr;
        }

        @Override
        public Object get(int i) {
            com.eclipsesource.json.JsonValue obj = jsonArray.get(i);
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
                return obj.asInt();
            } else if (obj.isString()) {
                return obj.asString();
            } else {
                throw new RuntimeException("Unknown object type: " + obj.getClass());
            }
        }

        @Override
        public JsonArray add(Object obj) {
            if(obj == null) {
            } else if (obj instanceof Integer) {
                jsonArray.add((Integer)obj);
            } else if (obj instanceof Boolean) {
                jsonArray.add((Boolean)obj);
            } else if (obj instanceof Long) {
                jsonArray.add((Long)obj);
            } else if (obj instanceof Double) {
                jsonArray.add((Double)obj);
            } else if (obj instanceof Float) {
                jsonArray.add((Float)obj);
            } else if (obj instanceof String) {
                jsonArray.add((String)obj);
            } else if (obj instanceof com.eclipsesource.json.JsonValue) {
                jsonArray.add((com.eclipsesource.json.JsonValue)obj);
            } else if (obj instanceof JsonArray) {
                jsonArray.add((com.eclipsesource.json.JsonValue)((JsonArray)obj).original());
            } else if (obj instanceof JsonObject) {
                jsonArray.add((com.eclipsesource.json.JsonObject)((JsonObject)obj).original());
            } else {
                throw new RuntimeException("Unknown object type: " + obj.getClass());
            }
            return this;
        }

        @Override
        public JsonArray remove(int i) {
            jsonArray.remove(i);
            return this;
        }

        @Override
        public int length() {
            return jsonArray.size();
        }

        public JsonArray convert(String[] arr) {
            jsonArray = new com.eclipsesource.json.JsonArray();
            for(String s: arr) {
                jsonArray.add(s);
            }
            return this;
        }
        public JsonArray convert(List<String> arr) {
            jsonArray = new com.eclipsesource.json.JsonArray();
            for(String s: arr) {
                jsonArray.add(s);
            }
            return this;
        }
        public JsonArray add(String s) {
            jsonArray.add(s);
            return this;
        }
    }


    public static JsonArray convertArray(String [] arr) {
        // FIXME: exchange proxy
        return new MinimalJsonArray().convert(arr);
    }
    public static JsonArray convertArray(List<String> arr) {
        // FIXME: exchange proxy
        return new MinimalJsonArray().convert(arr);
    }

    public static JsonArray readArray(String s) {
        // FIXME: exchange proxy
        return new MinimalJsonArray().read(s);
    }

    public static JsonObject readObject(String s) {
        // FIXME: exchange proxy
        return new MinimalJsonObject().read(s);
    }
    public static JsonArray newArray() {
        // FIXME: exchange proxy
        return new MinimalJsonArray();
    }

    public static JsonObject newObject() {
        // FIXME: exchange proxy
        return new MinimalJsonObject();
    }


    public static boolean isArray (Object obj) {
        if (obj instanceof  JsonArray) {
            return true;
        }
        return false;
    }

    public static boolean isObject (Object obj) {
        if (obj instanceof  JsonObject) {
            return true;
        }
        return false;
    }
}
