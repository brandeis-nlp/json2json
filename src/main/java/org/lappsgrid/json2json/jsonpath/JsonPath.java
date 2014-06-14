package org.lappsgrid.json2json.jsonpath;

import org.lappsgrid.json2json.Json2JsonException;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by lapps on 6/13/2014.
 */
public class JsonPath {
    static int InitialSize = 8;

    public static interface IPath {
        public String path(String json, String path) throws Json2JsonException;
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
        //FIXME: change into other JsonPath Implementation
        return new JayWayJsonPath();
    }
}
