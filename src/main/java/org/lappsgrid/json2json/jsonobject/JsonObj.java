package org.lappsgrid.json2json.jsonobject;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shi on 5/15/14.
 */
public class JsonObj implements IJsonObj {

    Map<String, Object> map = null;

    public JsonObj(String json){
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = (Map)mapper.readValue(json, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public IJsonObj read(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = (Map)mapper.readValue(json, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        try {
            return mapper.writeValueAsString(this.map);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JsonObj(Map map){
        this.map = map;
    }

    public JsonObj(){
        this.map = new LinkedHashMap<String, Object>();
    }

    public Object get(String key) {
        return this.map.get(key);
    }

    public String getString(String key) {
        Object val = this.get(key);
        if (val != null) {
            return (String)val;
        }
        return null;
    }

    public Long getLong(String key) {
        Object val = this.get(key);
        if (val != null) {
            return (Long)val;
        }
        return null;
    }

    public Double getDouble(String key) {
        Object val = this.get(key);
        if (val != null) {
            return (Double)val;
        }
        return null;
    }

    public Boolean getBoolean(String key) {
        Object val = this.get(key);
        if (val != null) {
            return (Boolean)val;
        }
        return null;
    }

    public Integer getInt(String key) {
        Object val = this.get(key);
        if (val != null) {
            return ((Integer)val);
        }
        return null;
    }

    public JsonArr getJsonArr(String key){
        Object val = this.get(key);
        if (val != null) {
            return new JsonArr((List)val);
        }
        return null;
    }

    public boolean has(String key) {
        return this.map.containsKey(key);
    }

    public JsonObj getJsonObj(String key){
        Object val = this.get(key);
        if (val != null) {
            return new JsonObj((Map)val);
        }
        return null;
    }

    public JsonObj put(String key, Object obj) {
        if(obj instanceof JsonArr) {
            obj = ((JsonArr)obj).list;
        } else if (obj instanceof JsonObj) {
            obj = ((JsonObj)obj).map;
        }
        this.map.put(key, obj);
        return this;
    }

    public IJsonObj remove(String key) {
        this.map.remove(key);
        return this;
    }

    public int length() {
        return this.map.size();
    }

    public Collection<String> keys() {
        return this.map.keySet();
    }

    public Object original() {
        return this.map;
    }

    @Override
    public boolean equals(Object o) {
        JsonObj obj = (JsonObj) o;
        if (obj == null)
            return false;
        if (this.map == null && obj.map == null)
            return true;
        if (this.map != null && obj.map != null) {
            return this.map.equals(obj.map);
        }
        return false;
    }

}