package edu.brandeis.cs.json2json;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by shi on 4/10/14.
 */
public class Process {

    protected static final ConcurrentHashMap<String, String> Symbols = new ConcurrentHashMap<String, String>();

    protected static final ConcurrentHashMap<String, String> Exprs = new ConcurrentHashMap<String, String>();
    protected static final ConcurrentHashMap<String, String> Definitions = new ConcurrentHashMap<String, String>();
    private static final ConcurrentHashMap<String, Method> cache = new ConcurrentHashMap<String, Method>();

    static {
        // Process
        Definitions.put("%!proc", "%!+");
        Definitions.put("%def", "%$");
        Definitions.put("%steps", "%{}");
        Definitions.put("%ret", "%#");

        // If-Then-Else
        Definitions.put("%!if", "%!?");
        Definitions.put("%expr", "%<>");
        Definitions.put("%if", "%if");
        Definitions.put("%else", "%else");

        // For-Each
        Definitions.put("%!for", "%!*");
        Definitions.put("%iter", "%[]");
        Definitions.put("%each", "%each");

        // While
        Definitions.put("%!while", "%!_");
        Definitions.put("%do", "%do");

        // Process
        Symbols.put("%!+", "process");
        Symbols.put("%$", "variables");
        Symbols.put("%{}", "steps");
        Symbols.put("%#", "ret");
        Symbols.put("%!?", "if_then_else");
        Symbols.put("%<>", "expr");
        Symbols.put("%!*", "for_each");
        Symbols.put("%[]", "iterate");
        Symbols.put("%!_", "while_do");
        Symbols.put("%if", "if_");
        Symbols.put("%else", "else_");
        Symbols.put("%each", "each_");
        Symbols.put("%do", "do_");

        // Express
        Exprs.put("==", "==");
        Exprs.put(">", ">");
        Exprs.put("<", "<");
        Exprs.put(">=", ">=");
        Exprs.put("<=", "<=");
        Exprs.put("&&", "&&");
        Exprs.put("||", "||");
        Exprs.put("!", "!");
    }


    protected static Process SINGLE = new Process();


    protected static boolean section(String name, Object obj,  Map<String, Object> map) throws Json2JsonException{
         if (obj instanceof JSONObject) {
//             System.out.println("Section " + name +" " + obj);
             Object val = null;
             if(((JSONObject) obj).has(name)){
                 val = ((JSONObject) obj).get(name);
             }
             // update to new name.
             if(Definitions.containsKey(name)) {
                 name = Definitions.get(name);
             }
             if (val == null) {
                 if(((JSONObject) obj).has(name)){
                    val = ((JSONObject) obj).get(name);
                 }
             }
             System.out.println();
             System.out.println("Section : " + name +" " +val);
             System.out.println("Map : " + map);
             if (val != null) {
                invoke(name, val, map);
                return true;
             }
             return false;
         } else {
             throw new Json2JsonException("Wrong format : " + obj + " is not JSON object.");
         }
    }

    public static Object invoke(String name, Object obj, Map<String, Object> map) throws Json2JsonException{
        System.out.println("invoke : " + name  + " ( " + obj +" )");
        // replace definition with symbol
        Object ret = null;
        String symbol = Definitions.get(name);
        if (symbol != null) {
            name = symbol;
        }
        // replace symbol with method name
        String methodName = Symbols.get(name);
        if (methodName != null) {
//            System.out.println();
//            System.out.println("invoke : " + name +"(" + obj +")");
            System.out.println("map before : " + map);
            ret = GroovyEngine.invoke(SINGLE, methodName, obj, map);
            System.out.println("map after : " + map);
        } else {
            String var = name.substring(1);
            if (map.containsKey(var)) {
                Object replaced = Template2.replace(obj, map, null, null);
                map.put(var, replaced);
                System.out.println("assign : " + var +" = " + obj);
            }
            ret = true;
        }
        return ret;
    }

    public static boolean process (Object obj, Map<String, Object>  map) throws Json2JsonException {
        System.out.println("process : " + obj);
        if(obj instanceof JSONObject){
            Iterator<String> keys = ((JSONObject) obj).keys();
            while(keys.hasNext()) {
                String key = keys.next().trim();
                Object val = ((JSONObject) obj).get(key);
                System.out.println("key : "  + key);
                if (key.startsWith("%")) {
                    invoke(key, val, map);
                }
            }
        } else if(obj instanceof JSONArray) {
            steps(obj, map);
        }
        return true;
    }

