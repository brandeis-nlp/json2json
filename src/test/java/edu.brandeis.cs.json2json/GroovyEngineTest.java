package edu.brandeis.cs.json2json;


import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GroovyEngineTest {

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
            "&$.@context.homepage.*[0]",
            "&$.homepage"
    };



    @Before
    public void setup() throws Exception{

    }

    @After
    public void tear() {}

    @Test
    public void test(){
        System.out.println();
        String filter = "%+(\"hello\",\"world\") ";
        String t = GroovyEngine.runFilter(filter);
        Assert.assertEquals("helloworld",t);



        filter = "%|(\"http://manu.sporny.org/\",\".\")";
        t = GroovyEngine.runFilter(filter);
        Assert.assertEquals("[\"http://manu\",\"sporny\",\"org/\"]",t);

        filter = "%|(\"hello.world\", \".\") ";
        t = GroovyEngine.runFilter(filter);
        Assert.assertEquals("[\"hello\",\"world\"]",t);

        filter = "%*(\"[\\\"hello\\\",\\\"world\\\"]\", \".\")";
        t = GroovyEngine.runFilter(filter);
        Assert.assertEquals("hello.world",t);

        filter = "%%(\"hello.world\",\"[a-z]+\")";
        t = GroovyEngine.runFilter(filter);
        Assert.assertEquals("[\"hello\",\"world\"]",t);

        filter = "%%(%*(%|(\"hello.world\", \".\"), \"x\"),\"[a-z]+\")";
        t = GroovyEngine.runFilter(filter);
        Assert.assertEquals("[\"helloxworld\"]",t);



        String iterate = "%|(%-([\"hello\", \".world\"]){ %r += %e;},\".\")";
        t = GroovyEngine.runFilter(iterate);
        Assert.assertEquals("[\"hello\",\"world\"]",t);

    }
    
}