package org.lappsgrid.json2json.template;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Template Naming provides all the definition, symbols and expressions</p>
 */
public class TemplateNaming {

    public static final int Category = 0;
    public static final int Category_Symbol = 1;
    public static final int Symbol = 2;
    public static final int KeyWord = 3;
    public static final int Name = 4;
    public static final int NamingLength = 5;

    // [ Category, Category-Symbol, Symbol, KeyWord, Name, Class, Function-Name]
    public static final String [][] Namings = new String [][]{

            /** json path operation**/
            new String []{"jsonpath", "%", "&", "jsonpath", "jsonpath"},

            /** string operation **/
            new String []{"string", "%", "+", "concat", "concatenation"},
            new String []{"string", "%", "|", "split", "split"},
            new String []{"string", "%", "*", "join", "join"},
            new String []{"string", "%", "?", "idx", "index"},
            new String []{"string", "%", "_", "sub", "substring"},
            new String []{"string", "%", "#", "len", "length"},
            new String []{"string", "%", "/", "rep", "replacement"},
            new String []{"string", "%", "%", "rmatch", "match-by-regular-expression"},
            new String []{"string", "%", "%/", "rrep", "replacement-by-regular-expression"},
            new String []{"string", "%", "%|", "rsplit", "split-by-regular-expression"},

            /** array operation **/
            new String []{"array", "%]", "+", "arr-add", "array-add"},
            new String []{"array", "%]", "/", "arr-set", "array-set"},
            new String []{"array", "%]", "-", "arr-rm", "array-remove"},
            new String []{"array", "%]", "$", "arr-get", "array-get"},
            new String []{"array", "%]", "_", "arr-sub", "array-sub"},
            new String []{"array", "%]", "?", "arr-idx", "array-index"},
            new String []{"array", "%]", "#", "arr-len", "array-length"},

            /** map operation **/
            new String []{"map", "%}", "+", "map-put", "map-put"},
            new String []{"map", "%}", "$", "map-get", "map-get"},
            new String []{"map", "%}", "-", "map-rm", "map-remove"},
            new String []{"map", "%}", "#", "map-len", "map-length"},
            new String []{"map", "%}", "*", "map-keys", "map-keys"},

            /** process operation **/
            new String []{"process", "%!", "+", "proc", "default-process"},
            new String []{"process", "%!", "?", "if", "if-process"},
            new String []{"process", "%!", "*", "for", "for-each-process"},
            new String []{"process", "%!", "_", "while", "do-while-process"},

            /** default process operation **/
            new String []{"process-proc", "%", "$", "def", "definitions-of-process"},
            new String []{"process-proc", "%", "{}", "steps", "steps-of-process"},
            new String []{"process-proc", "%", "#", "ret", "return-of-process"},

            /** if process operation **/
            new String []{"process-if", "%", "$", "def", "definitions-of-if-process"},
            new String []{"process-if", "%", "<>", "expr", "expression-of-if-process"},
            new String []{"process-if", "%", "then", "then", "then-step-of-if-process"},
            new String []{"process-if", "%", "else", "else", "else-step-of-if-process"},
            new String []{"process-if", "%", "#", "ret", "return-of-if-process"},

            /** for process operation **/
            new String []{"process-for-each", "%", "$", "def", "definitions-of-for-each-process"},
            new String []{"process-for-each", "%", "[]", "iter", "iterator-of-for-each-process"},
            new String []{"process-for-each", "%", "each", "each", "each-step-of-for-each-process"},
            new String []{"process-for-each", "%", "#", "ret", "return-of-for-each-process"},


            /** while process operation **/
            new String []{"process-do-while", "%", "$", "def", "definitions-of-do-while-process"},
            new String []{"process-do-while", "%", "<>", "expr", "expression-of-do-while-process"},
            new String []{"process-do-while", "%", "do", "do", "do-step-of-do-while-process"},
            new String []{"process-do-while", "%", "#", "ret", "return-of-do-while-process"},

            /** expression operation **/
            new String []{"expression", "", "==", "==", "equals-expression"},
            new String []{"expression", "", ">", ">", "larger-than-expression"},
            new String []{"expression", "", "<", "<", "smaller-than-expression"},
            new String []{"expression", "", ">=", ">=", "no-smaller-than-expression"},
            new String []{"expression", "", "<=", "<=", "no-larger-than-expression"},
            new String []{"expression", "", "&&", "&&", "and-boolean-expression"},
            new String []{"expression", "", "||", "||", "or-boolean-expression"},
            new String []{"expression", "", "!", "!", "negate-boolean-expression"},
    };

    public static final Map<String, String[]> [] Indexes = new LinkedHashMap[NamingLength];

    static {
        /** if index is group information, all the Names in this group will be put in the Indexes **/
        final Map<String, List<String>> GroupHelper = new LinkedHashMap<String, List<String>>();

        for(int i = 0; i < NamingLength; i ++) {
            Indexes[i] = new LinkedHashMap<String, String[]>();
            for(String [] naming: Namings) {
                if(!Indexes[i].containsKey(naming[i])){
                    Indexes[i].put(naming[i], naming);
                } else {
                    List<String> groupNamings = GroupHelper.get(naming[i]);
                    if(groupNamings == null) {
                        groupNamings = new ArrayList<String>();
                        groupNamings.add(Indexes[i].get(naming[i])[Name]);
                        GroupHelper.put(naming[i], groupNamings);
                    } else {
                        groupNamings.add(Indexes[i].get(naming[i])[Name]);
                        GroupHelper.put(naming[i], groupNamings);
                        /** all the namings in this group will be indexed. **/
                        Indexes[i].put(naming[i], groupNamings.toArray(new String[groupNamings.size()]));
                    }
                }
            }
        }
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
}
