package com.recipegrace.wikispa.spark

import com.recipegrace.biglibrary.electric.ElectricContext
import com.recipegrace.biglibrary.electric.jobs.SimpleJob
import com.recipegrace.wikispa.extractors.Categories


/**
 * Created by Ferosh Jacob on 10/10/15.
 */
object TitlePerPage extends SimpleJob with WikiAccess{


  override def execute(input: String, output: String)(ec: ElectricContext)= {
    implicit val context = ec
    val WIKIURL = """https?\:\/\/en\.wikipedia\.org\/wiki\/(.*)""".r

    def formatURL(url:String) = {
        url match {
          case WIKIURL(resource) => resource
          case _ => url
        }

    }
    val categoriesCount =
      wikiPages(input)
        .map(f =>(f.id,formatURL(f.title.pageIri),f.title.decoded))
        .map(f=> if(f._2!= f._3) f else (f._1, "-", f._3))
        .map(f=> List(f._1 ,f._2, f._3).mkString("\t"))

    writeFile(categoriesCount,output)

  }
}
