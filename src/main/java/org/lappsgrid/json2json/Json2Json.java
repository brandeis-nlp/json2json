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

import org.lappsgrid.json2json.jsonobject.JsonProxy;
import org.lappsgrid.json2json.jsonpath.JsonPath;
import org.lappsgrid.json2json.template.JsonTemplate;

/**
 * Created by lapps on 6/16/2014.
 */
public class Json2Json extends JsonPath {

    public static interface Template {
        public String transform(String template, String... jsons) throws Json2JsonException;
        public Object transform(JsonProxy.JsonObject template, JsonProxy.JsonObject ...jsons) throws Json2JsonException;
    }


    public static String transform(String template, String... jsons) throws Json2JsonException {
        return newTemplate().transform(template, jsons);
    }

    public static Object transform(JsonProxy.JsonObject template, JsonProxy.JsonObject ...jsons)
            throws Json2JsonException{
        return newTemplate().transform(template, jsons);
    }

    public static Template newTemplate() {
        return new JsonTemplate();
    }


//    public static Template newTemplate() {
//                   FIXME:  exchange other Template
//        return new edu.cs.brandeis.edu.json2json.Template();
//    }
}
