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

import org.lappsgrid.json2json.Json2JsonException;
import org.lappsgrid.json2json.jsonobject.JsonProxy.JsonObject;

import java.util.Collection;

/**
 * <p> TemplateUnit is the class to recognize Commands and Process </p>
 */
public abstract class TemplateUnit {
    JsonObject obj = null;

    public static interface Transform {
        Object transform (TemplateUnit obj) throws Json2JsonException;
    }

    public TemplateUnit (JsonObject obj) {
        this.obj = obj;
    }

    public boolean isTemplate() {
        if (obj != null) {
            Collection<String> keys = obj.keys();

            /** Only 1 key is allowed in the TemplateUnit, and the key must be a symbol or keyword 　**/
            if (keys.size() == 1) {
                String type = unitType ();
                // if find it in the symbols or keywords
                if (TemplateNaming.hasSymbol(type) ||
                        TemplateNaming.hasKeyword(type)) {
                    return true;
                }
            }
        }
        return false;
    }

    public abstract Object transform () throws Json2JsonException;

    public String unitType(){
        return obj.keys().iterator().next();
    }

    public Object unitContent() {
        return obj.get(unitType());
    }
}
