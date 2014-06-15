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
package org.lappsgrid.json2json.engine;

import org.lappsgrid.json2json.jsonobject.JsonProxy.JsonObject;
import org.lappsgrid.json2json.template.TemplateUnit;

/**
 * <p> Template Engine is executor of TemplateUnit. </p>
 */
public class TemplateEngine {

    public static interface Engine {
        public JsonObject transform (TemplateUnit templateUnit, Object proxy);
    }
}
