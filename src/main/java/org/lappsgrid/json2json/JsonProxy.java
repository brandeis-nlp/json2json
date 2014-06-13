package org.lappsgrid.json2json;

import com.eclipsesource.json.JsonObject;

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
    }

    public static interface JsonArray {
        public Object get(int i);
        public JsonArray add(Object s);                                                                                                                                                                                                                              u                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               huju
        public JsonArray remove(int i);
        public int length();
        public String toString();
        public JsonArray read(String s);
        public JsonArray convert(String [] arr);
    }

    public static class MinimalJsonObject implements JsonObject{
        com.eclipsesource.json.JsonObject jsonObject = null;

        public JsonObject read(String s) {
            jsonObject = com.eclipsesource.json.JsonObject.readFrom(s);
            return this;
        }

        protected MinimalJsonObject() {
            jsonObject = new com.eclipsesource.json.JsonObject();
        }

        public MinimalJsonObject(com.eclipsesource.json.JsonObject obj){
            jsonObject = obj;
        }

        @Override
        public Object get(String key) {
            Object obj = jsonObject.get(key);
            if (obj instanceof  com.eclipsesource.json.JsonObject) {
                return new MinimalJsonObject((com.eclipsesource.json.JsonObject)obj);
            } else
            if (obj instanceof  com.eclipsesource.json.JsonArray) {
                return new MinimalJsonArray((com.eclipsesource.json.JsonArray)obj);
            } else
                return obj;
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
    }

    public static class MinimalJsonArray implements JsonArray{
        com.eclipsesource.json.JsonArray jsonArray = null;

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
            Object obj = jsonArray.get(i);
            if (obj instanceof  com.eclipsesource.json.JsonObject) {
                return new MinimalJsonObject((com.eclipsesource.json.JsonObject)obj);
            } else
            if (obj instanceof  com.eclipsesource.json.JsonArray) {
                return new MinimalJsonArray((com.eclipsesource.json.JsonArray)obj);
            } else
                return obj;
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
