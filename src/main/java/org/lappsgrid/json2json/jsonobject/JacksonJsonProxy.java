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

import java.util.*;

/**
 * <p> Jackson Json provides best implementation of Json functions</p>
 * <a href="https://github.com/FasterXML">Jackson Json</a>.
 *
 */
@Deprecated
public class JacksonJsonProxy implements IJsonProxy {
    public Object unwrap(Object obj) {
        if (obj instanceof  IJsonWrapper) {
            return ((IJsonWrapper) obj).original();
        }
        return obj;
    }

    public IJsonArr newArr() {
        return new JacksonJsonArr();
    }

    public IJsonObj newObj() {
        return new JacksonJsonObj();
    }

    public Object wrap(Object obj ) {
        if(obj instanceof List) {
            return new JacksonJsonArr((List)obj);
        } else if(obj instanceof Map) {
            return new JacksonJsonObj((Map)obj);
        }
        return obj;
    }
}
