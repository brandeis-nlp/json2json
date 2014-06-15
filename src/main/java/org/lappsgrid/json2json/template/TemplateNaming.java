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

import java.util.*;

/**
 * <p>Template Naming provides all the definition, symbols and expressions</p>
 */
public class TemplateNaming {

    public static final int Category = 0;
    public static final int Category_Symbol = 1;
    public static final int Symbol = 2;
    public static final int KeyWord = 3;
    public static final int Name = 4;
    public static final int TemplateType = 5;
    public static final int NamingLength = 6;

    public static final String ProcedureInitMark = "%!";
    public static final String JsonPathReference = "&";

    public enum UnitType {
        /** json path operation**/
        jsonpath,

        /** string operation **/
        concatenation,
        split,
        join,
        index,
        substring,
        length,
        replacement,
        match_by_regular_expression,
        replacement_by_regular_expression,
        split_by_regular_expression,

        /** array operation **/
        array_add,
        array_set,
        array_remove,
        array_get,
        array_sub,
        array_index,
        array_length,

        /** map operation **/
        map_put,
        map_get,
        map_remove,
        map_length,
        map_keys,

        /** process operation **/
        default_process,
        if_process,
        for_each_process,
        do_while_process,

        /** default process operation **/
        definitions_of_process,
        steps_of_process,
        return_of_process,

        /** if process operation **/
        definitions_of_if_process,
        expression_of_if_process,
        then_step_of_if_process,
        else_step_of_if_process,
        return_of_if_process,

        /** for process operation **/
        definitions_of_for_each_process,
        iterator_of_for_each_process,
        each_step_of_for_each_process,
        return_of_for_each_process,


        /** while process operation **/
        definitions_of_do_while_process,
        expression_of_do_while_process,
        do_step_of_do_while_process,
        return_of_do_while_process,

        /** expression operation **/
        equals_expression,
        larger_than_expression,
        smaller_than_expression,
        no_smaller_than_expression,
        no_larger_than_expression,
        and_boolean_expression,
        or_boolean_expression,
        negate_boolean_expression
    }


    public static int UnitTypeLength = UnitType.values().length;

