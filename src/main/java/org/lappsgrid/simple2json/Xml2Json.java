package org.lappsgrid.simple2json;

//import com.sun.xml.internal.txw2.output.IndentingXMLStreamWriter;
import org.lappsgrid.json2json.jsonobject.JsonProxy;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by lapps on 4/16/2015.
 */
public class Xml2Json{
    static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    public static String xml2json(String xml) throws  Exception {
        DocumentBuilder dBuilder = dbf.newDocumentBuilder();
        Document doc = dBuilder.parse(new InputSource(new StringReader(xml)));
        doc.getDocumentElement().normalize();
        JsonProxy.JsonObject json = node2json(doc, JsonProxy.newObject());
        return json.toString();
    }



    public static String json2xml(String json) throws Exception {
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        StringWriter sw = new StringWriter();
        XMLStreamWriter xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(sw);
//        XMLStreamWriter xmlStreamWriter = new IndentingXMLStreamWriter(xmlOutputFactory.createXMLStreamWriter(sw));
        xmlStreamWriter.writeStartDocument();
        // support #text and #tail
        json = json.replaceAll("#text", "__text__").replaceAll("#tail", "__tail__");
        json2node(JsonProxy.newObject().read(json), xmlStreamWriter);
        xmlStreamWriter.writeEndDocument();
        xmlStreamWriter.flush();
        xmlStreamWriter.close();
        return sw.toString();
    }



//    public static void main(String[] args) throws Exception {
//        DocumentBuilder dBuilder = dbf.newDocumentBuilder();
//        Document doc = dBuilder.parse(new File("in.xml"));
//        doc.getDocumentElement().normalize();
//        printNode(doc);
//        JsonProxy.JsonObject json = node2json(doc, JsonProxy.newObject());
//        System.out.println(json);
//        System.out.println(json2xml(json.toString()));
//    }

    public static void json2node(JsonProxy.JsonObject jsonObj, XMLStreamWriter xmlStreamWriter) throws Exception {
        for (String key : jsonObj.keys()){
            Object obj = jsonObj.get(key);
            if(key.startsWith("@") && obj instanceof String) {
                xmlStreamWriter.writeAttribute(key.substring(1), (String) obj);
            } else if(key.matches("__.*__")) {
                if(key.equals("__text__") && obj instanceof String) {
                    xmlStreamWriter.writeCharacters((String)obj);
                }
            } else {
                if(obj instanceof String) {
                    xmlStreamWriter.writeStartElement(key);
                    xmlStreamWriter.writeCharacters((String)obj);
                    xmlStreamWriter.writeEndElement();
                } if (obj instanceof JsonProxy.JsonObject) {
                    JsonProxy.JsonObject child = (JsonProxy.JsonObject) obj;
                    if(key.equals("#comment")) {
                        xmlStreamWriter.writeComment((String)child.get("__text__"));
                    } else {
                        xmlStreamWriter.writeStartElement(key);
                        json2node(child, xmlStreamWriter);
                        xmlStreamWriter.writeEndElement();
                        if (child.get("__tail__") != null) {
                            xmlStreamWriter.writeCharacters((String) child.get("__tail__"));
                        }
                    }
                } else if (obj instanceof JsonProxy.JsonArray) {
                    JsonProxy.JsonArray arr = (JsonProxy.JsonArray)obj;
                    for (int i = 0; i < arr.length(); i++) {
                        if(key.equals("#comment")) {
                            JsonProxy.JsonObject child = (JsonProxy.JsonObject) arr.get(i);
                            xmlStreamWriter.writeComment((String)child.get("__text__"));
                        } else {
                            xmlStreamWriter.writeStartElement(key);
                            JsonProxy.JsonObject child = (JsonProxy.JsonObject) arr.get(i);
                            json2node(child, xmlStreamWriter);
                            xmlStreamWriter.writeEndElement();
                            if(child.get("__tail__") != null) {
                                xmlStreamWriter.writeCharacters((String)child.get("__tail__") );
                            }
                        }
                    }
                }
            }
        }
    }

    public static JsonProxy.JsonObject node2json(Node node, JsonProxy.JsonObject jsonObj) {
        if(node.getNodeType() == Node.ELEMENT_NODE
                || node.getNodeType() == Node.DOCUMENT_NODE
                || node.getNodeType() == Node.COMMENT_NODE) {

            // node attributes
            NamedNodeMap attrs = node.getAttributes();
            if(attrs != null) {
                for (int k = 0; k < attrs.getLength(); k++) {
                    Node arr = attrs.item(k);
                    jsonObj.put("@"+arr.getNodeName(), arr.getNodeValue());
                }
            }

            // node value
            String val = node.getNodeValue();
            if( val != null) {
                val = val.trim();
                if(val.length() > 0) {
                    jsonObj.put("__text__", val);
                }
            }

            NodeList list = node.getChildNodes();
            int i = 0;
            if (list != null && list.getLength() > 0) {
                if(list.item(i).getNodeType()  == Node.TEXT_NODE) {
                    String txt = list.item(i).getNodeValue().trim();
                    if (txt.length() > 0) {
                        jsonObj.put("__text__", txt);
                    }
                    i ++;
                }
            }
            for (; i < list.getLength(); i++) {
                Node child = list.item(i);
                String childName = child.getNodeName();
                String tail = "";
                if(child.getNextSibling() != null &&
                        child.getNextSibling().getNodeType() == Node.TEXT_NODE) {
                    tail = child.getNextSibling().getNodeValue().trim();
                    i ++;
                }
                if (jsonObj.get(childName) == null) {
                    JsonProxy.JsonObject childObj = JsonProxy.newObject();
                    if(tail.length() > 0)
                        childObj.put("__tail__", tail);
                    node2json(child, childObj);
                    jsonObj.put(child.getNodeName(),childObj);
                } else {
                    JsonProxy.JsonArray arrChildObjs = null;
                    if (jsonObj.get(childName) instanceof JsonProxy.JsonObject) {
                        arrChildObjs = JsonProxy.newArray();
                        arrChildObjs.add(jsonObj.get(childName));
                    } else {
                        arrChildObjs = (JsonProxy.JsonArray) jsonObj.get(childName);
                    }
                    JsonProxy.JsonObject childObj = JsonProxy.newObject();
                    if(tail.length() > 0)
                        childObj.put("__tail__", tail);
                    node2json(child, childObj);
                    System.out.println("arrChildObjs:"+childName);
                    arrChildObjs.add(childObj);
                    jsonObj.put(child.getNodeName(),arrChildObjs);
                }
            }

        } else {
            System.out.println("Node: type=" + node.getNodeType() +" (" + node +")");
            throw new RuntimeException("Unexpected Node Type.");
        }
        return jsonObj;
    }


    public static String xml2jsonml (String json) {

        return null;
    }

    public static void printNode(Node cur) {
        System.out.println("--------------------------------");
        System.out.println("Child:" + cur.getChildNodes().getLength());
//        System.out.println("NamespaceURI:" + cur.getNamespaceURI());
//        System.out.println("LocalName:" + cur.getLocalName());
        System.out.println("NodeValue:" + cur.getNodeValue());
        System.out.println("NodeType:" + cur.getNodeType());
        System.out.println("NodeName:" + cur.getNodeName());
        System.out.println("Attributes:" + cur.getAttributes());
        System.out.println("NextSibling:" + cur.getNextSibling());
        System.out.println("PreviousSibling:" + cur.getPreviousSibling());;
        System.out.println("================================" +
                "");

        NodeList list = cur.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node child = list.item(i);
            printNode(child);
        }

    }

}
