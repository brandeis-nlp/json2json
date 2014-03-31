package edu.brandeis.cs.json2json;

import com.jayway.jsonpath.JsonPath;

/**
 * Created by shi on 3/30/14.
 */
public class Json2JsonJayWay implements IJsonPath{
    public String path(String json, String path) throws Json2JsonException {
        return JsonPath.read(json, path).toString();
    }
}
