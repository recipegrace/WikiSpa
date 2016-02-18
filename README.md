WikiSpa
==========
	
[![Build Status](https://travis-ci.org/recipegrace/WikiSpa.svg?branch=master)](https://travis-ci.org/recipegrace/WikiSpa)

This is a simple wrapper around the dbpedia-extraction framework mainly to make sure each execution is independent.
The project is focused in executing wikipedia queries locally or in a Spark cluster.

An example to print out all the wikipedia pageids and their categories separated by TAB is shown below. 
```scala
object CategoryPerPage extends SimpleJob with WikiAccess{
  override def execute(input: String, output: String)(ec: ElectricContext)= {
    implicit val context = ec
    val categoriesCount =
      wikiXML(input)
        .map(f => {
             //Category's extract method return (wikiPageId:Long, categories:List[String])
            //Check out wikispa core project to checkout more wiki operations
             Categories.extract(f).getOrElse((0L, List(): List[String]))
         })
        .filter(f => f._1 != 0 && f._2.nonEmpty)
        .map(f=> f._1 + "\t"+f._2.mkString("\u0001"))

    writeFile(categoriesCount,output)

  }
}
```

An example output is given below.
```text
290     ISO basic Latin letters,Vowel letters
334     Time scales
```
  
The code runs on a (16GB, OSX) laptop for the latest wikipedia data(enwiki-20151002-pages-articles-multistream.xml) in less than three hours.
 For the rich and the impatient, the code below can be deployed and executed in a Hadoop cluster.    


<h3>Repository location </h3>

```scala  
"Recipegrace repo" at "http://recipegrace.com:8080/nexus/content/repositories/releases/"
```

