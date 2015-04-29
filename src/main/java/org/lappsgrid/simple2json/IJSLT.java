package org.lappsgrid.simple2json;

import org.lappsgrid.json2json.jsonobject.JsonProxy;
import org.lappsgrid.json2json.template.TemplateUnit;

/**
 * Created by lapps on 4/19/2015.
 * @deprecated
 */
public interface IJSLT {


    public static final String MarkTemplate = "%";
    public static final String MarkElement = "&";

    public enum JSLTType {
        type_template,
        type_value_of,
        type_for_each,
        type_if,
        type_choose,
        elem_match,
        elem_select,
        elem_test,
        elem_when,
        elem_otherwise
    }

    // [ Category, Mark, KeyWord ]
    public static final String [][] Namings = new String [][]{
            new String []{JSLTType.type_template.name(), MarkTemplate, "template"},
            new String []{JSLTType.type_value_of.name(), MarkTemplate, "value-of"},
            new String []{JSLTType.type_for_each.name(), MarkTemplate, "for-each"},
            new String []{JSLTType.type_if.name(), MarkTemplate, "if"},
            new String []{JSLTType.type_choose.name(), MarkTemplate, "choose"},
            new String []{JSLTType.elem_match.name(), MarkElement, "match"},
            new String []{JSLTType.elem_select.name(), MarkElement, "select"},
            new String []{JSLTType.elem_test.name(), MarkElement, "test"},
            new String []{JSLTType.elem_when.name(), MarkElement, "when"},
            new String []{JSLTType.elem_otherwise.name(), MarkElement, "otherwise"}
    };


    public Object transform() throws  JSLTException;

    /**
     *   {"%template": [
     *                  {"&match": "JsonPath" },
     *                  {}
     *                 ]}
     *   <a href="https://github.com/jayway/JsonPath">JsonPath</a>
     *   <a href="http://www.w3schools.com/xsl/xsl_templates.asp">Template</a>
     *
     */

    public Object template(Object json)  throws JSLTException ;


/**
 *
 *  {"%value-of": [
 *                  {"&select": "JsonPath"},
 *                  {}
 *                  ]}
 *
 *   <a href="http://www.w3schools.com/xsl/xsl_value_of.asp">Value-Of</a>
 */

public Object valueOf (Object json)  throws JSLTException ;

/**
 *
 *  {"%for-each": [
 *                  {"&select": "JsonPath"},
 *                  {}
 *                  ]}
 *
 *   <a href="http://www.w3schools.com/xsl/xsl_for_each.asp">For-Each</a>
 */

public Object forEach (Object json)  throws JSLTException ;


/**
 *
 *  {"%if": [
 *                  {"&test": ""},
 *                  {}
 *                  ]}
 *
 *   <a href="http://www.w3schools.com/xsl/xsl_if.asp">If</a>
 */

public Object if_ (Object json)  throws JSLTException ;


/**
 *
 *  {"%choose": [
 *                  {"&test": ""},
 *                  {"&when": },
 *                  {"&otherwise": }
 *                  ]}
 *
 *   <a href="http://www.w3schools.com/xsl/xsl_choose.asp">Choose</a>
 */

public Object choose (Object json)  throws JSLTException ;


}
