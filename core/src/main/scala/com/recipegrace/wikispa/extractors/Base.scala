package com.recipegrace.wikispa.extractors

import org.dbpedia.extraction.sources.XMLSource
import org.dbpedia.extraction.util.Language
import org.dbpedia.extraction.wikiparser.{WikiParser, PageNode}

import scala.xml.Elem

/**
 * Created by Ferosh Jacob on 10/10/15.
 */
trait Base[T] {

  def getParser(): WikiParser

  def extract[T](xmlElem: Elem) = {
    val parser = getParser()
    try {
      val pages = XMLSource.fromXML(xmlElem, Language.English)
      if (pages.isEmpty) None
      else {
        val page = pages.head

        parser(page) match {
          case Some(pageNode) => Some(page.id, extractComponent(pageNode))
          case _ => None
        }
      }
    } catch {
      case _: Throwable => None
    }

  }

  def extractComponent(pageNode: PageNode): T


}
