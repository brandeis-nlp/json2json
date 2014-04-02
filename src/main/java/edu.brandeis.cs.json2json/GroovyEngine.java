package edu.brandeis.cs.json2json;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import net.minidev.json.JSONObject;
import org.json.JSONArray;

/**
 * Created by shi on 4/1/14.
 */
public class GroovyEngine {


//    public static void run(String script){
//        Binding binding = new Binding();
//        binding.setVariable("foo", new Integer(2));
//        GroovyShell shell = new GroovyShell(binding);
//
//        Object value = shell.evaluate("println 'Hello World!'; x = 123; return foo * 10");
//        assert value.equals(new Integer(20));
//        assert binding.getVariable("x").equals(new Integer(123));
//    }


    public static final String StringProxyName = "_string_proxy_";
    public static final String ArrayIteratorName = "_array_iterator_";
    public static final String ArrayIteratorIndexName = "_array_iterator_index_";
    public static final String ArrayIteratorEachName = "_array_iterator_each_";
    public static final String ArrayIteratorRetName = "_array_iterator_ret_";
    public static final String ReturnVariableName = "_return_variable_";


    public static String stringFilter(String script) {
        for(String key: StringProxy.Proxy.keySet()) {
            script = StringProxy.replace(script, key,  StringProxyName + "." + StringProxy.Proxy.get(key));
        }
        return script;
    }
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
    public static class ArrayIterator {
        public static long count = 0;
        public String sarr = null;
        public String scon = null;
        public String [] arr = null;
        public JSONArray jarr =  null;
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
                        jarr = new JSONArray(sarr);
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
