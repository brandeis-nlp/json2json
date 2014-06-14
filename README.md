<!-- Command+Shift+G -->
Json2Json Java Package
======================

Json2Json is **Template** based transformation design. 
## How Json2Json Works?
Just like the [XSLT](http://www.freeformatter.com/xsl-transformer.html) design, we want design a **Json Template**.

- - - - - - - - - - - - 
**`Template`** + `Source Json` ===> `Target Json`

- - - - - - - - - - - - 



## Json Entities

> #### Source Json
> * Source Json is the entity from which Json we get the information; e.g. source.json:
>
```
{ "json" : "hello world" }
```
<!--
```
{ "json":["hello", "world"] }
```
-->



> #### Template Json
> * Template is also a Json, which guides how tranform from the source Json to the target Josn; e.g. template.json:
>
```
{ "json2json" : {"%|": ["&$.json", " "]}} 
```
<!--
```
{ "json2json" : {"%*":["&$.json", " "]}} 
```
-->
* Note: Template must be a valid Json ( [online Json validation](http://eldrad.cs-i.brandeis.edu:8484/jld/json.html) ). 

> #### Target Json
> * Target Json is the entity to which Json we want informtion to be; e.g. target.json:
>
```
{ "json2json" : ["hello", "world"] }
```
<!--
```
{ "json2json" : "hello world" }
```
-->
* Note: You can **test** this json2json with the example at [online test](http://eldrad.cs-i.brandeis.edu:8484/jld/json.html).

<!-- -->
## How Template Works?

We design **Template Unit** to make transformation. 


> #### What is Template and Target Json?
> **Template Json** is Json comprised of *Tempalte Units*; e.g.  
>  
```
{ "json2json" :  *Template Unit* } 
::
Template Unit == {"%*":["&$.json", " "]}
```

> **Target Json** is Template Json replaced the *Tempalte Units* by the *Transformed Unit*; e.g.  
>  
```
{ "json2json" :  *Transformed Unit* } 
::
Transformed Unit == "hello world"
```
<!-- -->
> #### Template Unit
> * Template Unit is a **Json Object**, which **genetates** the Transformed Unit, and will be **replaced** by this Transformed Unit in the Target Json. e.g. 
> 
```
                     -------- generate ----->
                    |                         \.
{"%|": ["&$.json", " "]}  <-- replace -- "hello world"
```
<a name="template-unit"></a>
> **Template Unit** is designed as the following:
>
```
{ "SYMBOL/KEYWORKS" : PARAMETERS / PROCESSES }
```
> Thus, Template Unit is Json Format "Procedural Programming" design. It is just transforming by the execution of pieces of **commands** / **procedures**. It will be replaced by the transformed Json finally. 


<!-- -->
> #### Transformed Unit
> To do the transformation, we need process each Template Unit into Transformed Unit. How it works? We use the procedure language

	Input Json Unit + Command/Procedue --> Output Json Unit. 

## How to Read Input Json Unit?
We use JsonPath to read Input Json Unit from the source. 
> #### Json Path Reference
> To read json unit from the Source Json, we design the following reference. 
> 
```
" &JSONPATH " , e.g. "&$.json"
```
To understand how JsonPath works, you could read online [JsonPath Design](http://goessner.net/articles/JsonPath/index.html#e2), and you can [test/evaluate](http://jsonpath.curiousconcept.com/) online. 

* Note: DO NOT forget the *reference* ( **&** ) mark.


## What Commands and Procedures?
How to design commands and procedure under the [Template Unit](#template-unit) framework?

### Command Units
Since Json Objects include **String**, **JsonArray**, **JsonObject**, and others (e.g. boolean, numbers). We design **String**, **Array**, **Map**, and **Expression** commands. 

Generally, we design command units as following:
> #### Single-Parameter Command Unit
```
{"COMMAND-SYMBOL" : PARAMETER }
```
> #### Multi-Parameter Command Unit
```
{"COMMAND-SYMBOL" : [PARAMETER_1, PARAMETER_2, ..., PARAMETER_m ] }
```



<!-- -->
> #### String Commands


Command | Symbol | Keyword | Parameter | Example| Output 
:----------- | :-----------: | :-----------: | :-----------: | :----------- | -----------:
name | %*symbol* | %*keyword* | [ *para_1,..., para_n* ] | { "%*symbol*" : [ *para_1,..., para_n* ] } | output
concatenate | %+ | *%concat*|[*str_1*,*str_2*]|{ "%+" : ["hello","world"] } |"helloworld" 
split | %\| | *%split*|[*str*, *sep*]|{ "%\|" : ["hello.world","."] }  | ["hello", "world"]
join | %* | *%join*|[ [*str_1,..., str_n*] , *sep*]|{ "%*" :   [ ["hello", "world"],"."] }  | "hello.world"
index | %? | *%idx*|[*str_1*, *str_2*]|{"%?" : ["hello.world", "hello"] }  | 0
substring | %_ | *%sub*|[*str*, *start*, *end*]|{"%_" : ["hello.world", 0, 5] }  | "hello"
*length | %#| *%len* | *str*|{"%#" : "hello world"} |  11
replace | %/ | *%rep*|[*str*, *s*, *r*]|{"%/" : ["hello.world", ".", " "]}   | "hello world"
regex-match | %% | *%rmatch*|[*str*, *reg*]|{"%%" : ["hello.world", "[a-z]+"]}  | ["hello", "world"]
regex-split | %%\| | *%rsplit*|[*str*, *rsep*]|{"%%\|" : ["hello.world", ".l"]}  | ["h", "lo.wo", "d"]
regex-replace | %%/ | *%rrep*|[*str*, *sreg*, *r*]|{"%%/" : ["hello.world", ".l", "-"]}   | "h-lo.wo-d"
jsonpath | %& | %path|[*jsonobject*, *path*]|{"%&" : [{"hello" : "1", "world" : "2"}, "$.hello"] }    | "1"

*  Note: We note the **Single-Parameter Command** with *star* (**\***) mark.  

> #### Array Commands

Command | Symbol | Keyword | Parameter | Example| Output 
:----------- | :-----------: | :-----------: | :-----------: | :----------- | -----------:
name | %]*symbol* | %]*keyword* | [ *para_1,..., para_n* ] | { "%*symbol*" : [ *para_1,..., para_n* ] } | output
add | %]+ | %array-add | [ [*arr_1,..., arr_m*], *new*] | {"%]+" : [ ["hello","world"], "!" ] }  | ["hello","world", "!"]
get | %]$ | %array-get | [ [*arr_1,..., arr_m*], *index*] | {"%]$" : [ ["hello", "world"], 0]}  | "hello"
set | %]/ | %array-set | [ [*arr_1,..., arr_m*], *index*, *new*] | {"%]/" : [ ["hello", "world"], 0, "hi"]}  | ["hi","world"]
remove | %]- | %array-rm | [ [*arr_1,..., arr_m*], *index*] | {"%]-" : [ ["hello","world"], 0] }   | ["world"]
*length |%]# | %array-len | [*arr_1,..., arr_m*]| {"%]#" : ["hello", "world"]} | 2
index |%]? | %array-idx | [[*arr_1,..., arr_m*], *arr_x*]| {"%]?" : [ ["hello", "world"],"hello"]}  | 0
sublist |%]_ | %array-sub | [[*arr_1,..., arr_m*], *start*, *end*]| {"%]_" : [ ["hello", "world"], 0, 0]}  | ["hello"]


