package org.lappsgrid.simple2json;

import org.lappsgrid.json2json.jsonobject.JsonProxy;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lapps on 4/19/2015.
 * XSLT for JSON
 */
public class JSLT implements IJSLT{


    public static JSLTType getType(JsonProxy.JsonObject obj) {
        for (String[] arrName: Namings) {
            Object val = obj.get(arrName[1] + arrName[2]);
            if(val != null) {
                return JSLTType.valueOf(arrName[0]);
            }
        }
        return null;
    }
    static Map<JSLTType, String> keywords = new HashMap<JSLTType, String>();

    public static String getKeyword(JSLTType type) {
        if(keywords.size() == 0) {
            for (String[] arrName: Namings) {
                keywords.put(JSLTType.valueOf(arrName[0]), arrName[1] + arrName[2]);
            }
        }
        return keywords.get(type);
    }


    // template object
    Object tobj;
    // jslt transformed object
    Object jslt;
    // hold the data in the repository, in top -> down process.
    Map<String, Object> repo;

    public static final String RepositoryOriginalJson = "__original__json__";

    public JSLT(String template, String original){
        tobj = JsonProxy.str2json(template);
        repo = new LinkedHashMap<String, Object>();
        repo.put(RepositoryOriginalJson, JsonProxy.str2json(original));
    }

    protected JSLT(JSLT parent, Object inObj){
        tobj = inObj;
        repo = new LinkedHashMap<String, Object>();
        // copy the data from parent
        repo.putAll(parent.repo);
    }

    public Object transform() throws  JSLTException {
        if(tobj instanceof JsonProxy.JsonObject) {
            JsonProxy.JsonObject json = (JsonProxy.JsonObject) tobj;
            Object type = getType(json);
            if (type == JSLTType.type_template) {
                jslt = template(json);
            } else if (type == JSLTType.type_value_of) {
                jslt = valueOf(json);
            } else if (type == JSLTType.type_choose) {
                jslt = choose(json);
            } else if (type == JSLTType.type_for_each) {
                jslt = forEach(json);
            } else if (type == JSLTType.type_if) {
                jslt = if_(json);
            } else {
                jslt = JsonProxy.newObject();
                for (String key : ((JsonProxy.JsonObject) tobj).keys()) {
                    JSLT child = new JSLT(this, ((JsonProxy.JsonObject) tobj).get(key));
                    ((JsonProxy.JsonObject)jslt).put(key, child.transform());
                }
            }
        } else if(tobj instanceof JsonProxy.JsonArray) {
            jslt = JsonProxy.newArray();
            for (int i = 0; i < ((JsonProxy.JsonArray) tobj).length(); i++) {
                JSLT child = new JSLT(this, ((JsonProxy.JsonArray) tobj).get(i));
                ((JsonProxy.JsonArray)jslt).add(child.transform());
            }
        } else if(tobj instanceof String) {
            jslt = tobj;
        } else {
            throw new JSLTException("Unknown Object Type: " + tobj.getClass());
        }
        return jslt;
    }


    /**
     *   {"%template": [
     *                  {"&match": "JsonPath" },
     *                  {}
     *                 ]}
     *   <a href="https://github.com/jayway/JsonPath">JsonPath</a>
     *   <a href="http://www.w3schools.com/xsl/xsl_templates.asp">Template</a>
     *
     */

    public Object template(Object obj) throws JSLTException {
        JsonProxy.JsonObject json = (JsonProxy.JsonObject)obj;
        JsonProxy.JsonArray arr = (JsonProxy.JsonArray)json.get(getKeyword(JSLTType.type_template));
        if(arr.length() != 2) {
            throw new JSLTException("Template should have 2 parameters");
        }
        JsonProxy.JsonObject match = (JsonProxy.JsonObject)arr.get(0);
//        JsonPath ;




        return null;
    }

    /**
     *
     *  {"%value-of": [
     *                  {"&select": "JsonPath"},
     *                  {}
     *                  ]}
     *
     *   <a href="http://www.w3schools.com/xsl/xsl_value_of.asp">Value-Of</a>
     */

    public Object valueOf(Object json) {
        return null;
    }



    /**
     *
     *  {"%for-each": [
     *                  {"&select": "JsonPath"},
     *                  {}
     *                  ]}
     *
     *   <a href="http://www.w3schools.com/xsl/xsl_for_each.asp">For-Each</a>
     */

    public Object forEach(Object json) {
        return null;
    }


    /**
     *
     *  {"%if": [
     *                  {"&test": ""},
     *                  {}
     *                  ]}
     *
     *   <a href="http://www.w3schools.com/xsl/xsl_if.asp">If</a>
     */

    public Object if_(Object json) {
        return null;
    }


    /**
     *
     *  {"%choose": [
     *                  {"&test": ""},
     *                  {"&when": },
     *                  {"&otherwise": }
     *                  ]}
     *
     *   <a href="http://www.w3schools.com/xsl/xsl_choose.asp">Choose</a>
     */

    public Object choose(Object json) {
        return null;
    }








}
