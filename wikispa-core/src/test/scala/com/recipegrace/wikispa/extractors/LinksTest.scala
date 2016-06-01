package com.recipegrace.wikispa.extractors

import com.recipegrace.wikispa.BasicTest

/**
 * Created by Ferosh Jacob on 10/10/15.
 */
class LinksTest extends BasicTest{
/*
  test("example categoies") {

    val links =    Links.extract(lastPage()).get._2

    val redirects = links.redirects
      redirects should be (None)
    val disambigs = links.disambig
    disambigs should  be (None)
    val internalLinks = links.internal.get
    internalLinks should have size(65)
    internalLinks should contain (("PAULINE BUSH (ACTRESS)","Pauline Bush"))
  }
  */

  test("example disambig") {
    val links =    Links.extract(lastPage("files/enwiki-sample-disambig.xml")).get._2.disambigs.get
    links should have size(8)
  }
  test("example redirects") {
    val links =    Links.extract(lastPage("files/enwiki-sample-redirect.xml")).get._2.redirects.get
    links should have size(1)
  }
  test("example internal links") {
    val links =    Links.extract(lastPage("files/enwiki-sample-internal-links.xml")).get._2.internal.get
    links should have size(528)
  }
}
