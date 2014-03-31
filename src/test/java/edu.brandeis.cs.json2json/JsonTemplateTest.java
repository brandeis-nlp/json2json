package edu.brandeis.cs.json2json;


import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import scala.util.parsing.json.JSON;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;

public class JsonTemplateTest {

    String [] paths = new String []{
        "$.store.book[*].author",
        "$..author",
        "$..book[(@.length-1)]",
        "$..book[?(@.price<10)]"
    };

    String json = "    {\n" +
            "    \"@context\": {\n" +
            "    \"name\": \"http://schema.org/name\",\n" +
            "    \"homepage\": {\n" +
            "    \"@id\": \"http://schema.org/url\",\n" +
            "    \"@type\": \"@id\"\n" +
            "    },\n" +
            "    \"image\": {\n" +
            "    \"@id\": \"http://schema.org/image\",\n" +
            "    \"@type\": \"@id\"\n" +
            "    }\n" +
            "    },\n" +
            "    \"image\": \"http://manu.sporny.org/images/manu.png\",\n" +
            "    \"name\": \"Manu Sporny\",\n" +
            "    \"homepage\": \"http://manu.sporny.org/\"\n" +
            "    }";

    String jsonTemplate = "{\n" +
            "\"@context\": { \"&$.@context.homepage.*\" },\n" +
            "\"homepage\": \"&$.homepage\"\n" +
            "}";

    String[] tempatePaths = new String []{
            "&$.@context.homepage.*",
            "&$.homepage"
    };

    @Before
    public void setup() throws Exception{

    }

    @After
    public void tear() {}

    @Test
    public void testRegex() throws Exception{
        for(String path: paths) {
            boolean isMatch = Pattern.matches(JsonTemplate.JsonPathRegex, path);
            Assert.assertTrue(isMatch);
        }

        String [] finds = JsonTemplate.findJsonPath(jsonTemplate);
        System.out.println(Arrays.toString(finds));
        for(int i = 0; i < finds.length; i++){
            Assert.assertEquals(tempatePaths[i], finds[i]);
        }

        Map<String, Object> map = JsonTemplate.findJsonPathContent(jsonTemplate, json);
        String target = jsonTemplate;
        for(String path: map.keySet()) {
            String content = map.get(path).toString();
            System.out.println(path);
            System.out.println(content);
            System.out.println(jsonTemplate.indexOf(path));
            target =  StringProxy.replace(target, path, content);
        }
        System.out.println(target);
    }
    
}