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
import org.lappsgrid.json2json.jsonobject.JsonProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * <p>Provide command transforming support. </p>
 */
public class CommandUnit extends TemplateUnit {

    public CommandUnit(Object obj) {
        super(obj);
    }

    /**
     * Transform JsonArray into Parameter Object Array. In case of components is Template Unit,
     * we will do TemplateUnit.transform.
     * @param jarr
     * @return
     * @throws Json2JsonException
     */
    public Object [] toArray (JsonProxy.JsonArray jarr) throws Json2JsonException {
        if (jarr == null) {
            return null;
        }
        Object [] paras = new Object[jarr.length()];
        for(int i = 0; i < jarr.length(); i ++) {
            paras[i] = this.childUnit(jarr.get(i)).transform();
        }
        return paras;
    }

    public Object transform() throws Json2JsonException {
        if(super.isTemplate()) {
            String command = unitName();
            Object params = unitContent();
            List<Method> methods = ProxyMapping.methodByCommand(command);
            if (methods == null || methods.size() == 0)
                throw new Json2JsonException("Cannot find proxy mapping for the command: " + command);

            if(ProxyMapping.paramTypeByCommand(command) == ProxyMapping.ParamType.SingleParam) {
                // Single Parameter Transform
                /** In case of Template Unit, we will do Template.transform **/
                Object[] parameters = new Object[]{this.childUnit(params).transform()};
                transformed =  engine.invoke(methods, parameters);
            } else {
                // Multiple Parameter Transform
                JsonProxy.JsonArray arr = (JsonProxy.JsonArray) params;
                transformed =  engine.invoke(methods, toArray(arr));
            }

        }
        return transformed;
    }
}
