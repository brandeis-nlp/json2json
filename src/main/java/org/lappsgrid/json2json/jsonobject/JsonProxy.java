package org.lappsgrid.json2json.jsonobject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.lappsgrid.json2json.Json2JsonException;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by 310201833 on 2015/10/26.
 */
public class JsonProxy {

    public static String toJsonString(Object obj) throws Json2JsonException {
        ObjectWriter ow = new ObjectMapper().writer();
        String json = null;
        try {
            json = ow.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new Json2JsonException(e);
        }
        return json;
    }

    /**
     * Json util read string as Json object
     * @param json
     * @return
     */
    public static Object fromJsonString(String json) {
        if (json == null)
            return null;
        json = json.trim();
        if (json.startsWith("{")) {
            return new JsonObj(json);
            // return JsonProxy.readObject(json);
        } else if (json.startsWith("[")) {
            return new JsonArr(json);
            // return JsonProxy.readArray(json);
        } else {
            return json;
        }
    }


    public static Object unwrap(Object obj) {
        if (obj instanceof  IJsonWrapper) {
            return ((IJsonWrapper) obj).original();
        }
        return obj;
    }

    public static IJsonArr newArr() {
        return new JsonArr();
    }

    public static IJsonObj newObj() {
        return new JsonObj();
    }

    public static Object wrap(Object obj ) {
        if(obj instanceof List) {
            return new JsonArr((List)obj);
        } else if(obj instanceof Map) {
            return new JsonObj((Map)obj);
        }
        return obj;
    }

//    public static interface NewProxy {
//        public IJsonArr newArray();
//        public IJsonObj newObject();
//    }
    /** static methods for read / is / convert **/
//
//    public static IJsonArr convertArray(String [] arr) {
//        return newArray().convert(arr);
//    }
//    public static IJsonArr convertArray(Collection<String> arr) {
//        return newArray().convert(arr);
//    }
//
//    public static IJsonArr readArray(String s) {
//        return newArray().read(s);
//    }
//
//    public static IJsonObj readObject(String s) {
//        return newObject().read(s);
//    }

    public static boolean isArray (Object obj) {
        if (obj instanceof IJsonArr) {
            return true;
        }
        return false;
    }
    public static boolean isObject (Object obj) {
        if (obj instanceof IJsonObj) {
            return true;
        }
        return false;
    }

    /** fill in the proxy **/


/*    public static IJsonArr newArray(){
        return newProxy().newArr();
    }
    public static IJsonObj newObject(){
        return newProxy().newObj();
    }

    public static IJsonProxy newProxy() {
        // FIXME: exchange other proxy
        return new JacksonJsonProxy();
    }*/






}