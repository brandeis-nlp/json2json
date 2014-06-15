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
package org.lappsgrid.json2json.template;

import org.lappsgrid.json2json.engine.TemplateEngine;
import org.lappsgrid.json2json.jsonobject.JsonProxy;

import java.lang.reflect.Method;

/**
 * Created by shi on 6/15/14.
 */
public class CommandUnit extends TemplateUnit {

    public static abstract class CommandTransform implements Transform {
        Method method = null;
        TemplateEngine.Engine engine = null;

        public CommandTransform(Method method, TemplateEngine.Engine engine) {
            this.method = method;
            this.engine = engine;
        }

        public abstract Object transform (String command, Object obj);

        @Override
        public Object transform(TemplateUnit obj) {
            Object ret = null;
            if(obj.isTemplate()) {
                ret = transform(obj.unitType(), obj.unitContent());
            }
            return ret;
        }
    }


    public static class SingleParameterTransform extends CommandTransform {
        public SingleParameterTransform(Method method, TemplateEngine.Engine engine) {
            super(method, engine);
        }

        public Object transform (String command, Object obj){

            return null;
        }

    }

    public static class MultipleParameterTransform extends CommandTransform {
        public MultipleParameterTransform(Method method, TemplateEngine.Engine engine) {
            super(method, engine);
        }

        public Object transform (String command, Object obj){

            return null;
        }
    }

    public CommandUnit(JsonProxy.JsonObject obj) {
        super(obj);
    }

    @Override
    public Object transform() {
        return null;
    }
}
