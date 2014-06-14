package org.lappsgrid.json2json;


import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lappsgrid.json2json.jsonobject.JsonProxy;

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
    static String [] IF_THEN_ELSE_Results = {
        "http://www.opennlp.org",
    };

    static String [] FOR_EACH = {
            "{\"%!for\": {\n" +
                    "    \"%$\"       : {   \"%!s\": \"\" },\n" +
                    "    \"%[]\"      : [ [\"hello\", \"world\"], \"%i\", \"%e\"],\n" +
                    "    \"%each\" : {\"%s\": {\"%+\": [\"%s\", \"%e\"]}},\n" +
                    "    \"%#\"       : \"%s\" } }",
    };


    static String [] FOR_EACH_Results = {
            "helloworld"
    };



    static String [] WHILE_DO = {
            "{\"%!while\": {\n" +
                    "    \"%$\"    : {   \"%!s\": \"\" },\n" +
                    "    \"%<>\"   : {\"<\":[ {\"%#\": \"%s\"}, 20]},\n" +
                    "    \"%do\"   : {\"%s\":{\"%+\": [\"%s\", \" next\"]}},\n" +
                    "    \"%#\"    : \"%s\" } }  ",
    };


    static String [] WHILE_DO_Results = {
            " next next next next"
    };

    @Before
    public void setup() throws Exception{

    }

    @After
    public void tear() {}

    @Test
    public void test() throws Exception{
        Object target = null;

        Map<String,Object> map = Process.variables(JsonProxy.readObject(Variables[0]), null);
        System.out.println("Before variables : " + map);
        for(int i = 0; i < Steps.length; i ++ ) {
            System.out.println("Step : " + i + " " + Steps[i]);
            System.out.println();
            Object obj = Template2.toJSON(Steps[i]);
            Process.steps(obj, map);
        }
        Assert.assertEquals("hello", map.get("v3"));
        System.out.println("After variables : " + map);


        for(String expr: EXPRS) {
            map.clear();
            Process.expr(JsonProxy.readObject(expr), map);
            System.out.println("Expr : " + expr);
            System.out.println("Expr : " + map.get(Process.Map_Expr));
            System.out.println();
        }

        for(int i = 0; i < IF_THEN_ELSE.length; i ++ ) {
            map.clear();
            Process.if_then_else(JsonProxy.readObject(IF_THEN_ELSE[i]).get("%!?"), map);

            System.out.println("If-Then-Else : " + map.get(Process.Map_Ret));
            Assert.assertEquals(IF_THEN_ELSE_Results[i], map.get(Process.Map_Ret));
        }

        for(int i = 0; i < FOR_EACH.length; i ++ ) {
            map.clear();
            Process.for_each(JsonProxy.readObject(FOR_EACH[i]).get("%!for"), map);
            System.out.println("For-Each : " + map.get(Process.Map_Ret));
            Assert.assertEquals(FOR_EACH_Results[i], map.get(Process.Map_Ret));
        }

        for(int i = 0; i < WHILE_DO.length; i ++ ) {
            map.clear();
            Process.while_do(JsonProxy.readObject(WHILE_DO[i]).get("%!while"), map);
            System.out.println("While-Do : " + map.get(Process.Map_Ret));
            Assert.assertEquals(WHILE_DO_Results[i], map.get(Process.Map_Ret));
        }

    }
}