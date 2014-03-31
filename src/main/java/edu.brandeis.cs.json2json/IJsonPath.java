package edu.brandeis.cs.json2json;



public interface
        IJsonPath {
    public String path(String json, String path) throws Json2JsonException;
}