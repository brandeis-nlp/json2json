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
        Object res = replace(toJSON(template.trim()), variables, functions, cache);
//        System.out.println(variables);
//        System.out.println(functions);
//        System.out.println(cache);
        return res.toString();
    }


    private static final ConcurrentHashMap<String, Method> cache = new ConcurrentHashMap<String, Method>();


    public static Object invokeMethod(String name, Object obj) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
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
        if (obj instanceof JSONObject) {
            JSONObject replacedObj = new JSONObject();
            Iterator<?> keys = ((JSONObject) obj).keys();
            String lastKey = null;
            while( keys.hasNext() ){
                String key = (String)keys.next();
                lastKey = key.trim();
                // 1. define variable
                Object replacedVal = replace(((JSONObject) obj).get(key), variables, functions, cache);
                if (lastKey.startsWith("%!")) {
                    if(variables != null) {
                        variables.put(lastKey.substring(2), replacedVal);
                    }
                }
                replacedObj.put(key, replacedVal);
            }
            if (((JSONObject) obj).length() == 1) {
                if(lastKey.startsWith("%")) {
                    // 2. define function
                    if (lastKey.startsWith("%!!")) {
                        functions.put(lastKey.substring(3), replacedObj.get(lastKey));
                        return new JSONObject();
                    }
                    try {
                        Object res = invokeMethod(lastKey, replacedObj.get(lastKey));
                        if (res != null)
                            return res;
                    } catch (Exception e) {
                        // invoke method error.
                        e.printStackTrace();
                    }
                } else if(Process.Exprs.contains(lastKey)){
                    return Process.expr(replacedObj, null);
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
