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

> #### Template Unit
> * Template Unit is a **Json Object**, which **genetates** the transformed Json, and will be **replaced** by this transformed Json in the Target Json. e.g. 
> 
```
                     -------- generate ----->
                    |                         \.
{"%|": ["&$.json", " "]}  <-- replace -- "hello world"
```
Thus, 

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
> #### Json Path Reference
> To 

 

http://jsonpath.curiousconcept.com/

