*  Note: We note the **Single-Parameter Command** with *star* (**\***) mark.  

> #### Map Commands

Command | Symbol | Keyword | Parameter | Example| Output 
:----------- | :-----------: | :-----------: | :-----------: | :----------- | -----------:
name | %}*symbol* | %}*keyword* | [ *para_1,..., para_n* ] | { "%*symbol*" : [ *para_1,..., para_n* ] } | output
put | %}+ | %map-put | [ *jsonobject*, *key*, *value*] | {"%}+" : [{}, {"hello":"world"}] } |{"hello":"world"}
get | %}$ | %map-get | [ *jsonobject*, *key*] | {"%}$" : [{"hello":"world"},"hello"]} | "world"
*length | %}# | %map-len | *jsonobject* | {"%}#" : {"hello":"world"}}| 1
remove | %}- | %map-rm | *jsonobject* | {"%}-" : [{"hello" : "world"}, "hello"]} | {}
*keys | %}\* | %map-keys | *jsonobject* | {"%}\*" : {"hello":"world"}} |["hello"]


*  Note: We note the **Single-Parameter Command** with *star* (**\***) mark.  

> #### Expression Commands

Command | Symbol | Parameter | Example| Output 
:----------- |:-----------: | :-----------: | :----------- | -----------:
name | *symbol* | [ *para_1, para_2* ] | { "*symbol*" : [ *para_1, para_2* ] } | output
add , subtract , times , divide | + , - , * , / | [*num_1*, *num_2*]|{ "+": [1, 2] } | 3
equal , not equal , greater than , less than | == , != , > , < | [*num_1*, *num_2*]|{ ">": [1, 2] } | false
and , or | && , \|\| | [*bool_1*, *bool_2*]|{ "&&": [true, false] } | false
*negative | ! | *bool*|{ "!": true } | false




