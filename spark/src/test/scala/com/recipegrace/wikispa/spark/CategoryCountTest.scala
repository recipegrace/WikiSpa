package com.recipegrace.wikispa.spark

import java.nio.charset.StandardCharsets

import com.recipegrace.biglibrary.electric.jobs.Arguments.ThreeArgument
import com.recipegrace.wikispa.spark.SplitWikiFile.SerializationType

/**
 * Created by Ferosh Jacob on 10/10/15.
 */
class CategoryCountTest extends BaseWikiJobTest {



  def testByLocalFile(output: String): Unit = {
    val lines = readFilesInDirectory(output, "part", StandardCharsets.ISO_8859_1)
    lines should contain("Objectivists\t1")
    lines should contain("Russian essayists\t1")
    lines should have size (218)
  }

  test("category count test") {



    val output= createTempPath()

    launch(CategoryCount, ThreeArgument(  "files/enwiki-sample.xml","", output))

    testByLocalFile(output)

  }
  test("category count test sequence") {


    runWikiJob(CategoryCount, SerializationType.SequenceFile)
  }
  test("category count test object") {



    runWikiJob(CategoryCount, SerializationType.ObjectFile)
  }

}
