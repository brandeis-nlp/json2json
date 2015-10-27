html{
    "@xmlns:bar""http://www.bar.org"
    "@xmlns:foo""http://www.foo.org/"
    body {
        h2  {
                "@style" "font:12pt"
                "__text__" "My CD Collection"
            }
        table {
            "@border" "1"
            tr (
                [{
                    "@bgcolor" "#9acd32"
                    th "Title", "Artist", "Country", "Company", "Price", "Year"
                }] +
                &:catalog.cd.select{&.price > "9"}.foreach {
                    [td : [&.title, &.artist, &.company, &.country, &.price,  &.year]]
                }
            )
        }
    }
}