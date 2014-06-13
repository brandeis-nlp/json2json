package org.lappsgrid.json2json;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

/**
 * Created by shi on 4/1/14.
 */
public class GroovyEngine {

    public static final String StringProxyName = "_string_proxy_";
    public static final String ArrayIteratorName = "_array_iterator_";
    public static final String ArrayIteratorIndexName = "_array_iterator_index_";
    public static final String ArrayIteratorEachName = "_array_iterator_each_";
    public static final String ArrayIteratorRetName = "_array_iterator_ret_";
    public static final String ReturnVariableName = "_return_variable_";


    @Deprecated
    public static String stringFilter(String script) {
        for(String key: StringProxy.Proxy.keySet()) {
            script = StringProxy.replace(script, key,  StringProxyName + "." + StringProxy.Proxy.get(key));
        }
        return script;
    }



    public static Object expr(String expr, Object ... objs) {
//        System.out.println("expr : " + expr + " " + Arrays.toString(objs));
        Binding binding = new Binding();
        long id = System.currentTimeMillis();
        StringBuilder  sb = new StringBuilder();
        if (1 == objs.length) {
            binding.setVariable("__a__" + id, objs[0]);
            sb.append("(").append(expr).append(" (");
            sb.append("__a__" + id).append("))");
        } else if (2 == objs.length) {
            binding.setVariable("__a__" + id, objs[0]);
            binding.setVariable("__b__" + id, objs[1]);
            sb.append("( (").append("__a__" + id);
            sb.append(") ").append(expr).append(" (");
            sb.append("__b__" + id).append(") )");
        }
        GroovyShell shell = new GroovyShell(binding);
//        System.out.println("script : " + sb.toString());
        return shell.evaluate("return " + sb.toString());
    }



    public static Object invoke(Object obj, String methodName, Object... paras) {
        Binding binding = new Binding();
        long id = System.currentTimeMillis();
        StringBuilder  sb = new StringBuilder("__v__" + id);
        binding.setVariable("__v__" + id, obj);
        sb.append(".").append(methodName).append("(");
        for (int i = 0; i < paras.length; i++) {
            binding.setVariable("__v__" + i + id, paras[i]);
            sb.append("__v__" + i + id).append(",");
        }
        sb.setLength(sb.length() - 1);
        sb.append(")");
        GroovyShell shell = new GroovyShell(binding);
//        System.out.println("script : " + sb.toString());
        Object res = shell.evaluate("return " + sb.toString());
        if(res == null)
            return null;
        return res;
    }

    @Deprecated
    public static String runFilter(String script) {
        Binding binding = new Binding();
        binding.setVariable(StringProxyName, new StringProxy());
        script = stringFilter(script);
        script = arrayIterate(script,binding);
        GroovyShell shell = new GroovyShell(binding);
//        System.out.println("script:" + script);
        return shell.evaluate("return " + script).toString();
    }


    public static final String ArrayIteratorBegin = "%-(";
    public static final String ArrayIteratorMiddle = "){";
    public static final String ArrayIteratorEnd = "}";

    @Deprecated
    public static class ArrayIterator {
        public static long count = 0;
        public String sarr = null;
        public String scon = null;
        public String [] arr = null;
        public JsonProxy.JsonArray jarr =  null;
        public String iterator = null;
        public String index =  null;
        public String each =  null;
        public String ret =  null;
        public boolean isValid = false;

        public ArrayIterator(String s, int start, int end) {
            int aiStart = s.indexOf(ArrayIteratorBegin,start);
            if(aiStart >= 0) {
                int aiStart1 = aiStart + ArrayIteratorBegin.length();
                int middle = s.indexOf(ArrayIteratorMiddle, aiStart1);
                if (middle >= 0) {
                    int middle1 = middle + ArrayIteratorMiddle.length();
                    int aiEnd = s.indexOf(ArrayIteratorEnd, middle1);
                    if(aiEnd >= 0 && aiEnd<=end) {
                        sarr = s.substring(aiStart1, middle);
                        jarr = JsonProxy.readArray(sarr);
                        arr = new String[jarr.length()];
                        iterator = ArrayIteratorName + count;
                        index = ArrayIteratorIndexName + count;
                        each = ArrayIteratorEachName + count;
                        ret = ArrayIteratorRetName + count;
                        count ++;
                        for(int i = 0; i < jarr.length(); i ++) {
                            arr[i] = jarr.get(i).toString();
                        }
                        scon = s.substring(middle1, aiEnd);
                        scon = StringProxy.replace(scon, "%i", index);
                        scon = StringProxy.replace(scon, "%e", each);
                        scon = StringProxy.replace(scon, "%r", ret);
                        isValid = true;
                    }
                }
            }
        }
    }


    @Deprecated
    public static String arrayIterate(String script, Binding binding) {
        int start = script.indexOf(ArrayIteratorBegin);
        int end = 0;
        StringBuilder sb = new StringBuilder();
        while(start >= 0 ) {
            sb.append(script.substring(end, start));
            end = script.indexOf(ArrayIteratorEnd, start);
            if(end > start) {
                end = end + ArrayIteratorEnd.length();
                ArrayIterator ai = new ArrayIterator(script, start, end);
                if(ai.isValid) {
                    StringBuilder aisb = new StringBuilder();
                    binding.setVariable(ai.iterator, ai.arr);
                    binding.setVariable(ai.ret, new String());
                    aisb.append(ai.iterator).append(".eachWithIndex(){ ");
                    aisb.append(ai.each).append(", ").append(ai.index);
                    aisb.append(" -> ").append(ai.scon).append("};");
                    GroovyShell shell = new GroovyShell(binding);
//                    System.out.println(aisb.toString());
                    shell.evaluate(aisb.toString());
                    String ret = binding.getVariable(ai.ret).toString();
                    sb.append("\"").append(ret).append("\"");
                }
                start = script.indexOf(ArrayIteratorBegin, end);
            } else {
                break;
            }
        }
        sb.append(script.substring(end));
        return sb.toString();
    }


}
