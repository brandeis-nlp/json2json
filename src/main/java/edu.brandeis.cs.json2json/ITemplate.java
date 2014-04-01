package edu.brandeis.cs.json2json;



public interface
        ITemplate {
    public String transform(String template, String... jsons) throws Json2JsonException;
}