*  Note: We note the **Single-Parameter Command** with *star* (**\***) mark.  

> #### Composition Commands
> We are allowed to compose the above commands to make complex commands; e.g. 
> 
> **String Composition**
>
> Example: find all words from string, concat and regex-replace ".l" into "-".  
>
```
{ "%%/" : [ 
           {"%+" : 
		          {"%%" : ["hello.world", "[a-z]+"]}  	        ==  ["hello", "world"]
		                                           }  	        ==  "helloworld"
		                          , ".l", "-" ]    }  	        ==  "h-lowo-d"
```
> **Boolean Composition**
> 
```
{"!": { "&&": [ { ">": [ 2, 3 ]}, { "==": [ 1, 1 ]} ]} } 	    ==  false
```

*  Note: We note there are **Limits** of composition. It will very hard to make correct composition, since we have to guess output format (object, array, string, boolean) to prepare correct parameters. Thus, complex composition is NOT suggested. To prevent complex composition, using **Json Path Reference Command** and **Jsonpath String Commmand** can be helpful.   


### Procedure Units
The most important about procedure is that it allows **Variable**.
We design Procedure units as following:
> #### Procedure Unit
> **Procedue Unit** comprises 3 paramters :  **Initialization**, **Processes**, **Return-Value**. We provide 2 procedure definition format. 
> 
```
{
	"PROCEDURE-SYMBOL": [ { Initialization }, 
		                  { 
		                    "PROCESS-SYMBOL_1" : Process_1, 
		                    "PROCESS-SYMBOL_2" : Process_2,
		                    ...,
		                    "PROCESS-SYMBOL_n" : Process_n, 
		                  } ,
		                  Return-Value
		                ] 
}
```
> Or you can also define it as following 
>
```
{
	"PROCEDURE-SYMBOL": {  	"INIT-SYMBOL" : { Initialization },
		                    "PROCESS-SYMBOL_1" : Process_1, 
		                    "PROCESS-SYMBOL_2" : Process_2,
		                    ...,
		                    "PROCESS-SYMBOL_n" : Process_n, 
		                    "RETURN-SYMBOL" : Return-Value
		                } 
}
```
From the above, the Transformed Unit will be **"Return-Value"**. Once the procedure is transformed, it will be replaced by this return value. 




<!-- -->
> #### Initialization
>
> Variables must be declared and initialized in this sub-unit. 
> 
```
{  
   "%!variable_1" : value_1, 
   "%!variable_2" : value_2, 
   ..., 
   "%!variable_k" : value_k, 
}
```

*  Note: Variable is declared by "%!"

<!-- -->
> #### Return-Value
Return value can be a constant value, or a variable defined in the initialization. 

<!-- -->
> #### Processes
There are 3 typical procedures: **IF-TEHN-ELSE**, **FOR-EACH**, and **DO-WHILE**.


> #### IF-TEHN-ELSE

```
{"%!IF" : {
    "%$"    : {   "%!variable_1" : value_1, 
			   	   "%!variable_2" : value_2, 
   				   ..., 
   				   "%!variable_k" : value_k  
   			   },
    "%<>"   : Expression,
    "%THEN" : [Step_1, Step_2, ..., Step_l] ,
    "%ELSE" : [Step_1, Step_2, ..., Step_m],
    "%#"    : $return-object }  
}
```
Or the other format:

```
{"%!IF" : [  {   "%!variable_1" : value_1, 
			   	  "%!variable_2" : value_2, 
   				  ..., 
   				  "%!variable_k" : value_k  
   		     },
             {   "%<>" : Expression,
                 "%THEN" : [Step_1, Step_2, ..., Step_l] ,
                 "%ELSE" : [Step_1, Step_2, ..., Step_m]
             },  
             $return-object ]  
}
```

