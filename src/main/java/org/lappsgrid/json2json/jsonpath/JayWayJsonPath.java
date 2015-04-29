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
package org.lappsgrid.json2json.jsonpath;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.internal.JsonReader;
import org.lappsgrid.json2json.Json2JsonException;

import java.util.Map;

import org.lappsgrid.json2json.jsonobject.JsonProxy;
import org.lappsgrid.json2json.jsonpath.JsonPath.IPath;

/**
 * <p>JsonPath implementation is not popular and made standard.
 * I have test several JsonPath, but JayWay is the fastest implementation.
 * </p>
 * <a href="https://github.com/jayway/JsonPath">JayWay</a>
 */
public class JayWayJsonPath implements IPath {

    public String path(String json, String path) throws Json2JsonException {
        Object obj = JsonPath.read(json, path);
//        System.out.println("json-path-ret-type:" + obj.getClass());
        if (obj == null) {
            return null;
        }
        return JsonProxy.obj2json(obj);
    }

    // cache path.
    public String path(String json, String path, Map<String, Object> cache)throws Json2JsonException {
        Object val = cache.get(json);
        if (val == null) {
            val = new JsonReader().parse(json);
            cache.put(json, val);
        }
        ReadContext reader = (JsonReader)val;

        val = cache.get(path);
        if (val == null) {
            val = JsonPath.compile(path);
            cache.put(path, val);
        }
        JsonPath jsonpath = (JsonPath)val;
        Object obj = reader.read(jsonpath);
//        System.out.println("json-path-ret-type:" + obj.getClass());
        if (obj == null) {
            return null;
        }
        return JsonProxy.obj2json(obj);
    }
}
