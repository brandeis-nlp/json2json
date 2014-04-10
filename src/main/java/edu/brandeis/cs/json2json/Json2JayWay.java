package edu.brandeis.cs.json2json;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.internal.JsonReader;

import java.util.Map;

/**
 * Created by shi on 3/30/14.
 */
public class Json2JayWay implements IPath {
    public String path(String json, String path) throws Json2JsonException {
        return JsonPath.read(json, path).toString();
    }

    // cache path.
    public String path(String json, String path, Map<String, Object> cache)throws Json2JsonException {
        Object val = cache.get(json);
        if (val == null) {
            val = new JsonReader().parse(json);
            cache.put(json, val);
        }
        ReadContext reader = (JsonReader)val;

        val = cache.get(path);
        if (val == null) {
            val = JsonPath.compile(path);
            cache.put(path, val);
        }
        JsonPath jsonpath = (JsonPath)val;
        return reader.read(jsonpath).toString();
    }
}
