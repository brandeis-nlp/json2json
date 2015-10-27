package org.lappsgrid.json2json;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

//import com.sun.xml.internal.txw2.output.IndentingXMLStreamWriter;

/**
 * Created by lapps on 4/16/2015.
 */
public class Json2Json {
    public static String json2jsondsl(String sourceJson, String templateDsl) throws Exception {
        return DslJsonJson.transform(sourceJson, templateDsl);
    }


    public static String json2xml(String json) throws Exception {
        return DslXmlJson.json2xml(json);
    }

    public static String xml2json(String xml) throws  Exception {
        return DslXmlJson.xml2json(xml);
    }

    public static String xml2xmlxsl(String sourceXml, String templateXsl) throws Exception {
        StreamSource stylesource = new StreamSource(new StringReader(templateXsl.trim()));
        Transformer transformer = TransformerFactory.newInstance().newTransformer(stylesource);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(new StreamSource(new StringReader(sourceXml.trim())), result);
        return  writer.toString();
    }
}
