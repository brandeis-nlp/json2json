html{
    "@xmlns:bar""http://www.bar.org"
    "@xmlns:foo""http://www.foo.org/"
    body {
        "@border" "1"
        h2  {}
        table {
            tr (
                [{ "@bgcolor" "#9acd32"
                    th "Title", "Artist", "Country", "Company", "Price", "Year" }]  +
                &$catalog.cd.sort{it.price}.foreach{
                    [td : [it.artist, it.company, it.country, it.price, it.title, it.year]]
                }
            )
        }
    }
}