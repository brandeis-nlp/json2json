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

        shell.evaluate(dsl);
        return null;
    }


    public static String readResource(String filename) throws Exception {
        File objFile = new File(JDSL.class.getResource("/" + filename).toURI());
        return FileUtils.readFileToString(objFile);
    }

    public static void main(String [] args) throws Exception {
        String res = transform(readResource("source.json"), "");
        System.out.println(res);
    }
}
