package org.lappsgrid.simple2json;

import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.json.JSONML;
import org.junit.Test;
import org.lappsgrid.json2json.jsonobject.JsonProxy;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Created by lapps on 4/18/2015.
 */
public class TestXml2Json {
    public static String readResource(String filename) throws Exception {
        File objFile = new File(TestXml2Json.class.getResource("/" + filename).toURI());
        return FileUtils.readFileToString(objFile);
    }
    Xml2Json xml2json = new Xml2Json();

    @Test
    public void testJson2Xml() throws Exception{
        System.out.println(xml2json.json2xml("{\"good\":\"<OK>\"}"));
        System.out.println(xml2json.json2xml("{\"good\":{\"x\":{\"#tail\":\"World\",\"#text\":\"OK\"}}}"));
    }

    @Test
    public void testXml2Json() throws Exception{
        System.out.println(xml2json.xml2json("<good attr=\"y\"><x>OK</x><x>OK1</x><x>OK2</x>World</good>"));

        String xml = readResource("GATEANNIE.xml");
        String target = readResource("GATEANNIE.json");
        String result = xml2json.xml2json(xml);
        Assert.assertEquals(target.replaceAll("\\s", ""), result.replaceAll("\\s", ""));
    }

    @Test
    public void testXml2JsonML() throws Exception{
        System.out.println(JSONML.toJSONArray("<good  attr=\"y\"><x>OK</x>World<x>OK</x>World</good>"));
    }

    @Test
    public void testPrint() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbf.newDocumentBuilder();
        File xmlFile = FileUtils.toFile(this.getClass().getResource("/in.xml"));
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();
//        xml2json.printNode(doc);
        Object json = xml2json.node2json(doc, JsonProxy.newObject());
        System.out.println(json);
        System.out.println(xml2json.json2xml(json.toString()));
    }
}
