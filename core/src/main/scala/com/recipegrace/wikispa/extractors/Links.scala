package com.recipegrace.wikispa.extractors

import com.recipegrace.wikispa.extractors.LinkObjects.{Link, AllLinks}
import com.recipegrace.wikispa.ontology.ContextAccess
import org.dbpedia.extraction.config.mappings.DisambiguationExtractorConfig
import org.dbpedia.extraction.wikiparser.impl.simple.SimpleWikiParser
import org.dbpedia.extraction.wikiparser._

/**
 * Created by Ferosh Jacob on 10/10/15.
 */

object LinkObjects {
  type Link = WikiTitle

  case class AllLinks(redirects: Option[List[Link]], internal: Option[List[Link]], disambigs: Option[List[Link]])

}


object Links extends Base[AllLinks] with ContextAccess {

  override def getParser(): WikiParser = new SimpleWikiParser

  override def extractComponent(pageNode: PageNode): AllLinks = {

    AllLinks(extractRedirect(pageNode), extractInternalLinks(pageNode), extractDisAmbigLinks(pageNode))
  }

  def extractRedirect(n: PageNode): Option[List[Link]] = {
    val namespaces = Set(Namespace.Main, Namespace.Template, Namespace.Category)

    if (n.isRedirect && namespaces.contains(n.title.namespace)) {
      val links =
        for {
          InternalLinkNode(destination, _, _, _) <- n.children
             if !n.title.decoded.equalsIgnoreCase(destination.decoded)
        }
          yield (destination)

      Some(links)
    }
    else None
  }



  def extractDisAmbigLinks(n: PageNode): Option[List[Link]] = {

    try {
      val replaceString = DisambiguationExtractorConfig.disambiguationTitlePartMap(context.language.wikiCode)

      val allLinks = collectInternalLinks(n)
      val cleanPageTitle = n.title.decoded.replace(replaceString, "").toUpperCase(context.language.locale)

      if (!(n.title.namespace == Namespace.Main && n.isDisambiguation)) return None
      // use upper case to be case-insensitive. this also means we regard all titles as acronyms.

      // Extract only links that contain the page title or that spell out the acronym page title
      val disambigLinks = allLinks.filter { linkNode =>
        val cleanLink = linkNode.destination.decoded.toUpperCase(context.language.locale)
        //println(cleanLink, cleanPageTitle)
        cleanLink.contains(cleanPageTitle) || isAcronym(cleanPageTitle, cleanLink)
      }

      val totalLinks = for (
        each <- disambigLinks
      if each.retrieveText.nonEmpty
      ) yield each.destination


      if (totalLinks.nonEmpty) Some(totalLinks)
      else None
    }
    catch {
      case _: Throwable => None
    }

  }

  def extractInternalLinks(n: PageNode): Option[List[Link]] = {
    try {
      val totalLinks = collectInternalLinks(n)
        .map { x => x.destination }
        .filter(f => f!= n.title)
        .distinct

      if (totalLinks.nonEmpty) Some(totalLinks)
      else None
    }
    catch {
      case _: Throwable => None
    }

  }

  private def isAcronym(acronym: String, destination: String): Boolean = {
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
