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
package org.lappsgrid.json2json;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.lappsgrid.json2json.jsonobject.JsonProxy;
import org.lappsgrid.json2json.jsonobject.JsonProxy.JsonArray;
import org.lappsgrid.json2json.jsonobject.JsonProxy.JsonObject;
import org.lappsgrid.json2json.template.Process;
import org.lappsgrid.json2json.template.Proxy;

/**
 * Created by shi on 4/9/14.
 */
public class Template extends Proxy implements ITemplate  {


    public static final String Map_JSONs = "____json____";

    @Override
    public String transform(String template, String... jsons) throws Json2JsonException {
        HashMap<String, Object> cache = new HashMap<String, Object> ();
        Map<String, Object> variables = new HashMap<String, Object>();
        Map<String, Object> functions = new HashMap<String, Object>();

        // put variables
        for (int i = 0; i < jsons.length; i ++ ) {
            variables.put(Map_JSONs + String.valueOf(i), jsons[i]);
        }
//        System.out.println("input json : " + toJSON(template.trim()));
        Object res = replace(toJSON(template.trim()), variables, functions, cache);
//        System.out.println(variables);
//        System.out.println(functions);
//        System.out.println(cache);
        return res.toString();
    }

    private static final ConcurrentHashMap<String, Method> cache = new ConcurrentHashMap<String, Method>();


    public static Object invokeMethod(String name, Object obj) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        System.out.println("invokeMethod : " + name + " " + obj);
        Object res = Proxy.invoke(name, obj);
        return res;
    }

    public static Object read(String jsonpath, Map<String, Object> variables,  Map<String, Object> cache) throws Json2JsonException{
//        System.out.println("read : " + jsonpath + " " + variables);
        if (jsonpath != null){
            int pathBegin = jsonpath.indexOf("$.");
            if (pathBegin > 0) {
                // default put "0"
                String sequence = "0";
                // other sequence "1"
                if (pathBegin > 1)
                    sequence = jsonpath.substring(1, pathBegin);
                if (cache == null) {
                    cache = new ConcurrentHashMap<String, Object>();
                }
                String res =  Json2Json.path((String)variables.get(Map_JSONs + sequence), jsonpath.substring(pathBegin), cache).trim();
//                System.out.println("Template2.read : " + jsonpath);
//                System.out.println("Template2.read : " + res);
                // return different result.
                if (res.startsWith("{")) {
                    return JsonProxy.readObject(res);
                } else if (res.startsWith("[")) {
                    return JsonProxy.readArray(res);
                } else {
                    return res;
                }
            }
        }
        return null;
    }


//    public static Object fromJSON (Object obj) {
//        if (obj instanceof String[]) {
//            return new JsonArray(obj).toString();
//        } else if (obj instanceof Map) {
//            return new JsonObject(obj);
//        } else {
//            return obj;
//        }
//    }

    // {key:val} --> target
    public static Object replace(Object obj, Map<String, Object> variables, Map<String, Object> functions, Map<String, Object> cache) throws Json2JsonException{
//        System.out.println("replace : " + obj);
        if (obj instanceof JsonObject) {
            JsonObject replacedObj = JsonProxy.newObject();
            Collection<String> keys = ((JsonObject) obj).keys();
            for(String key: keys){
                Object val = ((JsonObject) obj).get(key);
                Object replacedVal = null;
                // check key.
                key = key.trim();
                if(key.startsWith("%")) {
                    // 2. define function
                    if (key.startsWith("%!!")) {
                        // 1. define variable
                        replacedVal = replace(val, variables, functions, cache);
                        functions.put(key.substring(3), replacedVal);
                        return JsonProxy.newObject();
                    } else if (Proxy.Definitions.containsKey(key) || Proxy.Symbols.containsKey(key)) {
                        replacedVal = replace(val, variables, functions, cache);
//                        System.out.println("Proxy : " + key +" " + val +" " + replacedVal);
                        Object res = Proxy.invoke(key, replacedVal);
                        return res;
                    } else if (org.lappsgrid.json2json.template.Process.Definitions.containsKey(key) || Process.Symbols.containsKey(key)) {
                        Map<String, Object> map = new HashMap<String, Object>(variables);
                        return Process.invoke(key, val, map);
                    }
                } else if(Process.Exprs.contains(key)){
                    replacedVal = replace(val, variables, functions, cache);
                    replacedObj.put(key, replacedVal);
                    return Process.expr(replacedObj, variables);
                } else {
                    replacedVal = replace(val, variables, functions, cache);
                    replacedObj.put(key, replacedVal);
                }
            }
            return replacedObj;
        } else if (obj instanceof JsonArray) {
            JsonArray replacedVal = JsonProxy.newArray();
            for (int i=0; i < ((JsonArray) obj).length(); i++ ) {
                replacedVal.add(replace(((JsonArray) obj).get(i), variables, functions, cache));
            }
            return replacedVal;
        } else if (obj instanceof String) {
            String val = ((String) obj).trim();
            if (val.startsWith("&")) {
                // read
                Object res = read(val, variables, cache);
                if (res != null) {
                    return res;
                }
            } else if (val.startsWith("%")) {
                // variable
                if (variables != null) {
                    Object res = variables.get(val.substring(1));
                    if (res != null) {
                        return res;
                    }
                }
            }
            return obj;
        } else {
            return obj;
        }
    }

}
