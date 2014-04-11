package edu.brandeis.cs.json2json;


import junit.framework.Assert;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ProcessTest {


    static String [] Variables = new String [] {
            "{\"%!v1\" : \"\", \"%!v2\": \"hello\", \"%!v3\" : {}, \"%!v4\" : [] }"
    };


    static String [] Steps = new String[]{
            "[{\"%v1\" :  \"hello\"}, {\"%v3\":\"%v1\" }]"
    };

    static  String [] StepResults = new String [] {
            "{}"
    };



    static String [] EXPRS = {
            "{ \">\": [ 2, 1 ]}",
            "{ \"==\": [ 1, 1 ]}",
            "{ \"<\": [ 2, 3 ]}",
            "{ \"||\": [ { \">\": [ 2, 3 ]}, { \"==\": [ 1, 1 ]} ]}",
            "{\"!\": { \"&&\": [ { \">\": [ 2, 3 ]}, { \"==\": [ 1, 1 ]} ]} }"
    };


    static String [] IF_THEN_ELSE = {
            "{\"%!?\": {\n" +
                    "    \"%$\"    : {   \"%!id\": \"\" },\n" +
                    "    \"%<>\"   : { \"==\": [ \"OpenNLP\", \"OpenNLP\" ]},\n" +
                    "    \"%if\"   : { \"%id\": \"http://www.opennlp.org\"},\n" +
                    "    \"%else\" : { \"%id\": \"http://unknown.org\" },\n" +
                    "    \"%#\"    : \"%id\" } } "
    };

    @Before
    public void setup() throws Exception{

    }

    @After
    public void tear() {}

    @Test
    public void test() throws Exception{
        Object target = null;

        Map<String,Object> map = Process.variables(new JSONObject(Variables[0]), null);
        System.out.println("Before variables : " + map);
        for(int i = 0; i < Steps.length; i ++ ) {
            System.out.println("Step " + i + " " + Steps[i]);
            Object obj = Template2.toJSON(Steps[i]);
            Process.steps(obj, map);
        }
        Assert.assertEquals("hello", map.get("v3"));
        System.out.println("After variables : " + map);


//        Process.if_then_else(new JSONObject(IF_THEN_ELSE[0]), map);
//        System.out.println("If-Then-Else : " + map.get(Process.Map_Ret));

        for(String expr: EXPRS) {
            Process.expr(new JSONObject(expr), map);
            System.out.println("Expr : " + expr);
            System.out.println("Expr : " + map.get(Process.Map_Expr));
            System.out.println();
        }
    }
}