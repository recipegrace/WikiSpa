package com.recipegrace.wikispa.spark

import java.nio.charset.StandardCharsets

import com.recipegrace.biglibrary.electric.jobs.Arguments.ThreeArgument
import com.recipegrace.wikispa.spark.SplitWikiFile.SerializationType

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

    launch(ContentBasedFilter, ThreeArgument(  "files/enwiki-sample.xml","", output))

    testByLocalFile(output)

  }


}
