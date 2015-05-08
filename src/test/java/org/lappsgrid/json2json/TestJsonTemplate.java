package org.lappsgrid.json2json;


import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lappsgrid.json2json.jsonobject.JsonProxy;
import org.lappsgrid.json2json.jsonobject.JsonProxy.JsonObject;

import java.io.File;

public class TestJsonTemplate {
    public static String readResource(String filename) throws Exception {
        File objFile = new File(TestJsonTemplate.class.getResource("/" + filename).toURI());
        return FileUtils.readFileToString(objFile);
    }

    public static JsonObject readJsonObject(String filename) throws Exception {
        return JsonProxy.readObject(readResource(filename));
    }

    @Test
    public void testCase1() throws Exception{
        JsonObject source = readJsonObject("testcase1.source.json");
        JsonObject dummy = readJsonObject("testcase1.dummy.json");
        JsonObject template = readJsonObject("testcase1.template.json");
        JsonObject expected = readJsonObject("testcase1.expected.json");
        String result = Json2Json.transform(template.toString(), source.toString(), dummy.toString());
        System.out.println("Result : " + result.toString());
    }

    JsonObject sources = null;
    JsonObject templates = null;
    JsonObject targets = null;
    Json2Json json2json = null;

    @Before
    public void setup() throws Exception {
        sources = readJsonObject("testcase.source.json");
        templates = readJsonObject("testcase.template.json");
        targets = readJsonObject("testcase.target.json");
        json2json = new Json2Json();
    }

    @After
    public void tear() {}

//    @Test
    public void test() throws Exception{
        for(String name : templates.keys()) {
            JsonObject template = (JsonObject)templates.get(name);
            JsonObject source = (JsonObject)sources.get(name);
            Object target = targets.get(name);

            System.out.println("----------------  " + name +"  ------------------");
            System.out.println("Template : " + template.toString());
            System.out.println("Source : " + source.toString());
            System.out.println("Expected : " + target.toString());
            String result = json2json.transform(template.toString(), source.toString());
            System.out.println("Result : " + result.toString());
            if(name.equals("error_wrong_jsonpath")) {
                Assert.assertTrue(result.contains("Exception"));
            } else {
                Assert.assertEquals(target.toString(), result);
            }
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }
    }
}