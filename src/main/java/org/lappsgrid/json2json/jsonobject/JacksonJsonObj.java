package org.lappsgrid.json2json.jsonobject;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by 310201833 on 2015/10/26.
 */
public class JacksonJsonObj extends JacksonJsonProxy implements IJsonObj {

    Map<String, Object> map = null;

    public JacksonJsonObj() {
        map = new LinkedHashMap<String, Object>();
    }

    public JacksonJsonObj(Map<String, Object> map) {
        this.map = map;
    }


    public IJsonObj read(String s) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = (Map)mapper.readValue(s, LinkedHashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }


    public boolean has(String key) {
        return map.containsKey(key);
    }


    public Object get(String key) {
        return wrap(map.get(key));
    }


    public IJsonObj put(String key, Object val) {
        map.put(key, unwrap(val));
        return this;
    }


    public IJsonObj remove(String key) {
        map.remove(key);
        return this;
    }


    public int length() {
        return map.size();
    }


    public Collection<String> keys() {
        return map.keySet();
    }


    public Object original() {
        return map;
    }

    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this.map);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

