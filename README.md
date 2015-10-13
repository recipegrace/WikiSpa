WikiSpa
==========

This is a simple wrapper around the dbpedia-extraction framework mainly to make sure each execution is independent.
The project is focused in executing wikipedia queries locally.

Below code prints out all wikipedia page titles and pageid separated by TAB. 
The code can be runs on my laptop on the entire wikipedia data in less than three hours, For the rich and the impatient, the code below can be executed 
in a Hadoop cluster also.   

```scala
object TitlePerPage extends ElectricJob with WikiAccess with SequentialFileAccess {
  val outputArgument = Argument("output")
  val inputArgument = Argument("input")

  override  val namedArguments = Set(inputArgument,outputArgument)
  override def job(args: Map[Argument, String], sc: ElectricContext) : Unit = {

    implicit val context=sc
    val idAndTitle =
      wikiPages(args(inputArgument))
        .map(f=> f.id +"\t"+f.title.decoded)
    writeFile(idAndTitle, args(outputArgument))

  }
}
```


