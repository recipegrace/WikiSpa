package com.recipegrace.wikispa.spark

import com.recipegrace.biglibrary.electric.jobs.{ThreeArgument, TwoInputJob}
import com.recipegrace.biglibrary.electric.tests.TwoInputJobTest
import com.recipegrace.wikispa.spark.SplitWikiFile.SerializationType

/**
 * Created by Ferosh Jacob on 10/25/15.
 */
trait BaseWikiJobTest extends TwoInputJobTest{

  def testByLocalFile(output: String)

  def runWikiJob(twoInputJob: TwoInputJob, serializationType: SerializationType.SerializationType) = {
    val wikiOut = createTempPath()

    launch(SplitWikiFile, ThreeArgument("files/enwiki-sample.xml", serializationType.toString, wikiOut))

    val output = createTempPath()

    launch(twoInputJob, ThreeArgument(wikiOut, serializationType.toString, output))

    testByLocalFile(output)
  }

}
