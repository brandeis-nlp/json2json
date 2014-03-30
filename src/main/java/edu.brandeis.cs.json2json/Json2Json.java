package edu.brandeis.cs.json2json;



public interface Json2Json {
    public String path(String json, String path) throws Json2JsonException;
    public String transform (String json) throws Json2JsonException;
}