Name | Keyword | Symbol
:-----------     | :-----------: | -----------:
Initialization   | %DEF          | %$
Return-Value     | %RET          | %#
Expression       | %EXP          | %<>


We provide an example that choosing a URL according to name. 

```
{ "uri" :  { "%!IF": {
                          "%$"      :  {   "%!uri": "" },
                          "%<>"     :  { "==": [ "&$.name", "OpenNLP" ]},
                          "%THEN"   :  { "%uri": "http://www.opennlp.org"},
                          "%ELSE"   :  { "%uri": "http://unknown.org" }
                          "%#"      :  "%uri"    } 
           } 
}          
```
Or it can also be:

```
{ "uri" :  { "%!IF": [   {   "%!uri": "" },
                         {   "%<>"     : { "==": [ "&$.name", "OpenNLP" ]},
                             "%THEN"   : { "%uri": "http://www.opennlp.org"},
                             "%ELSE"   : { "%uri": "http://unknown.org" }
                         },
                         "%uri" ] 
            } 
}          
```
The Target Json will be:

```
{ "uri" : "http://www.opennlp.org" }
```

> #### FOR-EACH


```
{"%!FOR" : {
    "%$"    :  {   "%!variable_1" : value_1, 
			   	   "%!variable_2" : value_2, 
   				   ..., 
   				   "%!variable_k" : value_k  
   		       },
    "%[]"   : [[iterator_1, iterator_2, ..., iterator_l], "%i", "%e"],
    "%EACH" : [Step_1, Step_2, ..., Step_m] ,
    "%#"    : $return-object }  
}
```
Or the other format:

```
{"%!FOR" : [  {   "%!variable_1" : value_1, 
			   	  "%!variable_2" : value_2, 
   				  ..., 
   				  "%!variable_k" : value_k  
   		     },
             {   "%[]"   : [[iterator_1, iterator_2, ..., iterator_l], "%i", "%e"],
                 "%EACH" : [Step_1, Step_2, ..., Step_m]
             },  
             $return-object ]  
}
```

Name | Keyword | Symbol
:-----------     | :-----------: | -----------:
Initialization   | %DEF          | %$
Return-Value     | %RET          | %#
Iteration        | %ITER         | %[]



We provide an example that iterating a string array and concating all of them. 

```
{ "letters" :  { "%!FOR": {
                          "%$"      :  {   "%!str": "" },
                          "%[]"     :  [ ["hello", "world"], "%i", "%e"],
                          "%EACH"   :  {"%str": {"%+": ["%str", "%e"]} } 
                          "%#"      :  "%str"    } 
               } 
}          
```
Or it can also be:

```
{ "letters" :  { "%!FOR": [   {   "%!str" : "" },
                              {   "%[]"   : [ ["hello", "world"], "%i", "%e"],                             
                                  "%EACH" : {"%str": {"%+": ["%str", "%e"]}                     
                              } 
                         },
                         "%str" ] 
               } 
}          
```
The Target Json will be:

```
{ "letters" : "helloworld" }
```
> #### DO-WHILE


```
{"%!WHILE"  : {
    "%$"    : {   "%!variable_1" : value_1, 
			   	   "%!variable_2" : value_2, 
   				   ..., 
   				   "%!variable_k" : value_k  
   			   },
    "%<>"   : Expression,
    "%DO"   : [Step_1, Step_2, ..., Step_l] ,
    "%#"    : $return-object }  
}
```
Or the other format:

```
{"%!WHILE" : [  {   "%!variable_1" : value_1, 
			   	  "%!variable_2" : value_2, 
   				  ..., 
   				  "%!variable_k" : value_k  
   		        },
                {   "%<>" : Expression,
                    "%DO" : [Step_1, Step_2, ..., Step_l]
                },  
                $return-object ]  
}
```

Name | Keyword | Symbol
:-----------     | :-----------: | -----------:
Initialization   | %DEF          | %$
Return-Value     | %RET          | %#
Expression       | %EXP          | %<>



We provide an example that repeating text of "go!" until its length not greater than 10. 

