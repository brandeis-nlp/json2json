package org.lappsgrid.json2json;

import groovy.json.JsonBuilder;
import groovy.json.JsonSlurper;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.util.XmlSlurper;
import groovy.util.slurpersupport.GPathResult;
import org.lappsgrid.json2json.jsonobject.JsonArray;
import org.lappsgrid.json2json.jsonobject.JsonObject;
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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

//import com.sun.xml.internal.txw2.output.IndentingXMLStreamWriter;

/**
 * Created by lapps on 4/16/2015.
 */
public class Json2Json {
    static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();


    private static int findMatchEnd(String s, int fromIdx, char left, char right) {
        return findMatch(s, fromIdx, left, right)[1];
    }

    private static int[] findMatch(String s, int fromIdx, char left, char right) {
        int begin = s.indexOf(left, fromIdx);
        if(begin < 0)
            return new int[]{-1, -1};
        int end = begin + 1;
        int leftCount = 1;
        while(end < s.length()){
            if(s.charAt(end) == left) {
                leftCount ++;
            } else if(s.charAt(end) == right){
                leftCount --;
            }
            if(leftCount == 0)
                return new int []{begin, end};
            end ++;
        }
        return new int[]{begin, -1};
    }

    private static Set<String> leaves (GPathResult xml) {
        return leaves((groovy.util.slurpersupport.Node)xml.getAt(0));
    }

    private static Set<String> leaves (groovy.util.slurpersupport.Node node) {
        Set<String> names = new HashSet<String>();
        for(Object child: node.children()) {
            if(child instanceof groovy.util.slurpersupport.Node) {
                names.addAll(leaves((groovy.util.slurpersupport.Node) child));
            } else {
                names.add(node.name());
            }
        }
        return names;
    }
//    private static int nextNonWhiteSpace(String s, int fromIdx) {
//        while(fromIdx < s.length() && Character.isWhitespace(s.charAt(fromIdx)))
//            fromIdx ++;
//        return fromIdx;
//    }

//     private static List<String> xmlEntities (String templateDsl) {
//        List<String> entities = new ArrayList<String>();
//        int start = templateDsl.indexOf("__source_xml__", 0);
//        int end = start + 2;
//        while( start >= 0) {
//            while(end < templateDsl.length()) {
//                if (templateDsl.charAt(end) == '{')
//                    end = findMatch(templateDsl, end, '{', '}')[1];
//                if (Character.isWhitespace(templateDsl.charAt(end))) {
//                    int next = nextNonWhiteSpace(templateDsl, end + 1);
//                    if(next == templateDsl.length() || templateDsl.charAt(next) != '{')
//                        break;
//                }
//                end ++;
//            }
//            entities.add(templateDsl.substring(start, end));
//            start = templateDsl.indexOf("__source_xml__",end);
//            end = start + 2;
//        }
//        return entities;
//    }



    public static String xml2jsondsl(String sourceXml, String templateDsl) throws Exception{
        Binding binding = new Binding();
        GroovyShell shell = new GroovyShell(binding);
        XmlSlurper xs = new XmlSlurper();
        GPathResult xml = xs.parseText(sourceXml);
        System.out.println(leaves(xml));
        binding.setVariable("__source_xml__", xml);
        templateDsl = filterXml(templateDsl, leaves(xml));
        binding.setVariable("__target_json__", null);
        JsonBuilder jb = new JsonBuilder();
        binding.setVariable("__json_builder__", jb);
        StringBuffer sb = new StringBuffer("__json_builder__.call(");
        sb.append(templateDsl);
        sb.append(") \n");
        sb.append("__target_json__ = __json_builder__.toString()");
//        System.out.println("Evaluate:\n" + sb.toString());
        shell.evaluate(sb.toString());
        return (String) binding.getVariable("__target_json__");
    }

