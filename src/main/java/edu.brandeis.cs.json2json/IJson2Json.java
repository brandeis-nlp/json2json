package edu.brandeis.cs.json2json;



public interface
        IJson2Json {

    public static final String PREFIX = "&::";
    public static final String JAVA = "";
    public String path(String json, String path) throws Json2JsonException;
    public String transform(String template, String ... jsons) throws Json2JsonException;
}