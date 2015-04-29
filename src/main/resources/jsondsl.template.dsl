catalogs{

    cds {
        annotations ( &$catalog.cd.foreach{
           [annotation : [mytitle:&.title, myartist:&.artist]]
        })
    }

}