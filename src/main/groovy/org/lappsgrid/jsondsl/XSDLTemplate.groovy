/**********************************************************************************************************************
 Copyright [2015] [Chunqi SHI (chunqi.shi@hotmail.com)]

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

package org.lappsgrid.jsondsl

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.util.slurpersupport.GPathResult

import java.beans.XMLDecoder

/**
 * Created by lapps on 4/24/2015.
 */

class XSDLTemplate {
    static def call() {
        def json = new JsonBuilder()
        def root = json {
            userId userId
        }
        print json.toString()
    }

    def static main(String[]args) {
        String xmlString = this.getClass().getResource( '/cdcatalog.xml' ).text
//        println xmlString


        xmlString = "<text id=\"UnknownId\" xref=\"UserQuery\">\n" +
                "<title>Unknown title.</title>\n" +
                "<annotations>\n" +
                "<entity type=\"gene\" subtype=\"9606\" ids=\"4150;100302740\" startIndex=\"17\" endIndex=\"19\">saf</entity>\n" +
                "<entity type=\"gene\" subtype=\"9606\" ids=\"54187;6302\" startIndex=\"21\" endIndex=\"23\">sas</entity>\n" +
                "</annotations>\n" +
                "</text>";
        def __source_xml__ = new XmlSlurper().parseText(xmlString)
//        println __source_xml__.cd.collect {
//            [td : [ it.artist, it.company, it.country, it.price, it.title, it.year]]
//        }



        def builder = new groovy.json.JsonBuilder()

//        def root = builder.call({
//            annotations (__source_xml__.annotations.entity.collect {
//                    [
//                            start:it.@startIndex.text(),
//                            end:it.@endIndex.text(),
//                            features : [
//                                    category : it.@type.text(),
//                                    word : it.text(),
//                                    subtype : it.@subtype.text(),
//                                    ids : it.@ids.text()
//                            ]
//                    ]
//                }
//            )
//        })
//        builder.call ({annotations  ( __source_xml__.annotations.entity.collect{
//            [
//                    start:it.@startIndex.text(),
//                    end:it.@endIndex.text(),
//                    features : [
//                            category : it.@type.text(),
//                            word : it.text(),
//                            subtype : it.@subtype.text(),
//                            ids : it.@ids.text()
//                    ]
//            ]
//        })})

        Binding binding = new Binding();
        GroovyShell shell = new GroovyShell(binding);
        XmlSlurper xs = new XmlSlurper();
        binding.setVariable("__source_xml__", __source_xml__);
        binding.setVariable("__target_json__", null);
        JsonBuilder jb = new JsonBuilder();
        binding.setVariable("__json_builder__", jb);
        shell.evaluate("__json_builder__.call({annotations  ( __source_xml__.annotations.entity.collect{\n" +
                "            [\n" +
                "                    start:it.@startIndex.text(),\n" +
                "                    end:it.@endIndex.text(),\n" +
                "                    features : [\n" +
                "                            category : it.@type.text(),\n" +
                "                            word : it.text(),\n" +
                "                            subtype : it.@subtype.text(),\n" +
                "                            ids : it.@ids.text()\n" +
                "                    ]\n" +
                "            ]\n" +
                "        })})\n" +
                "__target_json__ = __json_builder__.toJsonString()"
        )
        println binding.getVariable("__target_json__");



//        def root = builder.call{
//            html{
//                "@xmlns:bar""http://www.bar.org"
//                "@xmlns:foo""http://www.foo.org/"
//                body {
//                    h2  {
//                        "@style" "font:12pt"
//                        "__text__" "My CD Collection"
//                    }
//                    table {
//                        "@border" "1"
//                        tr (
//                                [{
//                                     "@bgcolor" "#9acd32"
//                                     th "Title", "Artist", "Country", "Company", "Price", "Year"
//                                 }] +
//                                __source_xml__.cd.collect {
//                                    [td : [it.title.text(), it.artist.text(), it.company.text(), it.country.text(), it.price.text(),  it.year.text()]]
//                                }
//                        )
//                    }
//                }
//            }}

        println builder.toString()


    }
}

