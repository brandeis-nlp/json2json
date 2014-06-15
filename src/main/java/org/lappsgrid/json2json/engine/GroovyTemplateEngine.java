/**********************************************************************************************************************
 Copyright [2014] [Chunqi SHI (chunqi.shi@hotmail.com)]

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
package org.lappsgrid.json2json.engine;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.lappsgrid.json2json.Json2JsonException;
import org.lappsgrid.json2json.jsonobject.JsonProxy;
import org.lappsgrid.json2json.template.TemplateUnit;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by shi on 6/14/14.
 */
public class GroovyTemplateEngine  extends TemplateEngine.EvalEngine {
    Binding binding = null;

    public GroovyTemplateEngine(){
        binding = new Binding();
    }
    @Override
    public Object eval(String s) throws Json2JsonException{
        GroovyShell shell = new GroovyShell(binding);
        Object res = shell.evaluate(s);
        return res;
    }

    @Override
    public void bind(String name, Object val) {
        binding.setVariable(name, val);
    }
}
