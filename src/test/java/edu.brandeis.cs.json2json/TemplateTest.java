package edu.brandeis.cs.json2json;


import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;

public class TemplateTest {

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
            "\"@context\": { \"homepage\": \"&$.@context.homepage.*\"  },\n" +
            "\"homepage\": \"&$.homepage\"\n" +
            "}";

    String jsonTrans = "{\n" +
            "\"@context\": { \"homepage\": [\"@id\",\"http:\\/\\/schema.org\\/url\"]  },\n" +
            "\"homepage\": \"http://manu.sporny.org/\"\n" +
            "}";

    String jsonTemplate1 = "{\n" +
            "\"@context\": { \"homepage\": \"&$.@context.homepage.* \" },\n" +
            "\"homepage\": \"%*(%|(&$.homepage,\\\"\\\\\\\\.\\\"), \\\"-\\\")\"\n" +
            "}";

    String jsonTrans1 = "{\n" +
            "\"@context\": { \"homepage\": [\"@id\",\"http:\\/\\/schema.org\\/url\"] },\n" +
            "\"homepage\": \"http://manu-sporny-org/\"\n" +
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
    public void test() throws Exception{
        Template template = new Template();
        String target = template.transform(jsonTemplate, json);
//        System.out.println(target);
        Assert.assertEquals(jsonTrans, target);


        target = template.transform(jsonTemplate1, json);
//        System.out.println(target);
        Assert.assertEquals(jsonTrans1, target);
    }

//    @Test
//    public void testRegex() throws Exception{
//        for(String path: paths) {
//            boolean isMatch = Pattern.matches(Template.JsonPathRegex, path);
//            Assert.assertTrue(isMatch);
//        }
//
//        String [] finds = Template.findJsonPath(jsonTemplate);
//        System.out.println(Arrays.toString(finds));
//        for(int i = 0; i < finds.length; i++){
//            Assert.assertEquals(tempatePaths[i], finds[i]);
//        }
//
//        Map<String, Object> map = Template.findJsonPathContent(jsonTemplate, json);
//        String target = jsonTemplate;
//        for(String path: map.keySet()) {
//            String content = map.get(path).toString();
//            System.out.println(path);
//            System.out.println(content);
//            System.out.println(jsonTemplate.indexOf(path));
//            target =  StringProxy.replace(target, path, content);
//        }
//        System.out.println(target);
//    }
    
}