    // [ Category, Category-Symbol, Symbol, KeyWord, Name, Template-Type]
    public static final String [][] Namings = new String [][]{

            /** json path operation**/
            new String []{"jsonpath", "%", "&", "jsonpath", UnitType.jsonpath.name(), TemplateUnit.TemplateType.Command.name()},

            /** string operation **/
            new String []{"string", "%", "+", "concat", UnitType.concatenation.name(), TemplateUnit.TemplateType.Command.name()},
            new String []{"string", "%", "|", "split", UnitType.split.name(), TemplateUnit.TemplateType.Command.name()},
            new String []{"string", "%", "*", "join", UnitType.join.name(), TemplateUnit.TemplateType.Command.name()},
            new String []{"string", "%", "?", "idx", UnitType.index.name(), TemplateUnit.TemplateType.Command.name()},
            new String []{"string", "%", "_", "sub", UnitType.substring.name(), TemplateUnit.TemplateType.Command.name()},
            new String []{"string", "%", "#", "len", UnitType.length.name(), TemplateUnit.TemplateType.Command.name()},
            new String []{"string", "%", "/", "rep", UnitType.replacement.name(), TemplateUnit.TemplateType.Command.name()},
            new String []{"string", "%", "%", "rmatch", UnitType.match_by_regular_expression.name(), TemplateUnit.TemplateType.Command.name()},
            new String []{"string", "%", "%/", "rrep", UnitType.replacement_by_regular_expression.name(), TemplateUnit.TemplateType.Command.name()},
            new String []{"string", "%", "%|", "rsplit", UnitType.split_by_regular_expression.name(), TemplateUnit.TemplateType.Command.name()},

            /** array operation **/
            new String []{"array", "%]", "+", "arr-add", UnitType.array_add.name(), TemplateUnit.TemplateType.Command.name()},
            new String []{"array", "%]", "/", "arr-set", UnitType.array_set.name(), TemplateUnit.TemplateType.Command.name()},
            new String []{"array", "%]", "-", "arr-rm", UnitType.array_remove.name(), TemplateUnit.TemplateType.Command.name()},
            new String []{"array", "%]", "$", "arr-get", UnitType.array_get.name(), TemplateUnit.TemplateType.Command.name()},
            new String []{"array", "%]", "_", "arr-sub", UnitType.array_sub.name(), TemplateUnit.TemplateType.Command.name()},
            new String []{"array", "%]", "?", "arr-idx", UnitType.array_index.name(), TemplateUnit.TemplateType.Command.name()},
            new String []{"array", "%]", "#", "arr-len", UnitType.array_length.name(), TemplateUnit.TemplateType.Command.name()},

            /** map operation **/
            new String []{"map", "%}", "+", "map-put", UnitType.map_put.name(), TemplateUnit.TemplateType.Command.name()},
            new String []{"map", "%}", "$", "map-get", UnitType.map_get.name(), TemplateUnit.TemplateType.Command.name()},
            new String []{"map", "%}", "-", "map-rm", UnitType.map_remove.name(), TemplateUnit.TemplateType.Command.name()},
            new String []{"map", "%}", "#", "map-len", UnitType.map_length.name(), TemplateUnit.TemplateType.Command.name()},
            new String []{"map", "%}", "*", "map-keys", UnitType.map_keys.name(), TemplateUnit.TemplateType.Command.name()},

            /** process operation **/
            new String []{"process", "%!", "+", "proc", UnitType.default_process.name(), TemplateUnit.TemplateType.Procedure.name()},
            new String []{"process", "%!", "?", "if", UnitType.if_process.name(), TemplateUnit.TemplateType.Procedure.name()},
            new String []{"process", "%!", "*", "for", UnitType.for_each_process.name(), TemplateUnit.TemplateType.Procedure.name()},
            new String []{"process", "%!", "_", "while", UnitType.do_while_process.name(), TemplateUnit.TemplateType.Procedure.name()},

            /** default process operation **/
            new String []{"process-proc", "%", "$", "def", UnitType.definitions_of_process.name(), TemplateUnit.TemplateType.Procedure.name()},
            new String []{"process-proc", "%", "{}", "steps", UnitType.steps_of_process.name(), TemplateUnit.TemplateType.Procedure.name()},
            new String []{"process-proc", "%", "#", "ret", UnitType.return_of_process.name(), TemplateUnit.TemplateType.Procedure.name()},

            /** if process operation **/
            new String []{"process-if", "%", "$", "def", UnitType.definitions_of_if_process.name(), TemplateUnit.TemplateType.Procedure.name()},
            new String []{"process-if", "%", "<>", "expr", UnitType.expression_of_if_process.name(), TemplateUnit.TemplateType.Procedure.name()},
            new String []{"process-if", "%", "then", "then", UnitType.then_step_of_if_process.name(), TemplateUnit.TemplateType.Procedure.name()},
            new String []{"process-if", "%", "else", "else", UnitType.else_step_of_if_process.name(), TemplateUnit.TemplateType.Procedure.name()},
            new String []{"process-if", "%", "#", "ret", UnitType.return_of_if_process.name(), TemplateUnit.TemplateType.Procedure.name()},

            /** for process operation **/
            new String []{"process-for-each", "%", "$", "def", UnitType.definitions_of_for_each_process.name(), TemplateUnit.TemplateType.Procedure.name()},
            new String []{"process-for-each", "%", "[]", "iter", UnitType.iterator_of_for_each_process.name(), TemplateUnit.TemplateType.Procedure.name()},
            new String []{"process-for-each", "%", "each", "each", UnitType.each_step_of_for_each_process.name(), TemplateUnit.TemplateType.Procedure.name()},
            new String []{"process-for-each", "%", "#", "ret", UnitType.return_of_for_each_process.name(), TemplateUnit.TemplateType.Procedure.name()},


            /** while process operation **/
            new String []{"process-do-while", "%", "$", "def", UnitType.definitions_of_do_while_process.name(), TemplateUnit.TemplateType.Procedure.name()},
            new String []{"process-do-while", "%", "<>", "expr", UnitType.expression_of_do_while_process.name(), TemplateUnit.TemplateType.Procedure.name()},
            new String []{"process-do-while", "%", "do", "do", UnitType.do_step_of_do_while_process.name(), TemplateUnit.TemplateType.Procedure.name()},
            new String []{"process-do-while", "%", "#", "ret", UnitType.return_of_do_while_process.name(), TemplateUnit.TemplateType.Procedure.name()},

            /** expression operation **/
            new String []{"expression", "", "==", "==", UnitType.equals_expression.name(), TemplateUnit.TemplateType.Expression.name()},
            new String []{"expression", "", ">", ">", UnitType.larger_than_expression.name(), TemplateUnit.TemplateType.Expression.name()},
            new String []{"expression", "", "<", "<", UnitType.smaller_than_expression.name(), TemplateUnit.TemplateType.Expression.name()},
            new String []{"expression", "", ">=", ">=", UnitType.no_smaller_than_expression.name(), TemplateUnit.TemplateType.Expression.name()},
            new String []{"expression", "", "<=", "<=", UnitType.no_larger_than_expression.name(), TemplateUnit.TemplateType.Expression.name()},
            new String []{"expression", "", "&&", "&&", UnitType.and_boolean_expression.name(), TemplateUnit.TemplateType.Expression.name()},
            new String []{"expression", "", "||", "||", UnitType.or_boolean_expression.name(), TemplateUnit.TemplateType.Expression.name()},
            new String []{"expression", "", "!", "!", UnitType.negate_boolean_expression.name(), TemplateUnit.TemplateType.Expression.name()},
    };

