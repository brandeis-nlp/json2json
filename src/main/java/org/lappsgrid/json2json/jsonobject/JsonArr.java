package org.lappsgrid.json2json.jsonobject;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by shi on 5/15/14.
 */
public class JsonArr implements IJsonArr {

    List<Object> list = null;

    public JsonArr(List list){
        this.list = list;
    }

    public JsonArr(String json){
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.list  = (List) mapper.readValue(json, List.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public IJsonArr read(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.list  = (List) mapper.readValue(json, List.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this.list);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public JsonArr(){
        list = new ArrayList<Object>();
    }

    public Object get(int i) {
        return this.list.get(i);
    }

    public IJsonArr add(Object obj) {
        this.list.add(obj);
        return this;
    }

    public IJsonArr remove(int i) {
        this.list.remove(i);
        return this;
    }

    public IJsonArr set(int i, Object obj) {
        this.list.set(i, obj);
        return this;
    }

    public IJsonArr convert(String[] arr) {
        for(String s: arr) {
            list.add(s);
        }
        return this;
    }

    public IJsonArr convert(Collection<String> arr) {
        for(String s: arr) {
            list.add(s);
        }
        return this;
    }

    public Object original() {
        return this.list;
    }

    public JsonArr put(Object obj) {
        if(obj instanceof JsonArr) {
            obj = ((JsonArr)obj).list;
        } else if (obj instanceof JsonObj) {
            obj = ((JsonObj)obj).map;
        }
        this.list.add(obj);
        return this;
    }


    public int length() {
        return this.list.size();
    }

    public JsonObj getJsonObj(int i) {
        Object val = get(i);
        if (val != null) {
            return new JsonObj((Map)val);
        }
        return null;
    }

    public JsonArr getJsonArr(int i) {
        Object val = get(i);
        if (val != null) {
            return new JsonArr((List)val);
        }
        return null;
    }

    public String getString(int i) {
        Object val = this.get(i);
        if (val != null) {
            return (String)val;
        }
        return null;
    }

    public Long getLong(int i) {
        Object val = this.get(i);
        if (val != null) {
            return (Long)val;
        }
        return null;
    }

    public Double getDouble(int i) {
        Object val = this.get(i);
        if (val != null) {
            return (Double)val;
        }
        return null;
    }

    public Boolean getBoolean(int i) {
        Object val = this.get(i);
        if (val != null) {
            return (Boolean)val;
        }
        return null;
    }

    public Integer getInt(int i) {
        Object val = this.get(i);
        if (val != null) {
            return (Integer)val;
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        JsonArr arr = (JsonArr)o;
        if (arr == null)
            return false;
        if (this.list == null && arr.list == null)
            return true;
        if (this.list != null && arr.list != null) {
            return this.list.equals(arr.list);
        }
        return false;
    }

}