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

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lappsgrid.json2json.template.TemplateNaming.UnitType;
import org.lappsgrid.json2json.template.ProxyMapping.MappingUnitType;
/**
 * Created by shi on 6/15/14.
 */
public class CommandProxy {

    @MappingUnitType(mapping = UnitType.concatenation)
    public static String concat(String s, String t) {
        return s + t;
    }


    @MappingUnitType(mapping = UnitType.split)
    public static String split(String s, String sep) {
        String t = s;
        int end = s.indexOf(sep);
        int start = 0;
        ArrayList<String> list = new ArrayList<String>();
        while (end >= 0) {
            if(end > start) {
                list.add(s.substring(start, end));
            }
            start = end + sep.length();
            end = s.indexOf(sep, start);
        }
        if (start < s.length()) {
            list.add(s.substring(start));
        }
        String [] arr = list.toArray(new String[list.size()]);
        return JsonProxy.convertArray(arr).toString();
    }

    @MappingUnitType(mapping = UnitType.join)
    public static String join(String sarr, String sep) {
        JsonProxy.JsonArray jarr = JsonProxy.readArray(sarr);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < jarr.length(); i ++) {
            sb.append(sep);
            sb.append(jarr.get(i));
        }
        return sb.substring(sep.length());
    }

    @MappingUnitType(mapping = UnitType.join)
    public static String join(JsonProxy.JsonArray jarr, String sep) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < jarr.length(); i ++) {
            sb.append(sep);
            sb.append(jarr.get(i));
        }
        return sb.substring(sep.length());
    }


    @MappingUnitType(mapping = UnitType.index)
    public static int index(String s, String t) {
        return s.indexOf(t);
    }

    @MappingUnitType(mapping = UnitType.index)
    public static int index(String s, String t, int start) {
        return s.indexOf(t, start);
    }

    @MappingUnitType(mapping = UnitType.substring)
    public static String substring(String s, int start, int end) {
        return s.substring(start, end);
    }

    @MappingUnitType(mapping = UnitType.length, paramType = ProxyMapping.ParamType.SingleParam)
    public static int length (String s) {
        return s.length();
    }

//    public static String replace(String s, String f, String rep) {
//        return s.replaceAll(Pattern.quote(f), rep);
//    }

    @MappingUnitType(mapping = UnitType.replacement)
    public static String replace(String s, String f, String r) {
        String t = s;
        int start = s.indexOf(f);
        int end = 0;
        StringBuilder sb = new StringBuilder();
        while (start >= 0) {
            sb.append(s.substring(end, start));
            sb.append(r);
            end = start + f.length();
            start = s.indexOf(f, end);
        }
        if(end > 0) {
            sb.append(s.substring(end));
            t = sb.toString();
        }
        return t;
    }


    @MappingUnitType(mapping = UnitType.match_by_regular_expression)
    public static String regex_match(String s, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(s);
        ArrayList<String> arr = new ArrayList<String>();
        while(m.find()) {
            arr.add(m.group());
        }
//        System.out.println(arr);
        return JsonProxy.convertArray(arr).toString();
    }


    @MappingUnitType(mapping = UnitType.split_by_regular_expression)
    public static String regex_split(String s, String sep) {
        String [] arr = s.split(sep);
        return JsonProxy.convertArray(arr).toString();
    }

    public static boolean regex_contains(String s, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(s);
        return m.find();
    }

    @MappingUnitType(mapping = UnitType.replacement_by_regular_expression)
    public static String regex_replace(String s, String regex, String rep) {
        return s.replaceAll(regex, rep);
    }

    @MappingUnitType(mapping = UnitType.jsonpath)
    public static String jsonpath(JsonProxy.JsonObject json, String jsonpath) {
        try {
            return Json2Json.path(json.toString(), jsonpath);
        } catch (Json2JsonException e) {
            throw new RuntimeException(e);
        }
    }

    @MappingUnitType(mapping = UnitType.jsonpath)
    public static String jsonpath(String json, String jsonpath) {
        try {
            return Json2Json.path(json, jsonpath);
        } catch (Json2JsonException e) {
            throw new RuntimeException(e);
        }
    }


    /////////////////////////////////// Array Operations ///////////////////////////////////////////////////////////

    @MappingUnitType(mapping = UnitType.array_add)
    public static JsonProxy.JsonArray array_add (JsonProxy.JsonArray arr, Object obj) {
        return arr.add(obj);
    }


    @MappingUnitType(mapping = UnitType.array_remove)
    public static JsonProxy.JsonArray array_remove (JsonProxy.JsonArray arr, int i) {
        arr.remove(i);
        return arr;
    }


    @MappingUnitType(mapping = UnitType.array_get)
    public static Object array_get (JsonProxy.JsonArray arr, int i) {
        return arr.get(i);
    }

    @MappingUnitType(mapping = UnitType.array_set)
    public static Object array_set (JsonProxy.JsonArray arr, int i, Object obj ) {
        return arr.set(i, obj);
    }

    @MappingUnitType(mapping = UnitType.array_sub)
    public static JsonProxy.JsonArray array_sub (JsonProxy.JsonArray arr, int start, int end) {
        if (start < 0) {
            start = 0;
        }

        if (end > arr.length()) {
            end = arr.length();
        }
        JsonProxy.JsonArray narr = JsonProxy.newArray();
        for (int i = start; i < end; i ++) {
            narr.add(arr.get(i));
        }
        return narr;
    }


    @MappingUnitType(mapping = UnitType.array_index)
    public static Object array_index(JsonProxy.JsonArray arr, Object obj) {
        for (int i = 0; i < arr.length(); i ++) {
            if (obj.equals(arr.get(i))) {
                return i;
            }
        }
        return -1;
    }


    @MappingUnitType(mapping = UnitType.array_length, paramType = ProxyMapping.ParamType.SingleParam)
    public static int array_size (JsonProxy.JsonArray arr) {
        return arr.length();
    }

    /////////////////////////////////// Map Operations ///////////////////////////////////////////////////////////

    @MappingUnitType(mapping = UnitType.map_put)
    public static JsonProxy.JsonObject map_put (JsonProxy.JsonObject obj, JsonProxy.JsonObject m) {
        Collection<String> keys = m.keys();
        for (String key:keys) {
            Object val = m.get(key);
            obj.put(key,val);
        }
        return obj;
    }


    @MappingUnitType(mapping = UnitType.map_get)
    public static Object map_get (JsonProxy.JsonObject obj, String key) {
        return obj.get(key);
    }


    @MappingUnitType(mapping = UnitType.map_remove)
    public static JsonProxy.JsonObject map_remove (JsonProxy.JsonObject obj, String key) {
        obj.remove(key);
        return obj;
    }


    @MappingUnitType(mapping = UnitType.map_keys, paramType = ProxyMapping.ParamType.SingleParam)
    public static JsonProxy.JsonArray map_keys(JsonProxy.JsonObject obj) {
        return JsonProxy.convertArray(obj.keys());
    }


    @MappingUnitType(mapping = UnitType.map_length, paramType = ProxyMapping.ParamType.SingleParam)
    public static int map_size (JsonProxy.JsonObject obj) {
        return obj.length();
    }
}
