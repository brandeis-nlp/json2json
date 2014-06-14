<!-- Command+Shift+G -->
Json2Json Java Package
======================

Json2Json is **Template** based transform design. 
## How Json2Json Works?
Just like the [XSLT](http://www.freeformatter.com/xsl-transformer.html) design, we want design a **Json Template**.

- - - - - - - - - - - - 
**`Template`** + `Source Json` ===> `Target Json`

- - - - - - - - - - - - 



## Entities

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
* Note: Template must be a valid Json ( [online Json validation](http://lappsgrid.org/json.html) ). 


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

You can **test** this json2json with the example at [online test](http://lappsgrid.org/json.html).

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
> Thus, Template Unit is Json Format "Procedural Programming" design. It is just transforming by the execution of pieces of **commands** / **procedures**. It will be replaced once the transformed Json. 


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
{"COMMAND-SYMBOL", PARAMETER }
```
> #### Multi-Parameter Command Unit
```
{"COMMAND-SYMBOL", [PARAMETER_1, PARAMETER_2, ..., PARAMETER_m ] }
```



<!-- -->
> #### String Commands

<!--table><tbody><tr>
        <th>Command</th><th>Keyword</th><th>Symbol</th><th>Example</th><th>Description</th> </tr><tr>
        <td style="color:green"><em>name</em></td><td style="color:green"> <em>keywords </em></td><td style="color:green"><em> symbol </em></td><td style="color:green"><em>{ "SYMBOL" : [PARA_1, PARA_2, ...]}</em> == <em>OUTPUT</em></td><td>.</td> </tr>
        <tr>
        <td>concatenate</td><td> %concat </td><td> %+ </td><td>{"%+" : ["hello","world"]} == "helloworld"</td><td>.</td> </tr><tr>
        <td>split</td><td> %split </td><td> %| </td><td>{"%|" : ["hello.world","."]} == ["hello", "world"]</td><td>.</td> </tr>
        <tr>
        <td>join</td><td> %join </td><td> %* </td><td>{"%*" : [["hello", "world"],"."]} == "hello.world"</td><td>.</td> </tr><tr>
        <td>index</td><td> %idx </td><td>%?</td><td>{"%?" : ["hello.world", "hello"]} == 0 </td><td>.</td> </tr><tr>
        <td>substring</td><td> %sub </td><td>%_</td><td>{"%_" : ["hello.world", 0, 5]} == "hello"</td><td>.</td> </tr><tr>
        <td>length</td><td> %len </td><td>%#</td><td>{"%#" : "hello world"} == 11</td><td>.</td> </tr><tr>
        <td>replace</td><td> %rep </td><td>%/</td><td>{"%/" : ["hello.world", ".", " "]} ==  "hello world"</td><td>.</td> </tr><tr>
        <td>regex-match</td><td> %rmatch </td><td>%%</td><td>{"%%" : ["hello.world", "[a-z]+"]} ==  ["hello", "world"]</td><td>.</td> </tr><tr>
        <td>regex-split</td><td> %rsplit </td><td>%%|</td><td>{"%%|" : ["hello.world", ".l"]} ==  ["h", "lo.wo", "d"]</td><td>.</td> </tr><tr>
        <td>regex-replace</td><td> %rrep </td><td>%%/</td><td>{"%%/" : ["hello.world", ".l", "-"]} ==  "h-lo.wo-d"</td><td>.</td> </tr><tr>
        <td>jsonpath</td><td> %path </td><td>%&</td><td>{"%&" : [{"hello" : "1",  "world" : "2"}, "$.hello"]} ==  "1"</td><td>.</td> </tr></tbody></table-->


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

<!--table  class="v"><tbody><tr>
        <th>Function</th><th>Definition</th><th>Symbol</th><th>Example</th><th>Description</th> </tr><tr>
        <td style="color:green"><em>function name</em></td><td style="color:green"> <em>keywords </em></td><td style="color:green"><em> symbol </em></td><td style="color:green"><em>{ "$symbol" : [$para1, $para2, ...]}</em> == <em>$result</em></td><td>.</td> </tr><tr>
        <td>add</td><td> %array-add </td><td> %]+ </td><td>{"%]+" : [ ["hello","world"], "!" ] } ==  ["hello","world", "!"]</td><td>.</td> </tr><tr>
        <td>remove</td><td> %array-remove </td><td> %]- </td><td>{"%]-" : [ ["hello","world"], 0] } == ["world"]</td><td>.</td> </tr><tr>
        <td>get</td><td> %array-get</td><td> %]$ </td><td>{"%]$" : [ ["hello", "world"], 0]} == "hello"</td><td>.</td> </tr><tr>
        <td>sublist</td><td> %array-sub</td><td> %]_ </td><td>{"%]_" : [ ["hello", "world"], 0, 0]} == ["hello"]</td><td>.</td> </tr><tr>
        <td>index</td><td> %array-idx </td><td>%]?</td><td>{"%]?" : [ ["hello", "world"],"hello"]} == 0 </td><td>.</td> </tr><tr>
        <td>size</td><td> %array-size </td><td>%]#</td><td>{"%]#" : ["hello", "world"]} == 2</td><td>.</td> </tr><tr>
    </tr></tbody></table-->


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

<!--table  class="v"><tbody><tr>
        <th>Function</th><th>Definition</th><th>Symbol</th><th>Example</th><th>Description</th> </tr><tr>
        <td style="color:green"><em>function name</em></td><td style="color:green"> <em>keywords </em></td><td style="color:green"><em> symbol </em></td><td style="color:green"><em>{ "$symbol" : [$para1, $para2, ...]}</em> == <em>$result</em></td><td>.</td> </tr><tr>
        <td>put</td><td> %map-put </td><td> %}+ </td><td>{"%}+" : [{}, {"hello":"world"}] } == {"hello":"world"}</td><td>.</td> </tr><tr>
        <td>get</td><td> %map-get </td><td> %}$ </td><td>{"%}$" : [{"hello":"world"},"hello"]} == "world"</td><td>.</td> </tr><tr>
        <td>remove</td><td> %map-remove </td><td> %}- </td><td>{"%}-" : [{"hello" : "world"}, "hello"]} == {}</td><td>.</td> </tr><tr>
        <td>size</td><td> %map-size </td><td>%}#</td><td>{"%}#" : {"hello":"world"}} == 1 </td><td>.</td> </tr><tr>
        <td>keys</td><td> %map-keys </td><td>%}*</td><td>{"%}*" : {"hello":"world"}} == ["hello"]</td><td>.</td> </tr><tr>
    </tr></tbody></table-->
    


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

 



























