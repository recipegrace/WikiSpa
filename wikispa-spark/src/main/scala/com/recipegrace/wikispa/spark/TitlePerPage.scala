package com.recipegrace.wikispa.spark

import com.recipegrace.biglibrary.electric.ElectricContext
import com.recipegrace.biglibrary.electric.jobs.{TwoInputJob, SimpleJob}
import com.recipegrace.wikispa.extractors.Categories
import com.recipegrace.wikispa.spark.CategoryPerPage._


/**
 * Created by Ferosh Jacob on 10/10/15.
 */
object TitlePerPage extends TwoInputJob with WikiAccess  {
  val WIKIURL = """https?\:\/\/en\.wikipedia\.org\/wiki\/(.*)""".r

  def formatURL(url:String) = {
    url match {
      case WIKIURL(resource) => resource
      case _ => url
    }

  }

  override def execute(input: String, serialization:String,output: String)(implicit ec: ElectricContext)={
    val titlesPerPages =
      wikiPages(input, serialization)
        .map(f =>(f.id,formatURL(f.title.pageIri),f.title.decoded))
        .map(f=> if(f._2!= f._3) f else (f._1, "-", f._3))
        .map(f=> List(f._1 ,f._2, f._3).mkString("\t"))

    writeFile(titlesPerPages,output)

  }
}
