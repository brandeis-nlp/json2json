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

import org.lappsgrid.json2json.Json2JsonException;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by shi on 6/14/14.
 */
public class ScriptEngineTemplateEngine   extends TemplateEngine.EvalEngine {

    ScriptEngineManager manager = null;
    ScriptEngine engine = null;
    Bindings binding = null;

    public ScriptEngineTemplateEngine (){
        manager = new ScriptEngineManager();
        engine = manager.getEngineByName("java");
        binding = engine.createBindings();
    }
    
    public Object eval(String s) throws Json2JsonException{
        try {
            return engine.eval(s, binding);
        } catch (ScriptException e) {
            e.printStackTrace();
            throw new Json2JsonException("Unsupported eval function", e);
        }
    }

    
    public void bind(String name, Object val) {
        binding.put(name, val);
    }
}
