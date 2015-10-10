package com.recipegrace.wikispa.extractors

import org.dbpedia.extraction.sources.XMLSource
import org.dbpedia.extraction.util.Language
import org.dbpedia.extraction.wikiparser.{WikiParser, PageNode}
import org.dbpedia.extraction.wikiparser.impl.simple.SimpleWikiParser

import scala.xml.Elem

/**
 * Created by Ferosh Jacob on 10/10/15.
 */
trait Base[T] {

  def getParser():WikiParser 

  def extract[T]( xmlElem: Elem) ={
    val parser = getParser()
    val page = XMLSource.fromXML(xmlElem, Language.English).head
    parser(page) match {
      case Some(pageNode) =>  Some( page.id,extractComponent(pageNode))
      case _=> None
    }
  }

  def extractComponent(pageNode:PageNode):T



}
