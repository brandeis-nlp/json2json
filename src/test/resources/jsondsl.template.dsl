html{
            "@xmlns:bar""http://www.bar.org"
            "@xmlns:foo""http://www.foo.org/"
            body {
                "@border" "1"
                h2  {
                            "@style": "font:12pt",
                            "__text__": "My CD Collection"
                          }
                table {
                    tr (
                        [{
                            "@bgcolor" "#9acd32"
                            th "Year", "Title", "Artist", "Country", "Company", "Price"
                        }] +
                        &$catalog.cd.select{&.year > 1990}.foreach {
                            [td : [ &.year, &.artist, &.company, &.country, &.price, &.title]]
                        }
                    )
                }
            }
        }