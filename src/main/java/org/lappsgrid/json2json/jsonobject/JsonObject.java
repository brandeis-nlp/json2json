package org.lappsgrid.json2json.jsonobject;

import java.util.Collection;

/**
 * Created by lapps on 4/29/2015.
 */
public interface JsonObject {
    /** read string into jsonObject **/
    public JsonObject read(String s);
    /** write jsonObject int string **/
    public String toString();

    /** has / get / put / remove **/
    public boolean has(String key);
    public Object get(String key);
    public JsonObject put(String key, Object val);
    public JsonObject remove(String key);

    /**  length / keys **/
    public int length();
    public Collection<String> keys();

    /** if has original mapping **/
    public Object original();

    /** clone a Json Object **/
//        public JsonObject clone();
}
