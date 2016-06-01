package com.recipegrace.wikispa.extractors

import com.recipegrace.wikispa.BasicTest

/**
 * Created by Ferosh Jacob on 10/10/15.
 */
class TemplatesTest extends BasicTest{

  test("example templates") {

    val templates=    Templates.extract(lastPage()).get._2
    templates should have size(9)
    templates should contain ("commons category")
  }

}
