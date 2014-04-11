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

    public static void invoke(String name, Object obj, Map<String, Object> map) {
        // replace definition with symbol
        String symbol = Definitions.get(name);
        if (symbol != null) {
            name = symbol;
        }
        // replace symbol with method name
        String methodName = Symbols.get(name);
        if (methodName != null) {
            System.out.println("invoke : " + name +"(" + obj +")");
            GroovyEngine.invoke(SINGLE, methodName, obj, map);
        }
    }

    public static void process (Object obj, Map<String, Object>  map) throws Json2JsonException {
        if(obj instanceof JSONObject){
            Iterator<String> keys = ((JSONObject) obj).keys();
            while(keys.hasNext()) {
                String key = keys.next().trim();
                Object val = ((JSONObject) obj).get(key);
                if (key.startsWith("%")) {
                    invoke(key, val, map);
                }
            }
        } else if(obj instanceof JSONArray) {
            steps(obj, map);
        }
    }

    public static void iterate(Object obj, Map<String, Object> map)throws Json2JsonException {
        ArrayList list = new ArrayList();
        if(obj instanceof JSONArray) {
            for(int i = 0; i < ((JSONArray) obj).length(); i++)
            list.add(Template2.replace(((JSONArray) obj).get(i), map, null, null));
        }
        map.put(Map_Iterable, list);
    }

    public static void if_then_else(JSONObject obj, Map<String, Object> map) throws Json2JsonException {
        process(obj, map);
    }

    public static boolean expr(Object obj, Map<String, Object>map) throws Json2JsonException {
        boolean bool = false;
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
//                            System.out.println("---:" + objs[i]);
                        }
                    } else {
                        objs = new Object[]{Template2.replace(val, map, null, null)};
                    }
                    bool = (Boolean)GroovyEngine.expr(key, objs);
                }
            }
        }
        if(map != null) {
            map.put(Map_Expr, bool);
            map.put(Map_Expr_Obj, obj);
        }
        return bool;
    }

    public static void do_(Object obj, Map<String, Object> map) throws Json2JsonException {
        Boolean bool = (Boolean)map.get(Map_Expr);
        while(bool) {
            process(obj, map);
            expr(map.get(Map_Expr_Obj), map);
        }
    }

    public static void if_(Object obj, Map<String, Object> map) throws Json2JsonException {
        Boolean bool = (Boolean)map.get(Map_Expr);
        if(bool) {
            process(obj, map);
        }
    }

    public static void else_(Object obj, Map<String, Object> map) throws Json2JsonException {
        Boolean bool = (Boolean)map.get(Map_Expr);
        if(!bool) {
            process(obj, map);
        }
    }

    public static void for_each(JSONObject obj, Map<String, Object> map) throws Json2JsonException{
        process(obj, map);
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

    public static void while_do(Object obj, Map<String, Object> map)  throws Json2JsonException{
        process(obj, map);
    }


    public static void ret (Object obj, Map<String, Object> map) throws Json2JsonException {
        Object ret = Template2.replace(obj, map, null, null);
        map.put(Map_Ret, ret);
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

    public static  void steps (Object obj, Map<String, Object> map) throws Json2JsonException{
        if (obj instanceof JSONArray) {
            for (int i = 0; i < ((JSONArray) obj).length(); i ++) {
                steps(((JSONArray) obj).get(i), map);
            }
        } else if (obj instanceof JSONObject) {
            Iterator<String> keys = ((JSONObject) obj).keys();
            while(keys.hasNext()) {
                String key = keys.next().trim();
                if (key.startsWith("%!")) {
                    map.put(key.substring(2), ((JSONObject) obj).get(key));
                } else if (key.startsWith("%")) {
                    String var = key.substring(1);
                    if (map.containsKey(var)) {
                        Object replaced = Template2.replace(((JSONObject) obj).get(key), map, null, null);
                        map.put(var, replaced);
                    }
                }
            }
        }
    }



}
