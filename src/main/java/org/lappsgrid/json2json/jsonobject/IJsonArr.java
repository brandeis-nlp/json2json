package org.lappsgrid.json2json.jsonobject;

import java.util.Collection;

/**
 * Created by lapps on 4/29/2015.
 */
public interface IJsonArr {
    /** read string into IJsonArr **/
    public IJsonArr read(String s);
    /** write IJsonArr int string **/
    public String toString();

    /** length / get / add / insert / remove **/
    public int length();
    public Object get(int i);
    public IJsonArr add(Object obj);
    public IJsonArr remove(int i);
    public IJsonArr set(int i, Object obj);

    /** directly read string array as Json Array object. **/
    public IJsonArr convert(String [] arr);
    public IJsonArr convert(Collection<String> arr);

    /** clone a Json array **/
//        public IJsonArr clone();

    /** if has original mapping **/
    public Object original();
}
