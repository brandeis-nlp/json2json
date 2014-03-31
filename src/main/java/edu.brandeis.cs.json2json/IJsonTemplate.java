package edu.brandeis.cs.json2json;



public interface
        IJsonTemplate {
    public String transform(String template, String... jsons) throws Json2JsonException;
}