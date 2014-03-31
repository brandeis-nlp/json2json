package edu.brandeis.cs.json2json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UnknownFormatFlagsException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shi on 3/31/14.
 */
public class JsonTemplate implements  IJsonTemplate{
    public static final String JsonPathRegex = "\\$\\.[^\\s\"\']+";
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

    @Override
    public String transform(String template, String... jsons) throws Json2JsonException {
        Map<String, Object> map = JsonTemplate.findJsonPathContent(template, jsons);
        String target = template;
        for(String path: map.keySet()) {
            String content = map.get(path).toString();
            target =  StringProxy.replace(target, path, content);
        }
        return  target;
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
            contents.put(path, Json2Json.path(jsons[tp.index], tp.path));
        }
        return contents;
    }

}
