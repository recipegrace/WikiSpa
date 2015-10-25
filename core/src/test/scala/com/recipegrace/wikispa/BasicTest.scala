package com.recipegrace.wikispa

import org.scalatest.{Matchers, FunSuite}

import scala.xml.{XML, Elem}

/**
 * Created by Ferosh Jacob on 10/10/15.
 */
class BasicTest extends FunSuite with Matchers {


  def nodeToElem(node:scala.xml.Node):Elem = {

    <mediawiki>{node}</mediawiki>
  }

  def lastPage() = {
    val pages =XML.loadFile("files/enwiki-sample.xml") \ "page"
    nodeToElem(pages.last)
  }
  def lastPage(file:String) = {
    val pages =XML.loadFile(file) \ "page"
    nodeToElem(pages.last)
  }
}
