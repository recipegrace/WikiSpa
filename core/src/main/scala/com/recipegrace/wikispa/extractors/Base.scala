package com.recipegrace.wikispa.extractors

import org.dbpedia.extraction.sources.{WikiPage, XMLSource}
import org.dbpedia.extraction.util.Language
import org.dbpedia.extraction.wikiparser.impl.wikipedia.Redirect
import org.dbpedia.extraction.wikiparser.{WikiParser, PageNode}

import scala.xml.Elem

/**
 * Created by Ferosh Jacob on 10/10/15.
 */
trait Base[T] {

  def getParser(): WikiParser

  def extract(xmlElem: Elem):Option[(Long,T)] = {
    try {
      extractWikiPage(xmlElem) match {
        case Some(x) => extractByPage(x)
        case _ => None
      }
    } catch {
      case _: Throwable => None
    }

  }

  def extractWikiPage(xmlElem:Elem):Option[WikiPage] = {
  try {
    val pages = XMLSource.fromXML(xmlElem, Language.English)
    if (pages.isEmpty) None
    else {
      val page = pages.head
      Some(page)
    }
  } catch {
    case _: Throwable => None
  }
  }

  def extractComponent(pageNode: PageNode): T

  def extractByPage(page:WikiPage):Option[(Long,T)] = {
    try {
      val parser = getParser()
      for( pageNode <- parser(page))
        yield (page.id, extractComponent(pageNode))
    } catch {
      case _:Throwable => None
    }
  }
}
