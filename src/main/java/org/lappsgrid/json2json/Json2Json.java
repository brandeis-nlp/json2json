/**********************************************************************************************************************
 Copyright [2014] [Chunqi SHI (chunqi.shi@hotmail.com)]

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 **********************************************************************************************************************/
package org.lappsgrid.json2json;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class Json2Json {
    static int InitialSize = 8;
    static final LinkedBlockingQueue<IPath> cacheJsonPath = new LinkedBlockingQueue<IPath>(InitialSize);

    static final LinkedBlockingQueue<ITemplate> cacheJsonTemplate = new LinkedBlockingQueue<ITemplate>(InitialSize);
    static {
        for (int i = 0; i < InitialSize; i ++)
            try {
                cacheJsonPath.put(new Json2JayWay());
                cacheJsonTemplate.put(new Template2());
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