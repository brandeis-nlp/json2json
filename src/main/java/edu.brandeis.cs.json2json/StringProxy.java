package edu.brandeis.cs.json2json;

/**
 * Created by shi on 3/31/14.
 */
public class StringProxy {

//
//    public static String split(String s, String sep) {
//        int index
//        return s.split(sep);
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
