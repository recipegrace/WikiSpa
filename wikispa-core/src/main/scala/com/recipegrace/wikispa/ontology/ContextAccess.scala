package com.recipegrace.wikispa.ontology

import org.dbpedia.extraction.mappings.Redirects
import org.dbpedia.extraction.ontology.io.OntologyReader
import org.dbpedia.extraction.sources.XMLSource
import org.dbpedia.extraction.util.Language

import scala.xml.XML

/**
 * Created by Ferosh Jacob on 10/10/15.
 */
trait ContextAccess {

  val context = new {
    def ontology = {
      val in = getClass().getResourceAsStream("/ontology.xml");

      val xmlElem = XML.load(in)

      val ontologySource = XMLSource.fromXML(xmlElem, Language.Mappings)
      new OntologyReader().read(ontologySource)
    }
    def language = Language.English

    def redirects = new Redirects(Map())


  }

}
