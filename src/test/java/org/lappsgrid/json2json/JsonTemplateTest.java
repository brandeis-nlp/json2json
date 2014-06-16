package org.lappsgrid.json2json;


import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lappsgrid.json2json.jsonobject.JsonProxy;
import org.lappsgrid.json2json.jsonobject.JsonProxy.JsonObject;
import org.lappsgrid.json2json.jsonpath.JayWayJsonPath;

import java.io.File;
import java.net.URISyntaxException;

public class JsonTemplateTest {
    public static String readResource(String filename) throws Exception {
        File objFile = new File(JsonTemplateTest.class.getResource("/" + filename).toURI());
        return FileUtils.readFileToString(objFile);
    }

    public static JsonObject readJsonObject(String filename) throws Exception {
        return JsonProxy.readObject(readResource(filename));
    }



    JsonObject sources = null;
    JsonObject templates = null;
    JsonObject targets = null;
    JsonTemplate.Json2Json json2json = null;

    @Before
    public void setup() throws Exception {
        sources = readJsonObject("testcase.source.json");
        templates = readJsonObject("testcase.template.json");
        targets = readJsonObject("testcase.target.json");
        json2json = new JsonTemplate.Json2Json();
    }

    @After
    public void tear() {}

    @Test
    public void test() throws Exception{
        for(String name : templates.keys()) {
            JsonObject template = (JsonObject)templates.get(name);
            JsonObject source = (JsonObject)sources.get(name);
            Object target = targets.get(name);

            System.out.println("----------------" + name +"------------------");
            System.out.println("Template : " + template.toString());
            System.out.println("Source : " + source.toString());
            System.out.println("Expected : " + target.toString());
            String result = json2json.transform(template.toString(), source.toString());
            System.out.println("Result : " + result.toString());
            Assert.assertEquals(target.toString(), result);
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }
    }
    
}