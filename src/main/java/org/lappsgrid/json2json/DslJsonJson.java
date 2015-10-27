package org.lappsgrid.json2json;

import groovy.json.JsonBuilder;
import groovy.json.JsonSlurper;
import groovy.json.StringEscapeUtils;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Created by 310201833 on 2015/10/26.
 */
public class DslJsonJson {


    private static Logger LOG = Logger.getLogger(DslJsonJson.class.getName());

    public static final String REF_JSON_SOURCE = "__source_json__";
    public static final String REF_JSON_BUILDER = "__json_builder__";

    public static final String KEYWORD_GLOBAL = "&:";
    public static final String KEYWORD_GLOBAL_MATCH = REF_JSON_SOURCE + ".";
    public static final String KEYWORD_LOCAL = "&.";
    public static final String KEYWORD_LOCAL_MATCH = "it.";
    public static final String KEYWORD_FOREACH  = "foreach";
    public static final String KEYWORD_FOREACH_MATCH  = "collect";
    public static final String KEYWORD_SELECT = "select";
    public static final String KEYWORD_SELECT_MATCH = "findAll";


    public static String transform(String sourceJson, String templateDsl){
        Binding binding = new Binding();
        GroovyShell shell = new GroovyShell(binding);
        JsonSlurper js = new JsonSlurper();
        JsonBuilder jb = new JsonBuilder();
        Object json = js.parseText(sourceJson);
        binding.setVariable(REF_JSON_SOURCE, json);
        binding.setVariable(REF_JSON_BUILDER, jb);
        String script = String.format("%s.call(\n %s \n)", REF_JSON_BUILDER, filterJson(templateDsl));
        LOG.info(String.format("JsonBuilder Script: \n %s \n", script));
        shell.evaluate(script);
        return jb.toPrettyString() ;
    }

    protected static String replacingWithSpace (String s, String keyword, String match) {
        String pattern  = String.format("\\.%s\\s*\\{", keyword);
        String replacement =  String.format("\\.%s\\{", match);
        return s.replaceAll(pattern,replacement);
    }

    protected static String replacing(String s, String keyword,String match) {
        return s.replaceAll(Pattern.quote(keyword), match);
    }

    private static String filterJson(String dsl) {
        dsl = dsl.trim();
        if(!dsl.startsWith("{")) {
            dsl = String.format("{ %s }", dsl);
        }
        // enable keywords foreach and select
        // replace .foreach { by .collect{
        dsl = replacingWithSpace(dsl, KEYWORD_FOREACH, KEYWORD_FOREACH_MATCH);
        // replace .select { by .findAll {
        dsl = replacingWithSpace(dsl, KEYWORD_SELECT, KEYWORD_SELECT_MATCH);

        // replace global json
        dsl = replacing(dsl, KEYWORD_GLOBAL, KEYWORD_GLOBAL_MATCH);
        // replace local json
        dsl = replacing(dsl, KEYWORD_LOCAL, KEYWORD_LOCAL_MATCH);
        return dsl;
    }
}