    public static String json2jsondsl(String sourceJson, String templateDsl) throws Exception {
        Binding binding = new Binding();
        GroovyShell shell = new GroovyShell(binding);
        JsonSlurper js = new JsonSlurper();
        Object json = js.parseText(sourceJson);
        binding.setVariable("__source_json__", json);
        binding.setVariable("__target_json__", null);
        JsonBuilder jb = new JsonBuilder();
        binding.setVariable("__json_builder__", jb);
        StringBuffer sb = new StringBuffer("__json_builder__.call(");
        sb.append(filterJson(templateDsl));
        sb.append(") \n");
        sb.append("__target_json__ = __json_builder__.toString()");
//        System.out.println("Evaluate:\n" + sb.toString());
        shell.evaluate(sb.toString());
        return (String) binding.getVariable("__target_json__");
    }


    private static String[] OperatorIts = new String[]{
            "collect",
            "findAll",
            "find",
            "sort",
            "removeAll",
            "unique",
            "each"
    };

    private static String filterXml(String dsl, Collection<String> leaves) {
        dsl = dsl.trim();
        if(!dsl.startsWith("{")) {
            dsl = "{" + dsl + "}";
        }
        // replace global json
        dsl = dsl.replaceAll("\\.foreach\\s*\\{",".collect{");
        dsl = dsl.replaceAll("\\.select\\s*\\{",".findAll{");
        dsl = dsl.replaceAll("\\&\\$","__source_xml__.");
        dsl = dsl.replaceAll("%\\$","__source_xml__.");
        // replace local json
        dsl = dsl.replaceAll("\\&\\.","it.");
        dsl = dsl.replaceAll("\\&\\.","it.");
        dsl = dsl.replaceAll("%\\.","it.");

        for(String leaf : leaves) {
            dsl = dsl.replaceAll("it\\.[\"]?"+leaf+"[\"]?", "it.\""+leaf+"\".text()");
        }
        // replace Node functions
        dsl = dsl.replaceAll("#text",".text()");
        dsl = dsl.replaceAll("#name",".name()");
        dsl = dsl.replaceAll("#parent",".parent()");
        dsl = dsl.replaceAll("#children",".children()");
        dsl = dsl.replaceAll("#localText",".localText()");
        dsl = dsl.replaceAll("#localtext",".localText()");
        dsl = dsl.replaceAll("\\.text\\(\\)\\.text\\(\\)",".text()");
        return dsl;
    }

    private static String filterJson(String dsl) {
        dsl = dsl.trim();
        if(!dsl.startsWith("{")) {
            dsl = "{" + dsl + "}";
        }
        // replace global json
        dsl = dsl.replaceAll("\\.foreach\\s*\\{",".collect{");
        dsl = dsl.replaceAll("\\.select\\s*\\{",".findAll{");
        dsl = dsl.replaceAll("\\&\\$","__source_json__.");
        dsl = dsl.replaceAll("%\\$","__source_json__.");
        // replace local json
        dsl = dsl.replaceAll("\\&\\.","it.");
        dsl = dsl.replaceAll("%\\.","it.");
        return dsl;
    }

