package com.recipegrace.wikispa.extractors

import org.dbpedia.extraction.sources.{WikiPage, XMLSource}
import org.dbpedia.extraction.util.Language
import org.dbpedia.extraction.wikiparser.{WikiParser, PageNode}

import scala.xml.Elem

/**
 * Created by Ferosh Jacob on 10/10/15.
 */
trait Base[T] {

  def getParser(): WikiParser

  def extract[T](xmlElem: Elem) = {
    try {
      extractWikiPage(xmlElem) match {
        case Some(x) => extractByPage(x)
        case _ => None
      }
    } catch {
      case _: Throwable => None
    }

  }

  def extractWikiPage(xmlElem:Elem) = {
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

  def extractByPage(wikiPage:WikiPage) = {

    val parser = getParser()
    parser(wikiPage) match {
      case Some(pageNode) => Some((wikiPage.id, extractComponent(pageNode)))
      case _ => None
    }

  }
}
