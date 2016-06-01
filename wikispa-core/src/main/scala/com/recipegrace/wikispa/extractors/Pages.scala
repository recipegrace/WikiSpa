package com.recipegrace.wikispa.extractors

import org.dbpedia.extraction.wikiparser._
import org.dbpedia.extraction.wikiparser.impl.simple.SimpleWikiParser

/**
 * Created by Ferosh Jacob on 10/10/15.
 */
object Pages  extends Base[Unit]{
  override def getParser(): WikiParser = new SimpleWikiParser()


  override def extractComponent(pageNode: PageNode)= {

  }
}
