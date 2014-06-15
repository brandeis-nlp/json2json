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

/**
 * Created by shi on 6/15/14.
 */
public class ProxyMapping {

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    public @interface MappingUnitType {
        TemplateNaming.UnitType mapping ();
    }


    public static final void main(String [] args) {
        index(CommandProxy.class);
    }


    public static void index(Class cls) {
        for (Method method : cls.getMethods()) {
//            System.out.println(method);
            if (method.isAnnotationPresent(MappingUnitType.class)) {
                System.out.println(method);
                try {
                    for (Annotation anno : method.getDeclaredAnnotations()) {
                        System.out.println("Annotation in Method"
                                + method + " : " + anno);
                    }
                    MappingUnitType methodAnno = method.getAnnotation(MappingUnitType.class);
                    if (methodAnno.mapping() != null) {
                        System.out.println("Method with mapping "
                                        + method);
                    }
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            }
        }

    }
}
