package org.lappsgrid.json2json.jsonobject;

import java.util.Collection;

/**
 * Created by lapps on 4/29/2015.
 */
public interface IJsonObj {
    /** read string into jsonObject **/
    public IJsonObj read(String s);
    /** write jsonObject int string **/
    public String toString();

    /** has / get / put / remove **/
    public boolean has(String key);
    public Object get(String key);
    public IJsonObj put(String key, Object obj);
    public IJsonObj remove(String key);

    /**  length / keys **/
    public int length();
    public Collection<String> keys();

    /** if has original mapping **/
    public Object original();

    /** clone a Json Object **/
//        public IJsonObj clone();
}
