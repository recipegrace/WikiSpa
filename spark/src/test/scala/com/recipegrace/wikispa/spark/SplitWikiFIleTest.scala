package com.recipegrace.wikispa.spark

import java.nio.charset.StandardCharsets

import com.recipegrace.biglibrary.electric.jobs.Arguments.ThreeArgument
import com.recipegrace.biglibrary.electric.tests.TwoInputJobTest

/**
 * Created by Ferosh Jacob on 10/25/15.
 */
class SplitWikiFileTest extends TwoInputJobTest {

  test("check split file") {
    val output = createTempPath()
    launch(SplitWikiFile, ThreeArgument("files/enwiki-sample.xml",SplitWikiFile.SerializationType.SequenceFile.toString,output))

    val lines = readFilesInDirectory(output, "part", StandardCharsets.ISO_8859_1)
     lines should have size(9698)
  }

}
