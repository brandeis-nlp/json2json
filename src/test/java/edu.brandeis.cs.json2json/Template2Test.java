package edu.brandeis.cs.json2json;


import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class Template2Test {

    public static final String readResources (String name) throws Exception{
        File objFile = new File(Json2JsonTest.class.getResource("/" + name).toURI());
        return FileUtils.readFileToString(objFile);
    }


    public static final void assertEqualJSON(String json1, String json2) {
        Assert.assertEquals(new JSONObject(json1).toString(), new JSONObject(json2).toString());
    }

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

    String [] Processes = {
            "{\"%!for\": {\n" +
                    "    \"%$\"       : {   \"%!s\": \"\" },\n" +
                    "    \"%[]\"      : [ [\"hello\", \"world\"], \"%i\", \"%e\"],\n" +
                    "    \"%each\" : {\"%s\": {\"%+\": [\"%s\", \"%e\"]}},\n" +
                    "    \"%#\"       : \"%s\" } }",
    };


    String [] ProcessResults = {
            "helloworld"
    };



    int NumberOfResources = 2;


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
        String temp = null;
        Template2 template = new Template2();
//        target = template.transform("{\"%v2\" : {\"%!v2\" : \"hello\"} } ", "{}");
//        System.out.println(target);
//
        // String Filters

        for (int i = 0; i < StringFilters.length; i++) {
            in = StringFilters[i];
            target = StringFilterResults[i];
//            System.out.println("<--------------------------");
            out = template.transform(in, "{}");
//            System.out.println("in : " + in);
//            System.out.println("out : " + out);
//            System.out.println("-------------------------->");
//            System.out.println();
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
//            System.out.println("<--------------------------");
            out = template.transform(in, "{}");
//            System.out.println("in : " + in);
//            System.out.println("out : " + out);
//            System.out.println("-------------------------->");
//            System.out.println();
            Assert.assertEquals(target, out);
        }




        for (int i = 0; i < Processes.length; i++) {
            in = Processes[i];
            target = ProcessResults[i];
//            System.out.println("<--------------------------");
            out = template.transform(in, "{}");
//            System.out.println("in : " + in);
//            System.out.println("out : " + out);
//            System.out.println("-------------------------->");
//            System.out.println();
            Assert.assertEquals(target, out);
        }

        in = readResources("obj.json");
        out = template.transform("{\"jsonpath\":\"&$.Items.Request\"}", in);
//        System.out.println(out);
        target = "{\"jsonpath\":{\"ItemSearchRequest\":{\"ResponseGroup\":[\"Offers\",\"Similarities\",\"Small\"," +
                "\"SalesRank\",\"SearchBins\",\"Images\"],\"ItemPage\":\"1\"," +
                "\"SearchIndex\":\"All\",\"Keywords\":\"steve jobs\"}," +
                "\"IsValid\":\"True\"}}";

        assertEqualJSON(target, out);

        for (int i = 0; i < NumberOfResources; i++) {
            in = readResources(i + "in.json");
            target = readResources(i + "out.json");
            temp = readResources(i + "temp.json");
            out = template.transform(temp, in);

//            System.out.println("---------- " + i + " -------------------");
//            System.out.println(out);
//            System.out.println();
            Assert.assertEquals(new JSONObject(target).toString(), new JSONObject(out).toString());
        }
    }

    
}