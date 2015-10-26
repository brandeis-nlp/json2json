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
package org.lappsgrid.json2json.jsonobject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.lappsgrid.json2json.Json2JsonException;

import java.util.Collection;

/**
 * <p>This define Proxy class of Json object ( e.g. {key_1:value_1, key_2:value_2, ..., key_m:value_m} )
 * and Json Array (e.g. [obj_1, obj_2, ..., obj_n])</p>
 */
public class JsonProxy {

    public static String obj2json(Object obj) throws Json2JsonException{
        ObjectWriter ow = new ObjectMapper().writer();
        String json = null;
        try {
            json = ow.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new Json2JsonException(e);
        }
        return json;
    }

    /**
     * Json util read string as Json object
     * @param json
     * @return
     */
    public static Object str2json(String json) {
        if (json == null)
            return null;
        json = json.trim();
        if (json.startsWith("{")) {
            return JsonProxy.readObject(json);
        } else if (json.startsWith("[")) {
            return JsonProxy.readArray(json);
        } else {
            return json;
        }
    }

    public static interface NewProxy {
        public IJsonArr newArray();
        public IJsonObj newObject();
    }
    /** static methods for read / is / convert **/

    public static IJsonArr convertArray(String [] arr) {
        return newArray().convert(arr);
    }
    public static IJsonArr convertArray(Collection<String> arr) {
        return newArray().convert(arr);
    }

    public static IJsonArr readArray(String s) {
        return newArray().read(s);
    }

    public static IJsonObj readObject(String s) {
        return newObject().read(s);
    }

    public static boolean isArray (Object obj) {
        if (obj instanceof IJsonArr) {
            return true;
        }
        return false;
    }
    public static boolean isObject (Object obj) {
        if (obj instanceof IJsonObj) {
            return true;
        }
        return false;
    }

    /** fill in the proxy **/


    public static IJsonArr newArray(){
        return newProxy().newArray();
    }
    public static IJsonObj newObject(){
        return newProxy().newObject();
    }


//    public static NewProxy newProxy() {
//        // FIXME: exchange other proxy
//        return new MinimalJsonProxy();
//    }

    public static NewProxy newProxy() {
        // FIXME: exchange other proxy
        return new JacksonJsonProxy();
    }






}
