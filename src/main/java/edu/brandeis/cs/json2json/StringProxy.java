package edu.brandeis.cs.json2json;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shi on 3/31/14.
 */
public class StringProxy {

    public static void main (String[]args) {
        System.out.println(split("hello.world", "."));
    }

    public static HashMap<String,String> Proxy = new HashMap<String, String>();
    static {
        Proxy.put("%+","concat");
        Proxy.put("%|","split");
        Proxy.put("%*","join");
        Proxy.put("%?","index");
        Proxy.put("%_","substring");
        Proxy.put("%%","regex_match");
        Proxy.put("%%/","regex_replace");
        Proxy.put("%%|","regex_split");
        Proxy.put("%/","replace");

    }

//    public static String split(String s, String sep) {
//        String [] arr = s.split(Pattern.quote(sep));
//        return new JSONArray(arr).toString();
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
        return new JSONArray(arr).toString();
    }

    public static String regex_split(String s, String sep) {
        String [] arr = s.split(sep);
//        System.out.println(Arrays.toString(arr));
        return new JSONArray(arr).toString();
    }

    public static String concat(String s, String t) {
        return s + t;
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
        return new JSONArray(arr).toString();
    }



    public static String regex_replace(String s, String regex, String rep) {
        return s.replaceAll(regex, rep);
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
}
