package com.recipegrace.wikispa.extractors

import com.recipegrace.wikispa.BasicTest

/**
  * Created by Ferosh Jacob on 5/29/16.
  */
class ContentTest extends BasicTest{

  test("example categoies") {

    val categories=    Content.extract(lastPage()).get._2
    categories should have size 7211

  }

}