    public static Iterable iterate(Object obj, Map<String, Object> map)throws Json2JsonException {
        System.out.println("iterate : "+ obj);
        ArrayList list = new ArrayList();
        if(obj instanceof JSONArray) {
            Object arr = ((JSONArray) obj).get(0);
            if (arr instanceof JSONArray) {
                if (((JSONArray) obj).length() == 1) {
                    map.put(Map_Index, Default_Each_Index);
                    map.put(Map_Each, Default_Each);
                } else if (((JSONArray) obj).length() == 3) {
                    String var_each_idx = (String)((JSONArray) obj).get(1);
                    String var_each=  (String)((JSONArray) obj).get(2);
                    var_each_idx = var_each_idx.trim();
                    var_each = var_each.trim();
                    if (!var_each_idx.startsWith("%")) {
                        throw new Json2JsonException("Wrong iteratable's index format : " + ((JSONArray) obj).get(1) + " is not %[a-z]* format.");
                    }
                    if (!var_each.startsWith("%")) {
                        throw new Json2JsonException("Wrong iteratable's iterator format : " + ((JSONArray) obj).get(2) + " is not %[a-z]* format.");
                    }
                    map.put(Map_Index, var_each_idx.substring(1));
                    map.put(Map_Each, var_each.substring(1));
                } else {
                    throw new Json2JsonException("Wrong iteratable format : " + obj +" is not [$iterable, $index, $each] or not [$iterable].");
                }

                for(int i = 0; i < ((JSONArray) arr).length(); i++)
                list.add(Template2.replace(((JSONArray) arr).get(i), map, null, null));
            } else {
                throw new Json2JsonException("Wrong iteratable format : " + obj +" is not [[$i0, $i1, ...],\"%i\",\"%e\"] format.");
            }
        } else {
            throw new Json2JsonException("Wrong iterator format : " + obj +" is not array.");
        }
        map.put(Map_Iterable, list);
        return list;
    }

    public static Object if_then_else(Object obj, Map<String, Object> map) throws Json2JsonException {
        if(obj instanceof JSONObject) {
            boolean exist = false;
            exist = section("%def", obj, map);
            exist = section("%expr", obj, map);
            if (!exist) {
                throw new Json2JsonException("If-Then-Else process needs expression section : " + "e.g. \"%<>\"   : { \"==\": [ \"&$.name\", \"OpenNLP\" ]}");
            }
            exist = section("%if", obj, map);
            if (!exist) {
                throw new Json2JsonException("If-Then-Else process needs if section : " + "e.g.  \"%if\"   : {}");
            }
            exist = section("%else", obj, map);
            exist = section("%ret", obj, map);
        } else {
            throw new Json2JsonException("Wrong if-then-else format : " + obj + " is not {\"%!if\" : {\n" +
                    "    \"%def\"  : {$initialization},\n" +
                    "    \"%expr\" : $expression,\n" +
                    "    \"%if\"   : [$if-steps, ...] ,\n" +
                    "    \"%else\" : [$else-steps, ...],\n" +
                    "    \"%ret\"  : $return-object } }");
        }
        return map.get(Map_Ret);
    }

    public static boolean expr(Object obj, Map<String, Object>map) throws Json2JsonException {
        boolean bool = false;
        System.out.println("expr : " + obj);
        if(obj instanceof JSONObject){
            Iterator<String> keys = ((JSONObject) obj).keys();
            while(keys.hasNext()) {
                String key = keys.next().trim();
                Object val = ((JSONObject) obj).get(key);

                if(Exprs.contains(key)) {
                    Object [] objs = null;
                    if(val instanceof JSONArray) {
                        objs = new Object[((JSONArray) val).length()];
                        for (int i = 0; i < ((JSONArray) val).length(); i++) {
                            objs[i] = Template2.replace(((JSONArray) val).get(i), map, null, null);
                            System.out.println(key + "---:" + objs[i].getClass());
                        }
                    } else {
                        objs = new Object[]{Template2.replace(val, map, null, null)};
                    }
                    bool = (Boolean)GroovyEngine.expr(key, objs);
                    System.out.println(key + "---:" + bool);
                }
            }
        } else if(obj instanceof Boolean) {
            bool = (Boolean)obj;
        }
        if(map != null) {
            map.put(Map_Expr, bool);
            map.put(Map_Expr_Obj, obj);
        }
        return bool;
    }

    public static boolean do_(Object obj, Map<String, Object> map) throws Json2JsonException {
        Boolean bool = (Boolean)map.get(Map_Expr);
        while(bool) {
            process(obj, map);
            expr(map.get(Map_Expr_Obj), map);
            bool = (Boolean)map.get(Map_Expr);
        }
        return true;
    }

    public static boolean if_(Object obj, Map<String, Object> map) throws Json2JsonException {
        Boolean bool = (Boolean)map.get(Map_Expr);
        if(bool) {
            process(obj, map);
        }
        return bool;
    }

    public static boolean else_(Object obj, Map<String, Object> map) throws Json2JsonException {
        Boolean bool = (Boolean)map.get(Map_Expr);
        if(!bool) {
            process(obj, map);
        }
        return bool;
    }

