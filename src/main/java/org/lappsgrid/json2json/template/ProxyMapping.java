/**********************************************************************************************************************
 Copyright [2014] [Chunqi SHI (chunqi.shi@hotmail.com)]

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE_2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 **********************************************************************************************************************/
package org.lappsgrid.json2json.template;
import org.lappsgrid.json2json.template.TemplateNaming.UnitType;
import sun.management.MethodInfo;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Define Annotation MappingUnitType and index annotated methods</p>
 */
public class ProxyMapping {

    /**
     * Use annotation MappingUnitType to map methods in proxy classes, such CommandProxy and ProcedureProxy.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    public @interface MappingUnitType {
        TemplateNaming.UnitType mapping ();
    }

    public static final Map<String, List<Method>> Indexes = new LinkedHashMap();


    static {
        // index CommandProxy class.
        index(CommandProxy.class, Indexes);
    }

//    public static final void main(String [] args) {
//        index(CommandProxy.class, Indexes);
//    }

    public static void index(Class cls, Map<String, List<Method>> indexes) {
        for (Method method : cls.getMethods()) {
            /** Annotation found**/
            if (method.isAnnotationPresent(MappingUnitType.class)) {
                MappingUnitType unitType = method.getAnnotation(MappingUnitType.class);
                if (unitType.mapping() != null) {
                    String name = unitType.mapping().name();
                    List<Method> methods = indexes.get(name);
                    if(methods == null) {
                        methods = new ArrayList<Method>();
                    }
                    methods.add(method);
                    indexes.put(name, methods);
                }
            }
        }
    }

    public static List<Method> methodByKeyword (String keyword) {
        String name = TemplateNaming.nameByKeyword(keyword);
        return Indexes.get(name);
    }

    public static List<String> methodnameByKeyword(String keyword) {
        List<Method> methods = methodByKeyword(keyword);
        if (methods == null)
            return null;
        List<String> names = new ArrayList<String>(methods.size());
        for(Method mtd : methods) {
            names.add(mtd.getName());
        }
        return names;
    }


    public static List<Method> methodBySymbol (String symbol) {
        String name = TemplateNaming.nameBySymbol(symbol);
        return Indexes.get(name);
    }

    public static List<String> methodnameBySymbol(String symbol) {
        List<Method> methods = methodBySymbol(symbol);
        if (methods == null)
            return null;
        List<String> names = new ArrayList<String>(methods.size());
        for(Method mtd : methods) {
            names.add(mtd.getName());
        }
        return names;
    }

    public static List<Method> methodByCommand(String name) {
        /** try command as symbol **/
        List<Method> methods = methodBySymbol(name);

        if (methods == null) {
            /** try command as keyword **/
            methods = methodByKeyword(name);
        }
        return methods;
    }

}
