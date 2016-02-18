package com.recipegrace.wikispa.spark

import java.nio.charset.StandardCharsets

import com.recipegrace.biglibrary.electric.jobs.Arguments.ThreeArgument
import com.recipegrace.biglibrary.electric.tests.SimpleJobTest
import com.recipegrace.wikispa.spark.SplitWikiFile.SerializationType
import org.apache.spark.rdd.RDDOperationScope

/**
 * Created by Ferosh Jacob on 10/10/15.
 */
class TitlePerPageTest extends BaseWikiJobTest {



  def testByLocalFile(output: String): Unit = {


    val lines = readFilesInDirectory(output, "part", StandardCharsets.ISO_8859_1)
    lines should contain("290\t-\tA")
  }

  test("titleperpage test") {



    val output= createTempPath()

    launch(TitlePerPage, ThreeArgument(  "files/enwiki-sample.xml","", output))

    testByLocalFile(output)

  }
  test("titleperpage test sequence") {


    runWikiJob(TitlePerPage, SerializationType.SequenceFile)
  }
  test("titleperpage test object") {



    runWikiJob(TitlePerPage, SerializationType.ObjectFile)
  }
}
