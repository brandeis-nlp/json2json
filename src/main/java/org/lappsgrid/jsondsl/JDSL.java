/**********************************************************************************************************************
 Copyright [2015] [Chunqi SHI (chunqi.shi@hotmail.com)]

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 **********************************************************************************************************************/
package org.lappsgrid.jsondsl;

import groovy.json.JsonBuilder;
import groovy.json.JsonSlurper;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.apache.commons.io.FileUtils;

import java.io.File;


/**
 * Created by lapps on 4/24/2015.
 */
public class JDSL {
    public static String transform(String source, String dsl) {
        Binding binding = new Binding();
        GroovyShell shell = new GroovyShell(binding);
        JsonSlurper js = new JsonSlurper();
        Object json = js.parseText(source);
        binding.setVariable("__source_json__", json);
        binding.setVariable("__target_json__", null);
        JsonBuilder jb = new JsonBuilder();
        binding.setVariable("__json_builder__", jb);
        StringBuffer sb = new StringBuffer("__json_builder__.call(");
        sb.append(filter(dsl));
        sb.append(") \n");
        sb.append("__target_json__ = __json_builder__.toString()");
//        System.out.println("Source:\n" + binding.getVariable("__source_json__"));
//        System.out.println("Evaluate:\n" + sb.toString());
        shell.evaluate(sb.toString());
        return (String) binding.getVariable("__target_json__");
    }

    public static String[] OperatorIts = new String[]{
            "collect",
            "findAll",
            "find",
            "sort",
            "removeAll",
            "unique",
            "each"
    };

    public static String filter(String dsl) {
        dsl = dsl.trim();
        if(!dsl.startsWith("{")) {
            dsl = "{" + dsl + "}";
        }
        // replace global json
        dsl = dsl.replaceAll("\\.foreach\\s*\\{",".collect{");
        dsl = dsl.replaceAll("\\.select\\s*\\{",".findAll{");
        dsl = dsl.replaceAll("\\&\\$","__source_json__.");
        // replace local json
        dsl = dsl.replaceAll("\\&\\.","it.");
        return dsl;
    }

    public static String readResource(String filename) throws Exception {
        File objFile = new File(JDSL.class.getResource("/" + filename).toURI());
        return FileUtils.readFileToString(objFile);
    }

    public static void main(String [] args) throws Exception {
        String res = transform(readResource("jsondsl.source.json"), readResource("jsondsl.template.dsl"));
        System.out.println(res);
    }
}
