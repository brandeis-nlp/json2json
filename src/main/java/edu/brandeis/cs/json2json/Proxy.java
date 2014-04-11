package edu.brandeis.cs.json2json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shi on 3/31/14.
 */
public class Proxy {

    protected static final ConcurrentHashMap<String, String> Symbols = new ConcurrentHashMap<String, String>();
    protected static final ConcurrentHashMap<String, String> Definitions = new ConcurrentHashMap<String, String>();
    private static final ConcurrentHashMap<String, Method> cache = new ConcurrentHashMap<String, Method>();

    static {
        // String Filters
        Definitions.put("%concat", "%+");
        Definitions.put("%split", "%|");
        Definitions.put("%join", "%*");
        Definitions.put("%idx", "%?");
        Definitions.put("%sub", "%_");
        Definitions.put("%len", "%#");
        Definitions.put("%rep", "%/");
        Definitions.put("%rmatch", "%%");
        Definitions.put("%rspli", "%%|");
        Definitions.put("%rrep", "%%/");
        Definitions.put("%path", "%&");
        // String Filters
        Symbols.put("%+", "concat");
        Symbols.put("%|", "split");
        Symbols.put("%*", "join");
        Symbols.put("%?", "index");
        Symbols.put("%_", "substring");
        Symbols.put("%#", "length");
        Symbols.put("%/", "replace");
        Symbols.put("%%", "regex_match");
        Symbols.put("%%|", "regex_split");
        Symbols.put("%%/", "regex_replace");
        Symbols.put("%&", "jsonpath");

        ///////////////////////////////////////


        // Array Operators
        Definitions.put("%array-add", "%]+");
        Definitions.put("%array-remove", "%]-");
        Definitions.put("%array-get", "%]$");
        Definitions.put("%array-sub", "%]_");
        Definitions.put("%array-idx", "%]?");
        Definitions.put("%array-size ", "%]#");
        // Array Operators
        Symbols.put("%]+", "array_add");
        Symbols.put("%]-", "array_remove");
        Symbols.put("%]$", "array_get");
        Symbols.put("%]_", "array_sub");
        Symbols.put("%]?", "array_index");
        Symbols.put("%]#", "array_size");

        ///////////////////////////////////////


        // Map Operators
        Definitions.put("%map-put", "%}+");
        Definitions.put("%map-get", "%}$");
        Definitions.put("%map-remove", "%}-");
        Definitions.put("%map-size", "%}#");
        Definitions.put("%map-keys", "%}*");
        // Map Operators
        Symbols.put("%}+", "map_put");
        Symbols.put("%}$", "map_get");
        Symbols.put("%}-", "map_remove");
        Symbols.put("%}#", "map_size");
        Symbols.put("%}*", "map_keys");
    }

    protected static Proxy SINGLE = new Proxy();

    // JSON invoke to JSON
    public static Object invoke(String name, Object obj) {
        if (name == null)
            return null;
        // replace definition with symbol
        String symbol = Definitions.get(name);
        if (symbol != null) {
            name = symbol;
        }
        // replace symbol with method name
        String methodName = Symbols.get(name);
        if (methodName == null) {
            return null;
        }
//        System.out.println("Proxy.invoke : " + name +" ( " + obj +")");
        Object [] params = fromJSON2Arr(obj);
        if (methodName.equals("array_size")) {
            params = new Object[] {obj};
        }

        Object ret = GroovyEngine.invoke(SINGLE, methodName, params);
//        System.out.println("Proxy.invoke : " + name +" " + obj + " -> " + ret +" " + ret.getClass());
        return toJSON(ret);
    }



    public static Object toJSON (Object obj) {
//        System.out.println("Proxy.toJSON : " + obj + " " + obj.getClass());
        if(obj instanceof  String) {
            String json = ((String) obj).trim();
            if (json.startsWith("{")) {
                return new JSONObject((String)json);
            } else if (json.startsWith("[")) {
                return new JSONArray((String)json);
            }
        }
        return obj;
    }


    public static Object[] fromJSON2Arr (Object obj) {
        if (obj instanceof JSONArray) {
            Object [] ret = new Object[((JSONArray) obj).length()];
            for (int i = 0; i < ((JSONArray) obj).length(); i++) {
                Object item = ((JSONArray) obj).get(i);
                ret[i] = item;
            }
            return ret;
        }
        return new Object[]{obj};
    }

    public static String concat(String s, String t) {
        return s + t;
    }

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
        return new JSONArray(arr).toString();
    }

    public static String join(String sarr, String sep) {
        JSONArray jarr =  new JSONArray(sarr);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < jarr.length(); i ++) {
            sb.append(sep);
            sb.append(jarr.get(i));
        }
        return sb.substring(sep.length());
    }

    public static String join(JSONArray jarr, String sep) {
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

    public static int length (String s) {
        return s.length();
    }

//    public static String replace(String s, String f, String rep) {
//        return s.replaceAll(Pattern.quote(f), rep);
//    }

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

    public static String regex_match(String s, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(s);
        ArrayList<String> arr = new ArrayList<String>();
        while(m.find()) {
            arr.add(m.group());
        }
//        System.out.println(arr);
        return new JSONArray(arr).toString();
    }

    public static String regex_split(String s, String sep) {
        String [] arr = s.split(sep);
        return new JSONArray(arr).toString();
    }

    public static boolean regex_contains(String s, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(s);
        return m.find();
    }

    public static String regex_replace(String s, String regex, String rep) {
        return s.replaceAll(regex, rep);
    }

    public static String jsonpath(JSONObject json, String jsonpath) {
        try {
            return Json2Json.path(json.toString(), jsonpath);
        } catch (Json2JsonException e) {
            throw new RuntimeException(e);
        }
    }

    public static String jsonpath(String json, String jsonpath) {
        try {
            return Json2Json.path(json, jsonpath);
        } catch (Json2JsonException e) {
            throw new RuntimeException(e);
        }
    }


    /////////////////////////////////// Array Operations ///////////////////////////////////////////////////////////
    public static JSONArray array_add (JSONArray arr, Object obj) {
        return arr.put(obj);
    }

    public static JSONArray array_remove (JSONArray arr, int i) {
        arr.remove(i);
        return arr;
    }

    public static Object array_get (JSONArray arr, int i) {
        return arr.get(i);
    }

    public static JSONArray array_sub (JSONArray arr, int start, int end) {
        if (start < 0) {
            start = 0;
        }

        if (end > arr.length()) {
            end = arr.length();
        }
        JSONArray narr = new JSONArray();
        for (int i = start; i < end; i ++) {
            narr.put(arr.get(i));
        }
        return narr;
    }

    public static Object array_index(JSONArray arr, Object obj) {
        for (int i = 0; i < arr.length(); i ++) {
            if (obj.equals(arr.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public static int array_size (JSONArray arr) {
        return arr.length();
    }

    /////////////////////////////////// Map Operations ///////////////////////////////////////////////////////////
    public static JSONObject map_put (JSONObject obj, JSONObject m) {
        Iterator<String> keys = m.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object val = m.get(key);
            obj.put(key,val);
        }
        return obj;
    }

    public static Object map_get (JSONObject obj, String key) {
        return obj.get(key);
    }

    public static JSONObject map_remove (JSONObject obj, String key) {
        obj.remove(key);
        return obj;
    }

    public static JSONArray map_keys(JSONObject obj) {
        return new JSONArray(obj.keySet());
    }

    public static int map_size (JSONObject obj) {
        return obj.length();
    }

}
