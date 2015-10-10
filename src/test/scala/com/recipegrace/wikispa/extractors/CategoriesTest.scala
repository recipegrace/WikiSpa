package com.recipegrace.wikispa.extractors

import com.recipegrace.wikispa.BasicTest


/**
 * Created by Ferosh Jacob on 10/10/15.
 */
class CategoriesTest extends BasicTest{

  test("example categoies") {

   val categories=    Categories.extract(lastPage()).get._2
    categories should have size(12)
    categories should contain ("1885 births")
  }

}
