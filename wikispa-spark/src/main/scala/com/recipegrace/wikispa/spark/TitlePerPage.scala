package com.recipegrace.wikispa.spark

import com.recipegrace.biglibrary.electric.{ElectricContext, ElectricJob, FileAccess}


/**
 * Created by Ferosh Jacob on 10/10/15.
 */
object TitlePerPage extends ElectricJob[WikiFileAndSerialization] with  WikiAccess  with FileAccess {


  override def execute(argument:WikiFileAndSerialization)(implicit ec: ElectricContext)={
    val titlesPerPages =
      wikiPages(argument.wikiFile, argument.serializationType)
        .map(f =>(f.id,formatURL(f.title.pageIri),f.title.decoded))
        .map(f=> if(f._2!= f._3) f else (f._1, "-", f._3))
        .map(f=> List(f._1 ,f._2, f._3).mkString("\t"))

    writeFile(titlesPerPages,argument.output)

  }

  val WIKIURL = """https?\:\/\/en\.wikipedia\.org\/wiki\/(.*)""".r

  def formatURL(url:String) = {
    url match {
      case WIKIURL(resource) => resource
      case _ => url
    }

  }
}
