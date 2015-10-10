package com.recipegrace.wikispa.extractors

import com.recipegrace.wikispa.BasicTest

/**
 * Created by Ferosh Jacob on 10/10/15.
 */
class LinksTest extends BasicTest{

  test("example categoies") {

    val links =    Links.extract(lastPage()).get._2

    val redirects = links.redirects
      redirects should be (None)
    val disambigs = links.disambig
    disambigs should  be (None)
    val internalLinks = links.internal
    internalLinks should have size(65)
    internalLinks should contain (("PAULINE BUSH (ACTRESS)","Pauline Bush"))
  }

}
