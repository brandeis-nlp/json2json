package org.lappsgrid.json2json.jsonobject;

import java.util.Collection;

/**
 * Created by lapps on 4/29/2015.
 */
public interface IJsonArr extends IJsonWrapper{
    /** read string into IJsonArr **/
    public IJsonArr read(String s);

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
}
