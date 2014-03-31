package edu.brandeis.cs.json2json;

import java.util.concurrent.LinkedBlockingQueue;

public class Json2Json {
    static int InitialSize = 8;
    static final LinkedBlockingQueue<IJson2Json> cache = new LinkedBlockingQueue<IJson2Json>(InitialSize);
    static {
        for (int i = 0; i < InitialSize; i ++)
            try {
                cache.put(new Json2JsonJayWay());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
    public static String path(String json, String path) throws Json2JsonException {
        String ret = null;
        try {
            IJson2Json json2json = cache.take();
            ret = json2json.path(json, path);
            cache.put(json2json);
        } catch (InterruptedException e) {
            throw new Json2JsonException(e);
        }
        return ret;
    }

    public static String transform (String json, String template) throws Json2JsonException {
        String ret = null;
        try {
            IJson2Json json2json = cache.take();
            ret = json2json.transform(json, template);
            cache.put(json2json);
        } catch (InterruptedException e) {
            throw new Json2JsonException(e);
        }
        return ret;
    }
}