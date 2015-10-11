package com.recipegrace.wikispa.extractors

import com.recipegrace.wikispa.extractors.LinkObjects.AllLinks
import com.recipegrace.wikispa.ontology.ContextAccess
import org.dbpedia.extraction.config.mappings.DisambiguationExtractorConfig
import org.dbpedia.extraction.wikiparser.impl.simple.SimpleWikiParser
import org.dbpedia.extraction.wikiparser._

/**
 * Created by Ferosh Jacob on 10/10/15.
 */

object LinkObjects {
  type Link=(String,String)
  case class AllLinks(redirects:Option[Link], internal:List[Link],disambig:Option[List[Link]])
}


object Links extends Base[AllLinks] with ContextAccess {

  override def getParser(): WikiParser = new SimpleWikiParser

  override def extractComponent(pageNode: PageNode)= {

    AllLinks(extractRedirect(pageNode), extractInternalLinks(pageNode), extractDisAmbigLinks(pageNode))
  }

  def extractRedirect(n: PageNode) = {
    val namespaces = Set(Namespace.Main, Namespace.Template, Namespace.Category)
    if (n.isRedirect && namespaces.contains(n.title.namespace)) {
      for (InternalLinkNode(destination, _, _, _) <- n.children) {

        if (!n.title.decoded.equalsIgnoreCase(destination.decoded))
          Some( n.title.decoded, destination.decoded)
      }
    }
    None
  }
  def extractDisAmbigLinks(n: PageNode): Option[ List[(String, String)]] = {
    val replaceString = DisambiguationExtractorConfig.disambiguationTitlePartMap(context.language.wikiCode)

    val allLinks = collectInternalLinks(n)
    val cleanPageTitle = n.title.decoded.replace(replaceString, "").toUpperCase(context.language.locale)

    if (!(n.title.namespace == Namespace.Main && n.isDisambiguation)) return None
    // use upper case to be case-insensitive. this also means we regard all titles as acronyms.

    // Extract only links that contain the page title or that spell out the acronym page title
    val disambigLinks = allLinks.filter { linkNode =>
      val cleanLink = linkNode.destination.decoded.toUpperCase(context.language.locale)
      cleanLink.contains(cleanPageTitle) || isAcronym(cleanPageTitle, cleanLink)
    }

    val totalLinks = for (
      each <- disambigLinks if (each.retrieveText.getOrElse("").length() > 0)
    ) yield (each.retrieveText.getOrElse("").trim, each.destination.decoded.toUpperCase(context.language.locale))

    Some(totalLinks.toList)

  }
  def extractInternalLinks(n: PageNode): (List[(String, String)]) = {
    val allLinks = collectInternalLinks(n)

    allLinks.map { x => (x.destination.decoded.toUpperCase(context.language.locale), x.retrieveText.getOrElse("")) }
      .filter(f => !f._1.equalsIgnoreCase(f._2))

  }
  private def isAcronym(acronym: String, destination: String): Boolean =
  {
    val destinationWithoutDash = destination.replace("-", " ")

    val destinationList =
      if (destinationWithoutDash.contains(" ")) destinationWithoutDash.split(" ")
      else destinationWithoutDash.split("")

    var matchCount = 0
    for (word <- destinationList) {
      if (word.toUpperCase(context.language.locale).startsWith(acronym(matchCount).toString)) matchCount += 1
      if (matchCount == acronym.length) return true
    }

    false
  }

  private def collectInternalLinks(node: Node): List[InternalLinkNode] = node match {
    case linkNode: InternalLinkNode => List(linkNode)
    case _ => node.children.flatMap(collectInternalLinks)
  }
}
