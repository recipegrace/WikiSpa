package com.recipegrace.wikispa.mediawiki

import scalaj.http.HttpResponse
import scalaj.http._
/**
 * Created by Ferosh Jacob on 10/10/15.
 */
object ExampleRequest extends App {

  private val params = Map(
    "format" -> "xml",
    "action" -> "query",
    "prop" -> "revisions",
    "rvprop" -> "content")


  val finalParams = params + ("titles" -> "Main")
  val response = Http("http://en.wikipedia.org/w/api.php")
    .params(finalParams)
    .asString


  //val response: HttpResponse[String] = Http("https://en.wikipedia.org/w/api.php").params(finalParams).asString

  println(response.body)

}
