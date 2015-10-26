package org.lappsgrid.json2json.jsonobject;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by 310201833 on 2015/10/26.
 */
public class JacksonJsonArr extends JacksonJsonProxy  implements IJsonArr {

    List<Object> list = null;

    public JacksonJsonArr(List<Object> list) {
        this.list = list;
    }

    public JacksonJsonArr() {
        list = new ArrayList<Object>();
    }

    public IJsonArr read(String s) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            list = (List<Object>) mapper.readValue(s, List.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }


    public int length() {
        return list.size();
    }


    public Object get(int i) {
        return wrap(list.get(i));
    }


    public IJsonArr add(Object s) {
        list.add(unwrap(s));
        return this;
    }


    public IJsonArr remove(int i) {
        list.remove(i);
        return this;
    }


    public IJsonArr set(int i, Object obj) {
        list.set(i, obj);
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

//
//        public IJsonArr clone() {
//            return new JacksonJsonArr(new ArrayList<Object>(list));
//        }


    public Object original() {
        return list;
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
}