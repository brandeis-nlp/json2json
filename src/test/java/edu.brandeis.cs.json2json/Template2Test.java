package edu.brandeis.cs.json2json;


import junit.framework.Assert;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Template2Test {

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

    String[] tempatePaths = new String []{
            "&$.@context.homepage.*",
            "&$.homepage"
    };

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
            "\"homepage\": \"%*(%|(&$.homepage,\\\".\\\"), \\\"-\\\")\"\n" +
            "}";

    String jsonTrans1 = "{\n" +
            "\"@context\": { \"homepage\": [\"@id\",\"http:\\/\\/schema.org\\/url\"] },\n" +
            "\"homepage\": \"http://manu-sporny-org/\"\n" +
            "}";

    String jsonTemplate2 = "{\n" +
            "\"@context\": { \"homepage\": \"&$.@context.homepage.* \" },\n" +
            "\"homepage\": \"%*(%|(&$.homepage,\\\".\\\"), \\\"-\\\")\",\n" +
            "\"name\": \"%|(%-([\\\"hello\\\", \\\".world\\\"]){ %r += %e;},\\\".\\\")\"\n" +
            "}";

    String jsonTrans2 = "{\n" +
            "\"@context\": { \"homepage\": [\"@id\",\"http:\\/\\/schema.org\\/url\"] },\n" +
            "\"homepage\": \"http://manu-sporny-org/\",\n" +
            "\"name\": [\"hello\",\"world\"]\n" +
            "}";


    String [] StringFilters = new String [] {
            "{\"%+\" : [\"hello\",\"world\"]}",
            "{\"%|\" : [\"hello.world\",\".\"]} ",
            "{\"%*\" : [[\"hello\", \"world\"],\".\"]}",
            "{\"%?\" : [\"hello.world\", \"hello\"]}",
            "{\"%_\" : [\"hello.world\", 0, 5]}",
            "{\"%#\" : \"hello world\"} ",
            "{\"%/\" : [\"hello.world\", \".\", \" \"]} ",
            "{\"%%\" : [\"hello.world\", \"[a-z]+\"]} ",
            "{\"%%|\" : [\"hello.world\", \".l\"]} ",
            "{\"%%/\" : [\"hello.world\", \".l\", \"-\"]}",
            "{\"%&\" : [{\"hello\" : \"1\", \"world\" : \"2\"}, \"$.hello\"]} ",
            "{\"%+\" : {\"%%\" : [\"hello.world\", \"[a-z]+\"]} }",
            "{\"%%/\" : [{\"%+\" : {\"%%\" : [\"hello.world\", \"[a-z]+\"]} }, \".l\", \"-\"]}"
    };

    String [] StringFilterResults = new String [] {
            "helloworld",
            "[\"hello\",\"world\"]",
            "hello.world",
            "0",
            "hello",
            "11",
            "hello world",
            "[\"hello\",\"world\"]",
            "[\"h\",\"lo.wo\",\"d\"]",
            "h-lo.wo-d",
            "1",
            "helloworld",
            "h-lowo-d"
    };

    String [] ArrayOperators = new String [] {
            "{\"%]+\" : [ [\"hello\",\"world\"], \"!\" ] }",
            "{\"%]-\" : [ [\"hello\",\"world\"], 0] } ",
            "{\"%]$\" : [ [\"hello\", \"world\"], 0]}",
            "{\"%]_\" : [ [\"hello\", \"world\"], 0, 1]} ",
            "{\"%]?\" : [ [\"hello\", \"world\"],\"hello\"]} ",
            "{\"%]#\" : [\"hello\", \"world\"]} "
    };

    String [] ArrayOperatorResults = new String [] {
            "[\"hello\",\"world\",\"!\"]",
            "[\"world\"]",
            "hello",
            "[\"hello\"]",
            "0",
            "2",
    };

    String [] MapOperators = new String [] {
            "{\"%}+\" : [{}, {\"hello\":\"world\"}] } ",
            "{\"%}$\" : [{\"hello\":\"world\"},\"hello\"]} ",
            "{\"%}-\" : [{\"hello\" : \"world\"}, \"hello\"]}",
            "{\"%}#\" : {\"hello\":\"world\"}} ",
            "{\"%}*\" : {\"hello\":\"world\"}}"
    };

    String [] MapOperatorResults = new String [] {
            "{\"hello\":\"world\"}",
            "world",
            "{}",
            "1",
            "[\"hello\"]"
    };






    @Before
    public void setup() throws Exception{

    }

    @After
    public void tear() {}

    @Test
    public void test() throws Exception{
        String target = null;
        String in = null;
        String out = null;
        Template2 template = new Template2();
//        target = template.transform("{\"%v2\" : {\"%!v2\" : \"hello\"} } ", "{}");
//        System.out.println(target);
//
        // String Filters

        for (int i = 0; i < StringFilters.length; i++) {
            in = StringFilters[i];
            target = StringFilterResults[i];
            System.out.println("<--------------------------");
            out = template.transform(in, "{}");
            System.out.println("in : " + in);
            System.out.println("out : " + out);
            System.out.println("-------------------------->");
            System.out.println();
            Assert.assertEquals(target, out);
        }



        for (int i = 0; i < ArrayOperators.length; i++) {
            in = ArrayOperators[i];
            target = ArrayOperatorResults[i];
//            System.out.println("<--------------------------");
            out = template.transform(in, "{}");
//            System.out.println("in : " + in);
//            System.out.println("out : " + out);
//            System.out.println("-------------------------->");
            System.out.println();
            Assert.assertEquals(target, out);
        }



        for (int i = 0; i < MapOperators.length; i++) {
            in = MapOperators[i];
            target = MapOperatorResults[i];
            System.out.println("<--------------------------");
            out = template.transform(in, "{}");
            System.out.println("in : " + in);
            System.out.println("out : " + out);
            System.out.println("-------------------------->");
            System.out.println();
            Assert.assertEquals(target, out);
        }
    }

    
}