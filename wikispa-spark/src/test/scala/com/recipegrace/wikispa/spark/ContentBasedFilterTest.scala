package com.recipegrace.wikispa.spark

/**
 * Created by Ferosh Jacob on 10/10/15.
 */
class ContentBasedFilterTest extends BaseWikiJobTest {


  def testByLocalFile(output: String): Unit = {

    val lines = readSparkOut(output)
    lines should have size 5
  }

  test("content per page per page test") {



    val output= createTempPath()

    launch(ContentBasedFilter, WikiFileAndSerialization(  "files/enwiki-sample.xml","", output))

    testByLocalFile(output)

  }


}
