package com.recipegrace.wikispa.mediawiki

import scalaj.http.Http
import scala.xml.{XML, Elem}
import scalaj.http.HttpOptions
trait MediaWikiAccess {

  private val params = Map(
    "format" -> "xml",
    "action" -> "query",
    "prop" -> "revisions",
    "rvprop" -> "content")
  private def toMediaWiki(webXML: Elem) = {
    val rev = webXML \\ "rev"

    val wikiText = if(rev.isEmpty) <rev></rev>else  rev(0)
    <mediawiki>
      <page>
        <title>""</title>
        <ns>0</ns>
        <id>0</id>
        <revision>
          <id>0</id>
          <parentid>0</parentid>
          <minor/>
          <comment/>
          <text xml:space="preserve">
            { wikiText.text }
          </text>
        </revision>
      </page>
    </mediawiki>
  } 
  def wikiTitleSearch(query: String) = {

    val finalParams = params + ("titles" -> query)
    val request = Http("https://en.wikipedia.org/w/api.php")
      .params(finalParams)
      .charset("ISO-8859-1")
      .options(HttpOptions.connTimeout(5000), HttpOptions.readTimeout(5000))
    val textData = request.asString
    val xmlData = XML.loadString(textData.body)
    toMediaWiki(xmlData)
  }

}