package com.recipegrace.wikispa.spark

import com.recipegrace.biglibrary.electric.ElectricJob
import com.recipegrace.biglibrary.electric.tests.ElectricJobTest
import com.recipegrace.wikispa.spark.SplitWikiFile.SerializationType

/**
 * Created by Ferosh Jacob on 10/25/15.
 */
trait BaseWikiJobTest extends ElectricJobTest{

  def testByLocalFile(output: String)

  def runWikiJob(twoInputJob: ElectricJob[WikiFileAndSerialization], serializationType: SerializationType) = {
    val wikiOut = createTempPath()

    launch(SplitWikiFile, WikiFileAndSerialization("files/enwiki-sample.xml", serializationType.toString, wikiOut))

    val output = createTempPath()

    launch(twoInputJob, WikiFileAndSerialization(wikiOut, serializationType.toString, output))

    testByLocalFile(output)
  }

}
