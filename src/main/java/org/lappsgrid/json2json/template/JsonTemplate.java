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
package org.lappsgrid.json2json.template;

import org.lappsgrid.json2json.Json2Json;
import org.lappsgrid.json2json.Json2JsonException;
import org.lappsgrid.json2json.jsonobject.JsonProxy;
import org.lappsgrid.json2json.template.JsonUnit;

/**
 * Created by shi on 6/16/14.
 */
public class JsonTemplate implements org.lappsgrid.json2json.Json2Json.Template {
    
    public String transform(String template, String... jsons) throws Json2JsonException {
        JsonUnit junit = new JsonUnit(JsonProxy.readObject(template));
        junit.setJsons(jsons);
        return junit.transform().toString();
    }
    
    public Object transform(JsonProxy.JsonObject template, JsonProxy.JsonObject... jsons) throws Json2JsonException {
        String [] sources = new String [jsons.length];
        for (int i = 0; i < jsons.length; i++) {
            sources[i] = jsons[i].toString();
        }
        JsonUnit junit = new JsonUnit(template);
        junit.setJsons(sources);
        return junit.transform();
    }
}
