package edu.brandeis.cs.json2json;

import java.util.concurrent.LinkedBlockingQueue;

public class Json2Json {
    static int InitialSize = 8;
    static final LinkedBlockingQueue<IPath> cacheJsonPath = new LinkedBlockingQueue<IPath>(InitialSize);

    static final LinkedBlockingQueue<ITemplate> cacheJsonTemplate = new LinkedBlockingQueue<ITemplate>(InitialSize);
    static {
        for (int i = 0; i < InitialSize; i ++)
            try {
                cacheJsonPath.put(new Json2JayWay());
                cacheJsonTemplate.put(new Template());
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

    public static String transform (String template, String ...jsons) throws Json2JsonException {
        String ret = null;
        try {
            ITemplate json2json = cacheJsonTemplate.take();
            ret = json2json.transform(template, jsons);
            cacheJsonTemplate.put(json2json);
        } catch (InterruptedException e) {
            throw new Json2JsonException(e);
        }
        return ret;
    }
}