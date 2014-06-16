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

import edu.cs.brandeis.edu.json2json.Json2Json;
import org.lappsgrid.json2json.Json2JsonException;
import org.lappsgrid.json2json.jsonobject.JsonProxy;

/**
 * Created by shi on 6/15/14.
 */
public class JsonPathRefUnit extends JsonUnit {
    String path = null;
    int index = -1;
    boolean isPath = false;

    protected JsonPathRefUnit(JsonUnit ref) {
        super(ref);
        init();
    }

    public JsonPathRefUnit(Object obj) {
        super(obj);
        init();
    }

    protected void init(){
        if (obj != null && obj instanceof String) {
            String fullpath = ((String)obj).trim();
            if(fullpath.startsWith(TemplateNaming.JsonPathReference) && fullpath.contains("$.")) {
                isPath = true;
                int pathBegin = fullpath.indexOf("$.");
                if (pathBegin > 0) {
                    // default put "0"
                    index = 0;
                    // other sequence "1"
                    if (pathBegin > 1)
                        index = Integer.parseInt(fullpath.substring(1, pathBegin));
                }
                path = fullpath.substring(pathBegin);
            }
        }
    }

    public boolean isJsonPathRef(){
        return isPath;
    }

    public Object transform () throws Json2JsonException {
        if(isJsonPathRef()) {
            String ret =  Json2Json.path(jsons.get(index), path).trim();
            transformed = JsonProxy.str2json(ret);
        }
        return transformed;
    }

    public int getIndex() {
        return index;
    }

    public String getPath() {
        return path;
    }
}
