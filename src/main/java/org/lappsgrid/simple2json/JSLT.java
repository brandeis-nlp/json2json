package org.lappsgrid.simple2json;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.lappsgrid.json2json.Json2JsonException;
import org.lappsgrid.json2json.jsonobject.JsonProxy;
import org.lappsgrid.json2json.jsonpath.JsonPath;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lapps on 4/19/2015.
 * XSLT for JSON
 * @deprecated
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

    public JSLT(String template, String original){
        tobj = JsonProxy.str2json(template);
        repo = new LinkedHashMap<String, Object>();
        repo.put(JSLTType.type_template.name(), original);
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
        } else {
            jslt = tobj;
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
        String path = (String)match.get(getKeyword(JSLTType.elem_match));
        String origin = (String) repo.get(JSLTType.type_template.name());
        try {
            String value = JsonPath.newPath().path(origin, path);
//            System.out.println("jsonpath-value:" + value);
            repo.put(JSLTType.type_value_of.name(), value);
            return new JSLT(this, arr.get(1)).transform();
        } catch (Json2JsonException e) {
            e.printStackTrace();
            throw new JSLTException(e);
        }
    }

    /**
     *
     *  {"%value-of": {"&select": "JsonPath"}}
     *
     *   <a href="http://www.w3schools.com/xsl/xsl_value_of.asp">Value-Of</a>
     */

    public Object valueOf(Object obj) throws JSLTException{
        JsonProxy.JsonObject json = (JsonProxy.JsonObject)obj;
        JsonProxy.JsonObject select = (JsonProxy.JsonObject)json.get(getKeyword(JSLTType.type_value_of));
        String path = (String)select.get(getKeyword(JSLTType.elem_select));
        String origin = (String)repo.get(JSLTType.type_value_of.name());
        try {
//            System.out.println("orginal:" + origin);
//            System.out.println("path:" + path);
            return JsonProxy.str2json(JsonPath.newPath().path(origin, path));
        } catch (Json2JsonException e) {
            e.printStackTrace();
            throw new JSLTException(e);
        }
    }



    /**
     *
     *  [{"%for-each": [
     *                  {"&select": "JsonPath"},
     *                  {}
     *                  ]}]
     *
     *   <a href="http://www.w3schools.com/xsl/xsl_for_each.asp">For-Each</a>
     */

    public Object forEach(Object obj) throws JSLTException{
        JsonProxy.JsonObject json = (JsonProxy.JsonObject)obj;
        JsonProxy.JsonArray arr = (JsonProxy.JsonArray)json.get(getKeyword(JSLTType.type_for_each));
        if(arr.length() != 2) {
            throw new JSLTException("<"+getKeyword(JSLTType.type_for_each)+"> should have 2 parameters");
        }
        JsonProxy.JsonObject select = (JsonProxy.JsonObject)arr.get(0);
        String path = (String)select.get(getKeyword(JSLTType.elem_select));
        String origin = (String)repo.get(JSLTType.type_template.name());
        try {
            JsonProxy.JsonArray arrForEach = (JsonProxy.JsonArray)
                    JsonProxy.str2json(JsonPath.newPath().path(origin, path));
            JsonProxy.JsonArray ret = JsonProxy.newArray();
            for (int i = 0; i < arrForEach.length(); i++) {
                JSLT child = new JSLT(this, arr.get(1));
                child.repo.put(JSLTType.type_value_of.name(), arrForEach.get(i).toString());
                ret.add(child.transform());
            }
            return ret;
        } catch (Json2JsonException e) {
            e.printStackTrace();
            throw new JSLTException(e);
        }
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

    public Object if_(Object obj) throws JSLTException{
        JsonProxy.JsonObject json = (JsonProxy.JsonObject)obj;
        JsonProxy.JsonArray arr = (JsonProxy.JsonArray)json.get(getKeyword(JSLTType.type_if));
        if(arr.length() != 2) {
            throw new JSLTException("<"+getKeyword(JSLTType.type_if)+"> should have 2 parameters");
        }

        JsonProxy.JsonObject test = (JsonProxy.JsonObject)arr.get(0);
        String expr = (String)test.get(getKeyword(JSLTType.elem_test));
        Binding binding = new Binding();
        JsonProxy.JsonObject target = JsonProxy.newObject().read((String)repo.get(JSLTType.type_value_of.name()));
        for(String key: target.keys()) {
            Object val = target.get(key);
            if(val instanceof JsonProxy.JsonObject || val instanceof JsonProxy.JsonArray)
                continue;
            System.out.println("binding: "+key +" "+ val);
            binding.setVariable(key, val);
        }
        GroovyShell shell = new GroovyShell(binding);
        ;
        System.out.println("expression: "+expr);
        Boolean testEval = (Boolean) shell.evaluate(expr);
        if(testEval) {
            JSLT child = new JSLT(this, arr.get(1));
            return child.transform();
        }
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

    public Object choose(Object obj)throws JSLTException {
        JsonProxy.JsonObject json = (JsonProxy.JsonObject)obj;
        JsonProxy.JsonArray arr = (JsonProxy.JsonArray)json.get(getKeyword(JSLTType.type_choose));
        if(arr.length() != 3) {
            throw new JSLTException("<"+getKeyword(JSLTType.type_choose)+"> should have 2 parameters");
        }

        JsonProxy.JsonObject test = (JsonProxy.JsonObject)arr.get(0);
        String expr = (String)test.get(getKeyword(JSLTType.elem_test));
        Binding binding = new Binding();
        JsonProxy.JsonObject target = JsonProxy.newObject().read((String)repo.get(JSLTType.type_value_of.name()));
        for(String key: target.keys()) {
            Object val = target.get(key);
            if(val instanceof JsonProxy.JsonObject || val instanceof JsonProxy.JsonArray)
                continue;
            binding.setVariable(key, val);
        }
        GroovyShell shell = new GroovyShell(binding);
        Boolean testEval = (Boolean) shell.evaluate(expr);
        if(testEval) {
            JsonProxy.JsonObject whenObj = (JsonProxy.JsonObject)arr.get(1);
            JSLT child = new JSLT(this, whenObj.get(getKeyword(JSLTType.elem_when)));
            return child.transform();
        } else {
            JsonProxy.JsonObject otherwise = (JsonProxy.JsonObject)arr.get(1);
            JSLT child = new JSLT(this, otherwise.get(getKeyword(JSLTType.elem_otherwise)));
            return child.transform();
        }
    }







}
