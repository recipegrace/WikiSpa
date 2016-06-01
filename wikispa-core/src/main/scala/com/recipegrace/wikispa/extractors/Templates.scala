package com.recipegrace.wikispa.extractors

import com.recipegrace.wikispa.ontology.ContextAccess
import org.dbpedia.extraction.config.mappings.InfoboxExtractorConfig
import org.dbpedia.extraction.util.Language
import org.dbpedia.extraction.wikiparser.impl.simple.SimpleWikiParser
import org.dbpedia.extraction.wikiparser._

import scala.collection.mutable.ArrayBuffer

/**
 * Created by Ferosh Jacob on 10/10/15.
 */
object Templates extends Base [List[String]] with ContextAccess {
  override def getParser(): WikiParser = new SimpleWikiParser

  def collectTemplates(node: Node): List[TemplateNode] = {
    node match {
      case templateNode: TemplateNode => List(templateNode)
      case _ => node.children.flatMap(collectTemplates)
    }
  }

  val commonsNamespacesContainingMetadata: Set[Namespace] = try {
    Set[Namespace](
      Namespace.Main,
      Namespace.File,
      Namespace.Category,
      Namespace.Template,
      Namespace.get(Language.Commons, "Creator").get,
      Namespace.get(Language.Commons, "Institution").get)
  } catch {
    case ex: java.util.NoSuchElementException =>
      throw new RuntimeException("Commons namespace not correctly set up: " +
        "make sure namespaces 'Creator' and 'Institution' are defined in " +
        "settings/commonswiki-configuration.xml")
  }

  val ignoreTemplates = InfoboxExtractorConfig.ignoreTemplates
  val ignoreTemplatesRegex = InfoboxExtractorConfig.ignoreTemplatesRegex

  def titleContainsCommonsMetadata(title: WikiTitle): Boolean =
    (title.language == Language.Commons && commonsNamespacesContainingMetadata.contains(title.namespace))
  override def extractComponent(node: PageNode): List[String] = {
    val subjectUri = node.title.resourceIri
    if (node.title.namespace != Namespace.Main && !titleContainsCommonsMetadata(node.title)) return List()
    val templates = new ArrayBuffer[String]()
    for {
      template <- collectTemplates(node)
      resolvedTitle = context.redirects.resolve(template.title).decoded.toLowerCase
      if !ignoreTemplates.contains(resolvedTitle)
      if !ignoreTemplatesRegex.exists(regex => regex.unapplySeq(resolvedTitle).isDefined)
    } {
      templates += resolvedTitle + ""

    }
    templates.toList
  }
}
