package com.recipegrace.wikispa.extractors

import org.dbpedia.extraction.wikiparser.impl.simple.SimpleWikiParser
import org.dbpedia.extraction.wikiparser.{WikiParser, PageNode}

/**
  * Created by Ferosh Jacob on 5/29/16.
  */
object Content  extends Base[String]{
  override def getParser(): WikiParser = new SimpleWikiParser()

  override def extractComponent(pageNode: PageNode): String = {
    pageNode.toPlainText.trim
  }
}
