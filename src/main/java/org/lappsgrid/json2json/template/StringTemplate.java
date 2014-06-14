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
import org.lappsgrid.json2json.jsonobject.JsonProxy.JsonArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shi on 3/31/14.
 */
public class StringTemplate {
//    public static String split(String s, String sep) {
//        String [] arr = s.split(Pattern.quote(sep));
//        return JsonProxy.convertArray(arr).toString();
//    }


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

    public static String regex_split(String s, String sep) {
        String [] arr = s.split(sep);
        return JsonProxy.convertArray(arr).toString();
    }

    public static int length (String s) {
        return s.length();
    }

    public static String concat(String s, String t) {
        return s + t;
    }

    public static String join(String sarr, String sep) {
        JsonArray jarr =  JsonProxy.readArray(sarr);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < jarr.length(); i ++) {
            sb.append(sep);
            sb.append(jarr.get(i));
        }
        return sb.substring(sep.length());
    }


    public static int index(String s, String t) {
        return s.indexOf(t);
    }

    public static int index(String s, String t, int start) {
        return s.indexOf(t, start);
    }

    public static String substring(String s, int start, int end) {
        return s.substring(start, end);
    }

    public static boolean regex_contains(String s, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(s);
        return m.find();
    }

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

    public static String jsonpath(String json, String jsonpath) {
        try {
            return Json2Json.path(json, jsonpath);
        } catch (Json2JsonException e) {
            throw new RuntimeException(e);
        }
    }

    public static String regex_replace(String s, String regex, String rep) {
        return s.replaceAll(regex, rep);
    }

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
}
