package org.lappsgrid.json2json.jsonpath;

import com.eclipsesource.json.JsonObject;
import org.lappsgrid.json2json.Json2JsonException;
import org.lappsgrid.json2json.jsonobject.JsonProxy;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by lapps on 6/13/2014.
 */
public class JsonPath {
    static int InitialSize = 16;

    public static interface IPath {
        /**
         * Input a Json String, and Json Path String, return Json Object String.
         * @param json A Json object string.
         * @param path Json path string.
         * @return The selected Json objects string.
         * @throws Json2JsonException
         */
        public String path(String json, String path) throws Json2JsonException;

        /**
         * Same as {@code public String path(String json, String path) throws Json2JsonException;}, but
         * with JsonPath cache.
         * @param json
         * @param path
         * @param cache Map-interface-based cache.
         * @return
         * @throws Json2JsonException
         */
        public String path(String json, String path, Map<String, Object> cache) throws Json2JsonException;
    }

    static final LinkedBlockingQueue<IPath> cacheJsonPath = new LinkedBlockingQueue<IPath>(InitialSize);
    static {
        for (int i = 0; i < InitialSize; i ++)
            try {
                cacheJsonPath.put(newPath());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    public static Object path(JsonObject json, String path)  throws Json2JsonException {
        return JsonProxy.fromJsonString(path(json.toString(), path));
    }

    public static String path(String json, String path) throws Json2JsonException {
        String ret = null;
        try {
            IPath json2json = cacheJsonPath.take();
            ret = json2json.path(json, path);
            cacheJsonPath.put(json2json);
        } catch (InterruptedException e) {
            throw new Json2JsonException(e);
        }
        return ret;
    }

    public static String path(String json, String path, Map<String, Object> cache) throws Json2JsonException {
        String ret = null;
        try {
            IPath json2json = cacheJsonPath.take();
            ret = json2json.path(json, path, cache);
            cacheJsonPath.put(json2json);
        } catch (InterruptedException e) {
            throw new Json2JsonException(e);
        }
        return ret;
    }


    public static IPath newPath() {
        // FIXME: change into other JsonPath Implementation
        return new JayWayJsonPath();
    }
}
