package org.lappsgrid.json2json.jsonobject;

import java.util.Collection;

/**
 * Created by lapps on 4/29/2015.
 */
public interface JsonArray {
    /** read string into JsonArray **/
    public JsonArray read(String s);
    /** write JsonArray int string **/
    public String toString();

    /** length / get / add / insert / remove **/
    public int length();
    public Object get(int i);
    public JsonArray add(Object s);
    public JsonArray remove(int i);
    public JsonArray set(int i, Object s);

    /** directly read string array as Json Array object. **/
    public JsonArray convert(String [] arr);
    public JsonArray convert(Collection<String> arr);

    /** clone a Json array **/
//        public JsonArray clone();

    /** if has original mapping **/
    public Object original();
}
