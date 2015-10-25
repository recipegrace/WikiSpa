package com.recipegrace.wikispa.extractors

import org.dbpedia.extraction.wikiparser.impl.simple.SimpleWikiParser
import org.dbpedia.extraction.wikiparser._

/**
 * Created by Ferosh Jacob on 10/10/15.
 */
object Categories  extends Base[List[String]]{
  override def getParser(): WikiParser = new SimpleWikiParser()

  private  def collectCategoryLinks(node: Node): List[InternalLinkNode] = {
    node match {
      case linkNode: InternalLinkNode if linkNode.destination.namespace == Namespace.Category => List(linkNode)
      case _ => node.children.flatMap(collectCategoryLinks)
    }
  }
  private  def isCategoryForArticle(linkNode: InternalLinkNode) = linkNode.destinationNodes match {
    case TextNode(text, _) :: Nil => !text.startsWith(":") // links starting wih ':' are actually only related, not the category of this article
    case _ => true
  }
   def extractComponent(pageNode: PageNode): List[String] = {
    val links = collectCategoryLinks(pageNode).filter(isCategoryForArticle)
    val totalLinks = for (
      each <- links if each.retrieveText.getOrElse("").startsWith("Category:")
    ) yield each.retrieveText.getOrElse("").split("Category:")(1).trim

    totalLinks
  }


}
