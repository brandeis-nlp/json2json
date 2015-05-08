package org.lappsgrid.jsondsl;

import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lappsgrid.json2json.jsonobject.JsonProxy;
import org.lappsgrid.simple2json.JSLT;

import java.io.File;

public class TestJDSL {

    public static String readResource(String filename) throws Exception {
        File objFile = new File(TestJDSL.class.getResource("/" + filename).toURI());
        return FileUtils.readFileToString(objFile);
    }

    public static JsonProxy.JsonObject readJsonObject(String filename) throws Exception {
        return JsonProxy.readObject(readResource(filename));
    }



    JsonProxy.JsonObject sources = null;
    JsonProxy.JsonObject templates = null;
    JsonProxy.JsonObject targets = null;

    @Before
    public void setup() throws Exception {
        sources = readJsonObject("testsimple.source.json");
        templates = readJsonObject("testsimple.template.json");
        targets = readJsonObject("testsimple.target.json");
    }

    @After
    public void tear() {}

//    @Test
    public void test() throws Exception{
        for(String name : templates.keys()) {
            JsonProxy.JsonObject template = (JsonProxy.JsonObject)templates.get(name);
            JsonProxy.JsonObject source = (JsonProxy.JsonObject)sources.get(name);
            Object target = targets.get(name);

            System.out.println("----------------  " + name +"  ------------------");
            System.out.println("Template : " + template.toString());
            System.out.println("Source : " + source.toString());
            System.out.println("Expected : " + target.toString());
            String result =  new JSLT(template.toString(), source.toString()).transform().toString();
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
