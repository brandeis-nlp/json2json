package edu.brandeis.cs.json2json;

import com.jayway.jsonpath.JsonPath;

/**
 * Created by shi on 3/30/14.
 */
public class Json2JsonJayWay implements IJson2Json {
    @Override
    public String transform(String template, String... json) throws Json2JsonException {


        return null;
    }

    @Override
    public String path(String json, String path) throws Json2JsonException {
        return JsonPath.read(json, path).toString();
    }
}