    public static String xml2json(String xml) throws  Exception {
        DocumentBuilder dBuilder = dbf.newDocumentBuilder();
        Document doc = dBuilder.parse(new InputSource(new StringReader(xml)));
        doc.getDocumentElement().normalize();
        JsonObject json = (JsonObject)node2json(doc, JsonProxy.newObject());
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
//        JsonObj json = node2json(doc, new JsonObj());
//        System.out.println(json);
//        System.out.println(json2xml(json.toString()));
//    }
    public static void json2node(JsonObject jsonObj, XMLStreamWriter xmlStreamWriter) throws Exception {
        for (String key : jsonObj.keys()){
            Object obj = jsonObj.get(key);
            if(key.equals("#comment")) {
                if(obj instanceof JsonObject) {
                    JsonObject child = (JsonObject) obj;
                    xmlStreamWriter.writeComment((String) child.get("__text__"));
                } else if(obj instanceof JsonArray) {
                    JsonArray arr = (JsonArray)obj;
                    for (int i = 0; i < arr.length(); i++) {
                        if (arr.get(i) instanceof JsonObject) {
                            JsonObject child = (JsonObject) arr.get(i);
                            xmlStreamWriter.writeComment((String) child.get("__text__"));
                        } else{
                            xmlStreamWriter.writeComment((String) arr.get(i));
                        }
                    }
                } else {
                    xmlStreamWriter.writeComment((String)obj);
                }
            } else if(key.startsWith("@") && obj instanceof String) {
                xmlStreamWriter.writeAttribute(key.substring(1), (String) obj);
            } else if(key.matches("__.*__")) {
                if(key.equals("__text__")) {
                    if(obj instanceof JsonArray){
                        JsonArray arr = (JsonArray)obj;
                        for(int t = 0; t < arr.length(); t++) {
                            xmlStreamWriter.writeCharacters((String)arr.get(t));
                        }
                    }else {
                        xmlStreamWriter.writeCharacters((String)obj);
                    }
                }
            } else {
                if (obj instanceof JsonObject) {
                    JsonObject child = (JsonObject) obj;
                    xmlStreamWriter.writeStartElement(key);
                    json2node(child, xmlStreamWriter);
                    xmlStreamWriter.writeEndElement();
                    if (child.get("__tail__") != null) {
                        xmlStreamWriter.writeCharacters((String) child.get("__tail__"));
                    }
                } else if (obj instanceof JsonArray) {
                    JsonArray arr = (JsonArray)obj;
                    for (int i = 0; i < arr.length(); i++) {
                        if(arr.get(i) instanceof JsonObject) {
                            JsonObject child = (JsonObject) arr.get(i);
                            xmlStreamWriter.writeStartElement(key);
                            json2node(child, xmlStreamWriter);
                            xmlStreamWriter.writeEndElement();
                            if (child.get("__tail__") != null) {
                                xmlStreamWriter.writeCharacters((String) child.get("__tail__"));
                            }
                        }else {
                            xmlStreamWriter.writeStartElement(key);
                            xmlStreamWriter.writeCharacters((String)arr.get(i));
                            xmlStreamWriter.writeEndElement();
                        }
                    }
                } else {
                    xmlStreamWriter.writeStartElement(key);
                    xmlStreamWriter.writeCharacters(obj.toString());
                    xmlStreamWriter.writeEndElement();
                }
            }
        }
    }

    public static JsonObject node2json(Node node, JsonObject jsonObj) {
        if(node.getNodeType() == Node.ELEMENT_NODE
                || node.getNodeType() == Node.DOCUMENT_NODE
                || node.getNodeType() == Node.COMMENT_NODE
                || node.getNodeType() == Node.TEXT_NODE
                || node.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE) {
            // node attributes
            NamedNodeMap attrs = node.getAttributes();
            if(attrs != null) {
                for (int k = 0; k < attrs.getLength(); k++) {
                    Node arr = attrs.item(k);
                    jsonObj.put("@" + arr.getNodeName(), arr.getNodeValue());
                }
            }
            NodeList list = node.getChildNodes();
            List<Node> commentNodes = new ArrayList<Node>();
            for(int i = 0; i < list.getLength(); i ++) {
                if(list.item(i).getNodeType() == Node.COMMENT_NODE) {
                    commentNodes.add(list.item(i));
                    if(jsonObj.get("#comment") == null) {
                        jsonObj.put("#comment", list.item(i).getNodeValue());
                    } else {
                        if(jsonObj.get("#comment") instanceof JsonArray) {
                            ((JsonArray) jsonObj.get("#comment")).add(list.item(i).getNodeValue());
                        } else {
                            JsonArray comms = JsonProxy.newArray();
                            comms.add(jsonObj.get("#comment"));
                            comms.add(list.item(i).getNodeValue());
                            jsonObj.put("#comment", comms);
                        }
                    }
                }else if(list.item(i).getNodeType() == Node.PROCESSING_INSTRUCTION_NODE) {
                    commentNodes.add(list.item(i));
                }
            }
            // remove all comment nodes
            for(Node commNode: commentNodes) {
                node.removeChild(commNode);
            }
            // reget all the child nodes.
            list = node.getChildNodes();
            int i = 0;
//            System.out.println("<"+node.getNodeName()+"> :" + list.getLength() +" " + node.getNodeValue());

            if(node.getNodeValue() != null) {
                String txt = node.getNodeValue().trim();
                jsonObj.put("__text__", txt);
            }
            if (list.getLength() > 0) {
                while(i < list.getLength() && list.item(i).getNodeType()  == Node.TEXT_NODE) {
                    String txt = list.item(i).getNodeValue().trim();
                    if (txt.length() > 0) {
                        if (txt.length() > 0) {
                            if(jsonObj.get("__text__") == null) {
                                jsonObj.put("__text__", txt);
                            } else {
                                if(jsonObj.get("__text__") instanceof JsonArray) {
                                    ((JsonArray) jsonObj.get("__text__")).add(txt);
                                } else {
                                    JsonArray comms = JsonProxy.newArray();
                                    comms.add(jsonObj.get("__text__"));
                                    comms.add(txt);
                                    jsonObj.put("__text__", comms);
                                }
                            }
                        }
                    }
                    i ++;
                }
            }
            for (; i < list.getLength(); i++){
                Node child = list.item(i);
                String childName = child.getNodeName();
//                String tail = "";
                List<String> tails = new ArrayList<String>();
                Node sibling = child.getNextSibling();
                while(sibling != null && sibling.getNodeType() == Node.TEXT_NODE) {
                    String tail = sibling.getNodeValue().trim();
                    if(tail.length() > 0) {
                        tails.add(tail);
                    }
                    i ++;
                    sibling = sibling.getNextSibling();
                }
                if (jsonObj.get(childName) == null) {
                    JsonObject childObj = JsonProxy.newObject();
                    if(tails.size() > 0) {
                        if(tails.size() == 1)
                            childObj.put("__tail__", tails.get(0));
                        else {
                            childObj.put("__tail__", JsonProxy.newArray().convert(tails));
                        }
                    }
                    node2json(child, childObj);
                    // simplify and replace "__text__" object.
                    if(childObj.length() == 1 && childObj.has("__text__")) {
                        jsonObj.put(child.getNodeName(), childObj.get("__text__"));
                    } else {
                        jsonObj.put(child.getNodeName(), childObj);
                    }
                } else {
                    JsonArray arrChildObjs = null;
                    if (jsonObj.get(childName) instanceof JsonArray) {
                        arrChildObjs = (JsonArray) jsonObj.get(childName);
                    } else {
                        arrChildObjs = JsonProxy.newArray();
                        arrChildObjs.add(jsonObj.get(childName));
                    }
                    JsonObject childObj = JsonProxy.newObject();
                    if(tails.size() > 0) {
                        if(tails.size() == 1)
                            childObj.put("__tail__", tails.get(0));
                        else {
                            childObj.put("__tail__", JsonProxy.newArray().convert(tails));
                        }
                    }
                    node2json(child, childObj);
                    // simplify and replace "__text__" object.
                    if(childObj.length() == 1 && childObj.has("__text__")) {
                        arrChildObjs.add(childObj.get("__text__"));
                    } else {
                        arrChildObjs.add(childObj);
                    }
                    jsonObj.put(child.getNodeName(), arrChildObjs);
                }
            }
        } else {
            System.out.println("Node: type=" + node.getNodeType() +" (" + node +")");
            throw new RuntimeException("Unexpected Node Type.");
        }
        return jsonObj;
    }



    public static String xml2xmlxsl(String sourceXml, String templateXsl) throws Exception {
        StreamSource stylesource = new StreamSource(new StringReader(templateXsl.trim()));
        Transformer transformer = TransformerFactory.newInstance().newTransformer(stylesource);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(new StreamSource(new StringReader(sourceXml.trim())), result);
        return  writer.toString();
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