```
{ "duplicated" :  { "%!WHILE": {
                          "%$"      :  {   "%!str": "" },
                          "%<>"     :  { "<": [ {"%#": "%str"}, 10] },
                          "%DO"     :  {"%str": {"%+": ["%str", " go!"]}},
                          "%#"      :  "%str"    } 
                  } 
}          
```
Or it can also be:

```
{ "duplicated" :  { "%!WHILE": [   {   "%!str": "" },
                                   {   "%<>"     :  { "<": [ {"%#": "%str"}, 10] },
                                       "%DO"     :  {"%str": {"%+": ["%str", " go!"]}}
                                   },
                                   "%uri" ] 
                 } 
}          
```
The Target Json will be:

```
{ "duplicated" : "go!go!go!" }
```
<!--
<table  class="v"><tbody><tr>
    <th>Function</th><th>Definition</th><th>Symbol</th><th>Example</th><th>Description</th> </tr><tr>
    <td>Define Variable</td><td>{"<b>%!</b><em>name</em>" : <em>$json-object</em>}</td><td>%!</td> <td>{"%!<em>v</em>" : 1, "%!<em>v2</em>" : "hello", "%!<em>v3</em>" : {}, "%!<em>v4</em>" : [] }</td><td>define number, string, map, and array</td></tr><tr>
    <td>Assignment Variable</td><td>{"<b>%</b><em>name</em>" : <em>$json-object</em>}</td><td>%</td> <td>{"%<em>v2</em>" : "hello"} == (<em>v2</em> = "hello")</td><td>define number, string, map, and array</td></tr><tr>
    <td style="color:green">Process</td><td style="color:green"><pre>{"<b>%!proc</b>" : {
    "%def"  : {$initialization},
    "%steps" : [{$Process}, ...],
    "%ret"  : $return-object
    }
}</pre></td><td style="color:green">%!+ <br> %$<br> %{}<br> %# </td>
        <td style="color:green"><pre>{"<b>%!proc</b>" : {
    "%$"  : {$initialization},
    "%{}" : [{$Process}, ...],
    "%#"  : $return-object
    }
}</pre></td><td></td></tr><tr>
    <td>If-Then-Else</td><td><pre>{"<b>%!if</b>" : {
    "%def"  : {$initialization},
    "%expr" : <em>$expression</em>,
    "%if"   : [$if-steps, ...] ,
    "%else" : [$else-steps, ...],
    "%ret"  : $return-object } }</pre></td>
        <td>%!? <br> %<><br> %$<br> %if<br> %else<br> %#</td> <td><pre>{"%!if": {
    "%$"    : {   "%!id": "" },
    "%<>"   : { "==": [ "&$.name", "OpenNLP" ]},
    "%if"   : { "%id": "http://www.opennlp.org"},
    "%else" : { "%id": "http://unknown.org" },
    "%#"    : "%id" } }       </pre></td><td>Result will be "http://www.opennlp.org". </td></tr><tr>
    <td>For-Each</td><td><pre>{"<b>%!for</b>" : {
    "%def"     : {$initialization},
    "%iter"    : [$iterable, $index, $each],
    "%each"    : [{$each-process}, ...] ,
    "%ret"     : $return-object }}</pre></td><td>%!*<br> %[]<br> %$<br> %each<br> %#</td> <td><pre>{"%!for": {
    "%$"       : {   "%!s": "" },
    "%[]"      : [ ["hello", "world"], "%i", "%e"],
    "%each" : {"%s": {"%+": ["%s", "%e"]}},
    "%#"       : "%s" } }       </pre></td><td>Result will be "helloworld". </td></tr><tr>
    <td>While</td><td><pre>{"<b>%!while</b>" : {
    "%def"     : {$initialization},
    "%expr"    :  <em>$expression</em>,
    "%do" : [{$each-process}, ...] ,
    "%ret"  : $return-object }}</pre></td><td>%!_<br> %<><br> %$<br> %do<br> %#</td> <td><pre>{"%!while": {
    "%$"    : {   "%!s": "" },
    "%<>"   : {"<":[ {"%#": "%s"}, 200]},
    "%do"   : {"%s": {"%+": ["%s", " next"]}},
    "%#"    : "%s" } }       </pre></td><td>Result will be  " next next next next". </td></tr><tr>
    </tr></tbody></table>

-->

### More Examples

 



























