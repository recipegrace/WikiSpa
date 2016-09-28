package com.recipegrace.wikispa.spark

import com.recipegrace.biglibrary.electric.{ElectricContext, ElectricJob}
import com.recipegrace.wikispa.extractors.LinkObjects.Link
import com.recipegrace.wikispa.extractors.Links
import com.recipegrace.wikispa.spark.CategoryPerPage._
import org.apache.spark.rdd.RDD


/**
 * Created by Ferosh Jacob on 10/10/15.
 */
case class AllLinksArgument(input:String, redirects:String, disambigs:String, internalLinks:String, serialization:String)
object LinksPerPage extends ElectricJob[AllLinksArgument] with WikiAccess{
  val WIKIURL = """https?\:\/\/en\.wikipedia\.org\/wiki\/(.*)""".r

  def formatURL(url:String) = {
    url match {
      case WIKIURL(resource) => resource
      case _ => url
    }

  }

  def formatLink(link: Link)={

    formatURL(link.pageIri)
  }
  def formatLinks(links: List[Link])={

    links.map(formatLink).mkString("\u0001")
  }


  def writeToFile(texts:RDD[(Long,String,String)],fileName:String)(implicit sc: ElectricContext)= {

    val content=texts
     .filter(f=> f._2.trim.nonEmpty)
      .map(f=>  List( f._1 , f._2,f._3).mkString("\t"))

    writeFile(content,fileName)
  }

  def execute(t: AllLinksArgument)(implicit sc: ElectricContext): Unit = {
   // implicit val context = sc

    val allPages =
      wikiPages(t.input, t.serialization)
        .map(f=>(f.id,formatLink(f.title), Links.extractByPage(f)))
        .filter(f=> f._3.nonEmpty)

    val redirects =
      allPages
        .filter(f=> f._3.get._2.redirects.nonEmpty)
        .map(f=>  (f._1,f._2, formatLinks(f._3.get._2.redirects.get)))



    val disambigs =
      allPages
        .filter(f=> f._3.get._2.disambigs.nonEmpty)
        .map(f=>  (f._1, f._2, formatLinks(f._3.get._2.disambigs.get)))


    val internalLinks =
      allPages
        .filter(f=> f._3.get._2.disambigs.isEmpty && f._3.get._2.redirects.isEmpty && f._3.get._2.internal.nonEmpty )
        .map(f=>  (f._1, f._2,formatLinks(f._3.get._2.internal.get)))


    writeToFile(redirects,t.redirects)
    writeToFile(disambigs,t.disambigs)
    writeToFile(internalLinks,t.internalLinks)
  }


}
