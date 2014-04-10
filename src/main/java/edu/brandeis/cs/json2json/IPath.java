package edu.brandeis.cs.json2json;


import java.util.Map;

public interface
        IPath {
    public String path(String json, String path) throws Json2JsonException;
    public String path(String json, String path, Map<String, Object> cache) throws Json2JsonException;
}