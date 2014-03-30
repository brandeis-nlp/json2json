package edu.brandeis.cs.json2json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nebhale.jsonpath.JsonPath;

import java.io.IOException;
import java.util.List;

/**
 * Created by shi on 3/30/14.
 */
public class Json2JsonNebhale implements IJson2Json{
    @Override
    public String path(String json, String path) throws Json2JsonException {
        throw new UnsupportedOperationException();
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode node = null;
//        try {
//            node = mapper.readTree(json);
//        } catch (IOException e) {
//            //e.printStackTrace();
//            throw new Json2JsonException(e);
//        }
////        StringBuffer sb = new StringBuffer();
//        return JsonPath.read(path, node, String.class);
//        for(Object obj:JsonPath.read(path, json, String.class)) {
//            sb.append(obj.toString()).append("\n");
//        }
//        return sb.substring(0, sb.length() - 1);
    }

    @Override
    public String transform(String json) throws Json2JsonException {
        throw new UnsupportedOperationException();
    }
}