    public static final Map<String, String[]> [] Indexes = new LinkedHashMap[NamingLength];

    static {
        /** if index is group information, all the Names in this group will be put in the Indexes **/
        final Map<String, List<String>> GroupHelper = new LinkedHashMap<String, List<String>>();

        for(int i = 0; i < NamingLength; i ++) {
            Indexes[i] = new LinkedHashMap<String, String[]>();
            for(String [] naming: Namings) {

                String indexKey = naming[i];
                if (i == Symbol) {
                    /** update symbol as Category symbol concatenated with individual Symbol. **/
                    indexKey = naming[Category_Symbol] + indexKey;
                }
                if(!Indexes[i].containsKey(indexKey)){
                    Indexes[i].put(indexKey, naming);
                } else {
                    List<String> groupNamings = GroupHelper.get(indexKey);
                    if(groupNamings == null) {
                        groupNamings = new ArrayList<String>();
                        groupNamings.add(Indexes[i].get(indexKey)[Name]);
                        GroupHelper.put(indexKey, groupNamings);
                    } else {
                        groupNamings.add(Indexes[i].get(indexKey)[Name]);
                        GroupHelper.put(indexKey, groupNamings);
                        /** all the namings in this group will be indexed. **/
                        Indexes[i].put(indexKey, groupNamings.toArray(new String[groupNamings.size()]));
                    }
                }
            }
        }
    }

    public static boolean hasSymbol(String symbol) {
        return Indexes[Symbol].containsKey(symbol);
    }

    public static boolean hasKeyword(String keyword) {
        return Indexes[KeyWord].containsKey(keyword);
    }


    public static String nameBySymbol (String symbol) {
        String[] naming =   Indexes[Symbol].get(symbol);
        String name = null;
        if (naming != null)  {
            name = naming[Name];
        }
        return name;
    }

    public static String nameByKeyword (String keyword) {
        String[] naming =   Indexes[KeyWord].get(keyword);
        String name = null;
        if (naming != null)  {
            name = naming[Name];
        }
        return name;
    }

    public static String symbolByName (String name) {
        String[] naming =   Indexes[Name].get(name);
        String symbol = null;
        if (naming != null)  {
            symbol = naming[Symbol];
        }
        return symbol;
    }

    public static String keywordByName (String name) {
        String[] naming =   Indexes[Name].get(name);
        String keyword = null;
        if (naming != null)  {
            keyword = naming[KeyWord];
        }
        return keyword;
    }


    public static String symbolByKeyWord (String keyword) {
        String[] naming =   Indexes[KeyWord].get(keyword);
        String symbol = null;
        if (naming != null)  {
            symbol = naming[Symbol];
        }
        return symbol;
    }


    public static String keywordBySymbol (String symbol) {
        String[] naming =   Indexes[Symbol].get(symbol);
        String keyword = null;
        if (naming != null)  {
            keyword = naming[KeyWord];
        }
        return keyword;
    }

    public static String templateTypeBySymbol(String symbol) {
        String ttype = null;
        String[] naming =   Indexes[Symbol].get(symbol);
        if (naming != null)  {
            ttype = naming[TemplateType];
        }
        return ttype;
    }

    public static String templateTypeByKeyWord(String keyword) {
        String ttype = null;
        String[] naming =   Indexes[KeyWord].get(keyword);
        if (naming != null)  {
            ttype = naming[TemplateType];
        }
        return ttype;
    }

    public static TemplateUnit.TemplateType templateTypeByCommand(String command) {
        String ttype = templateTypeBySymbol(command);
        if(ttype == null) {
            ttype = templateTypeByKeyWord(command);
        }
        if(ttype != null) {
            return TemplateUnit.TemplateType.valueOf(ttype);
        }
        return null;
    }

}
