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
import org.lappsgrid.json2json.jsonobject.JsonProxy.JsonObject;
import org.lappsgrid.json2json.template.TemplateUnit;

import java.lang.reflect.Method;
import java.util.List;

/**
 * <p> Template Engine is executor of TemplateUnit. </p>
 */
public class TemplateEngine {

    public static interface Engine {
        public Object invoke (Method method, Object[] params) throws Json2JsonException;
        public Object invoke (Method[] methods, Object[] params) throws Json2JsonException;
        public Object invoke (List<Method> methods, Object[] params) throws Json2JsonException;
    }


    public static abstract class EvalEngine implements Engine{
        public abstract Object eval(String s) throws Json2JsonException;
        public abstract void bind(String name, Object val);

        @Override
        public Object invoke(Method method, Object[] params) throws Json2JsonException {
            Object obj = null;
            try {
                obj = method.getClass().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                throw new Json2JsonException("Failure in creating default instance.");
            }
            long id = System.currentTimeMillis();
            StringBuilder  sb = new StringBuilder("__v__" + id);
            bind("__v__" + id, obj);
            sb.append(".").append(method.getName()).append("(");
            for (int i = 0; i < params.length; i++) {
                bind("__v__" + i + id, params[i]);
                sb.append("__v__" + i + id).append(",");
            }
            sb.setLength(sb.length() - 1);
            sb.append(")");
            return eval("return " + sb.toString());
        }

        @Override
        public Object invoke(Method[] methods, Object[] params) throws Json2JsonException {
            Object ret = null;
            for(Method method : methods) {
                try{
                    ret = invoke(method, params);
                }catch (Throwable th) {
                    th.printStackTrace();
                }
                if(ret != null) {
                    return ret;
                }
            }
            return ret;
        }

        @Override
        public Object invoke(List<Method> methods, Object[] params) throws Json2JsonException {
            Object ret = null;
            for(Method method : methods) {
                try{
                    ret = invoke(method, params);
                }catch (Throwable th) {
                    th.printStackTrace();
                }
                if(ret != null) {
                    return ret;
                }
            }
            return ret;
        }

    }

    public static Engine newEngine(){
        // FIXME:  exchange other engine
        return new GroovyTemplateEngine();
    }

//    public static Engine newEngine(){
//        // FIXME:  exchange other engine
//        return new ScriptEngineTemplateEngine();
//    }
}
