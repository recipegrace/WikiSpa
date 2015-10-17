WikiSpa
==========

This is a simple wrapper around the dbpedia-extraction framework mainly to make sure each execution is independent.
The project is focused in executing wikipedia queries locally.

Below code prints out all the wikipedia page titles and pageids separated by TAB into a file specified.  
The code runs on my laptop for the latest wikipedia data(enwiki-20151002-pages-articles-multistream.xml) in less than three hours.
 For the rich and the impatient, the code below can be deployed and executed in a Hadoop cluster.    

```scala
object CategoryPerPage extends SimpleJob with WikiAccess{
  override def execute(input: String, output: String)(ec: ElectricContext)= {
    implicit val context = ec
    val categoriesCount =
      wikiXML(input)
        .map(f => Categories.extract(f).getOrElse((0L, List(): List[String])))

        .filter(f => f._1 != 0 && f._2.nonEmpty)
        .map(f=> f._1 + "\t"+f._2.mkString("\u0001"))

    writeFile(categoriesCount,output)

  }
}
```
Repository location

```scala  
"Recipegrace repo" at "http://recipegrace.com:8080/nexus/content/repositories/releases/"
```

