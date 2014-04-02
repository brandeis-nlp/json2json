package edu.brandeis.cs.json2json;

import org.json.JSONObject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shi on 3/31/14.
 */
public class Template implements ITemplate {
    public static final String JsonFilterRegex = "%[\\+\\|\\*\\?_%/-]\\(.+";
    public static final String JsonPathRegex = "\\$\\.[^\\s\"\'\\\\,]+";
    public static final String JsonPathTemplateRegex = "&\\d*" + JsonPathRegex;
    public static final Pattern TemplatePathPattern = Pattern.compile(JsonPathTemplateRegex);

    public static String [] findJsonPath(String template){
        Matcher m = TemplatePathPattern.matcher(template);
        ArrayList<String> paths = new ArrayList<String>(16);
        while(m.find()) {
            paths.add(m.group());
        }
        return paths.toArray(new String[paths.size()]);
    }

    public static String transform_path(String template, String... jsons) throws Json2JsonException {
        Map<String, Object> map = Template.findJsonPathContent(template, jsons);
        String target = template;
        for(String path: map.keySet()) {
            String content = map.get(path).toString();
            content = JSONObject.valueToString(content);
            target =  StringProxy.replace(target, path, content);
        }
        return  target;
    }

    public static  HashMap<String, String> iterateJson (JSONObject jObject, HashMap<String, String> map) {
        Iterator<?> keys = jObject.keys();
        while( keys.hasNext() ){
            String key = (String)keys.next();
            Object val = jObject.get(key);
            if(  val instanceof JSONObject ){
                iterateJson((JSONObject)val,  map);
            } else if (val instanceof  String) {
                String s = ((String) val).trim();
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                System.out.println("s = " + s);
                System.out.println("JsonPathTemplateRegex: " + StringProxy.regex_contains(s, JsonPathTemplateRegex));
                System.out.println("JsonFilterRegex: " + StringProxy.regex_contains(s, JsonFilterRegex));
                if(StringProxy.regex_contains(s, JsonPathTemplateRegex) || StringProxy.regex_contains(s, JsonFilterRegex)) {
                    map.put((String)val, s.substring(0,2));
                }
            }
        }
        return map;
    }


    public  static HashMap<String,String> iterateJson (String json) {
        System.out.println("json: " + json);
        JSONObject jObject = new JSONObject(json.trim());
        return iterateJson(jObject, new HashMap<String,String>());
    }


    @Override
    public String transform(String template, String... jsons) throws Json2JsonException {
        String target = template;
        HashMap<String, String> map = iterateJson(target);
        System.out.println("<--------------------------");
        System.out.println("map:" + map);
        for(String key: map.keySet()){
            String val = transform_path(key, jsons);
            System.out.println("key:[" + key + "]");
            System.out.println("val:[" + val + "]");
            val = GroovyEngine.runFilter(val);
            System.out.println("filter:" + val);
            if (!val.startsWith("[") && !val.startsWith("{")) {
                val = JSONObject.quote(val);
            }
            target = StringProxy.replace(target, JSONObject.quote(key), val);
        }
        System.out.println("target:" + target);
        System.out.println("-------------------------->");
        return target;
    }

    public static class TemplatePath {
        public String path = null;
        public int index = 0;
        public TemplatePath(String templatePath) {
            if (templatePath == null || !templatePath.startsWith("&")) {
                throw new UnknownFormatFlagsException(templatePath);
            }
            int start = templatePath.indexOf("$.");
            if (start < 0) {
                throw new UnknownFormatFlagsException(templatePath);
            }
            if (start > 1) {
                index = Integer.parseInt(templatePath.substring(1, start));
            }
            path = templatePath.substring(start);
        }
    }


    public static Map<String,Object> findJsonPathContent(String template, String ...jsons) throws Json2JsonException {
        Map<String, Object> contents = new HashMap<String, Object>(16);
        for(String path:findJsonPath(template)) {
            TemplatePath tp = new TemplatePath(path);
            contents.put(path, Json2Json.path(jsons[tp.index], tp.path).trim());
        }
        return contents;
    }

}
