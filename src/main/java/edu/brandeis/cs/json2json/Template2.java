package edu.brandeis.cs.json2json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by shi on 4/9/14.
 */
public class Template2 extends Proxy implements ITemplate  {

    @Override
    public String transform(String template, String... jsons) throws Json2JsonException {
        HashMap<String, Object> cache = new HashMap<String, Object> ();
        for (int i = 0; i < jsons.length; i ++ ) {
            cache.put(String.valueOf(i), jsons[i]);
        }
        Map<String, Object> variables = new HashMap<String, Object>();
        Map<String, Object> functions = new HashMap<String, Object>();
//        System.out.println("input json : " + toJSON(template.trim()));
        Object res = replace(toJSON(template.trim()), variables, functions, cache);
        System.out.println(variables);
        System.out.println(functions);
        System.out.println(cache);
        return res.toString();
    }

    private static final ConcurrentHashMap<String, Method> cache = new ConcurrentHashMap<String, Method>();


    public static Object invokeMethod(String name, Object obj) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        System.out.println("invokeMethod : " + name + " " + obj);
        Object res = Proxy.invoke(name, obj);
        return res;
    }

    public static Object read(String jsonpath, Map<String, Object> cache) throws Json2JsonException{
        if (jsonpath != null){
            int pathBegin = jsonpath.indexOf("$.");
            if (pathBegin > 0) {
                // default put "0"
                String sequence = "0";
                // other sequence "1"
                if (pathBegin > 1)
                    sequence = jsonpath.substring(1, pathBegin);
                String res =  Json2Json.path((String)cache.get(sequence), jsonpath.substring(pathBegin), cache).trim();
                // return different result.
                if (res.startsWith("{")) {
                    return new JSONObject(res);
                } else if (res.startsWith("[")) {
                    return new JSONArray(res);
                } else {
                    return res;
                }
            }
        }
        return null;
    }


    public static Object fromJSON (Object obj) {
        if (obj instanceof String[]) {
            return new JSONArray(obj).toString();
        } else if (obj instanceof Map) {
            return new JSONObject(obj);
        } else {
            return obj;
        }
    }

    // {key:val} --> target
    public static Object replace(Object obj, Map<String, Object> variables, Map<String, Object> functions, Map<String, Object> cache) throws Json2JsonException{
//        System.out.println("replace : " + obj);
        if (obj instanceof JSONObject) {
            JSONObject replacedObj = new JSONObject();
            Iterator<?> keys = ((JSONObject) obj).keys();
            while( keys.hasNext() ){
                String key = (String)keys.next();
                Object val = ((JSONObject) obj).get(key);
                Object replacedVal = null;
                // check key.
                key = key.trim();
                if(key.startsWith("%")) {
                    // 2. define function
                    if (key.startsWith("%!!")) {
                        // 1. define variable
                        replacedVal = replace(val, variables, functions, cache);
                        functions.put(key.substring(3), replacedVal);
                        return new JSONObject();
                    } else if (Proxy.Definitions.containsKey(key) || Proxy.Symbols.containsKey(key)) {
                        replacedVal = replace(val, variables, functions, cache);
                        System.out.println("Proxy : " + key +" " + val +" " + replacedVal);
                        Object res = Proxy.invoke(key, replacedVal);
                        return res;
                    } else if (Process.Definitions.containsKey(key) || Process.Symbols.containsKey(key)) {
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
        } else if (obj instanceof JSONArray) {
            JSONArray replacedVal = new JSONArray();
            for (int i=0; i < ((JSONArray) obj).length(); i++ ) {
                replacedVal.put(replace(((JSONArray) obj).get(i), variables, functions, cache));
            }
            return replacedVal;
        } else if (obj instanceof String) {
            String val = ((String) obj).trim();
            if (val.startsWith("&")) {
                // read
                Object res = read(val, cache);
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
