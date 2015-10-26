package org.lappsgrid.json2json.jsonobject;

import java.util.Collection;

/**
 * Created by lapps on 4/29/2015.
 */
public interface IJsonObj extends IJsonWrapper {
    /** read string into jsonObject **/
    public IJsonObj read(String s);

    /** has / get / put / remove **/
    public boolean has(String key);
    public Object get(String key);
    public IJsonObj put(String key, Object obj);
    public IJsonObj remove(String key);

    /**  length / keys **/
    public int length();
    public Collection<String> keys();


    /** clone a Json Object **/
//        public IJsonObj clone();
}