    public static Object for_each(Object obj, Map<String, Object> map) throws Json2JsonException{
        if(obj instanceof JSONObject) {
            boolean exist = false;
            exist = section("%def", obj, map);
            exist = section("%iter", obj, map);
            if (!exist) {
                throw new Json2JsonException("For-Each process needs iteratable section : " + "e.g. \"%[]\" : [ [\"hello\", \"world\"], \"%i\", \"%e\"]");
            }
            exist = section("%each", obj, map);
            if (!exist) {
                throw new Json2JsonException("For-Each process needs each section : " + "e.g. \"%each\" : {\"%+\": [\"%s\", \"%e\"]},");
            }
            exist = section("%ret", obj, map);
        } else {
            throw new Json2JsonException("Wrong if-then-else format : " + obj + " is not {\"%!for\" : {\n" +
                    "    \"%def\"     : {$initialization},\n" +
                    "    \"%iter\"    : [$iterable, $index, $each],\n" +
                    "    \"%each\"    : [{$each-process}, ...] ,\n" +
                    "    \"%ret\"     : $return-object }}");
        }
        return map.get(Map_Ret);
    }

    public static final String Map_Iterable = "___iterable___";
    public static final String Map_Each = "___each___";
    public static final String Map_Index = "___index___";
    public static final String Map_Expr = "___expr___";

    public static final String Map_Ret = "___ret___";
    public static final String Map_Expr_Obj = "___expr_obj___";
    public static final String Default_Each = "e";
    public static final String Default_Each_Index = "i";

    public static void each_(Object obj, Map<String, Object> map)  throws Json2JsonException{
        int i = 0;
        Iterable iterable = (Iterable)map.get(Map_Iterable);
        for (Object each: iterable) {
            String e = (String)map.get(Map_Each);
            String idx = (String)map.get(Map_Index);
            if(idx == null) {
                idx = Default_Each_Index;
            }
            if(e == null) {
                e = Default_Each;
            }
            map.put(e, each);
            map.put(idx, i);
            process(obj, map);
            i ++;
        }
    }

    public static Object while_do(Object obj, Map<String, Object> map)  throws Json2JsonException{
        if(obj instanceof JSONObject) {
            boolean exist = false;
            exist = section("%def", obj, map);
            exist = section("%expr", obj, map);
            if (!exist) {
                throw new Json2JsonException("While-Do process needs iteratable section : " + "e.g. \"%<>\"   : {\"<\":[ {\"%#\": \"%s\"}, 200]},");
            }
            exist = section("%do", obj, map);
            if (!exist) {
                throw new Json2JsonException("While-Do process needs each section : " + "e.g. \"%do\"   : {\"%+\": [\"%s\", \" next\"]},");
            }
            exist = section("%ret", obj, map);
        } else {
            throw new Json2JsonException("Wrong if-then-else format : " + obj + " is not {\"%!for\" : {\n" +
                    "    \"%def\"     : {$initialization},\n" +
                    "    \"%iter\"    : [$iterable, $index, $each],\n" +
                    "    \"%each\"    : [{$each-process}, ...] ,\n" +
                    "    \"%ret\"     : $return-object }}");
        }
        return map.get(Map_Ret);
    }


    public static Object ret (Object obj, Map<String, Object> map) throws Json2JsonException {
        Object ret = Template2.replace(obj, map, null, null);
        map.put(Map_Ret, ret);
        return ret;
    }

    public static Map<String, Object> variables (Object obj, Map<String, Object> map ) throws Json2JsonException {
        if (map == null)
            map = new HashMap<String, Object>();
        if(obj instanceof JSONObject) {
            Iterator<String> keys = ((JSONObject) obj).keys();
            while(keys.hasNext()) {
                String key = keys.next().trim();
                if (key.startsWith("%!")) {
                    map.put(key.substring(2), Template2.replace(((JSONObject) obj).get(key), map, null, null));
                }
            }
        }
        return map;
    }

    public static  boolean steps (Object obj, Map<String, Object> map) throws Json2JsonException{
        System.out.println("steps : " + obj);
        if (obj instanceof JSONArray) {
            for (int i = 0; i < ((JSONArray) obj).length(); i ++) {
                steps(((JSONArray) obj).get(i), map);
            }
        } else if (obj instanceof JSONObject) {
            Iterator<String> keys = ((JSONObject) obj).keys();
            while(keys.hasNext()) {
                String key = keys.next().trim();
                Object replaced = Template2.replace(((JSONObject) obj).get(key), map, null, null);
                if (key.startsWith("%!")) {
                    map.put(key.substring(2), replaced);
                } else if (key.startsWith("%")) {
                    String var = key.substring(1);
                    if (map.containsKey(var)) {
                        map.put(var, replaced);
                    }
                }
            }
        }
        return true;
    }
}
