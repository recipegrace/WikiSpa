package com.recipegrace.wikispa.spark

import java.nio.charset.StandardCharsets

import com.recipegrace.biglibrary.core.BaseTest
import com.recipegrace.biglibrary.electric.jobs.{TwoInputJob, ThreeArgument, TwoArgument}
import com.recipegrace.biglibrary.electric.tests.{ElectricJobTest, SimpleJobTest}
import com.recipegrace.wikispa.spark.SplitWikiFile.SerializationType

/**
 * Created by Ferosh Jacob on 10/10/15.
 */
class LinksPerPageTest extends ElectricJobTest[AllLinksArgument] {



  def testByLocalFile(redirects: String, disambigs:String,internalLinks:String): Unit = {


    val lines = readFilesInDirectory(redirects, "part", StandardCharsets.ISO_8859_1)
    lines should contain("10\tAccessibleComputing\tComputer_accessibility")

    val lines1 = readFilesInDirectory(disambigs, "part", StandardCharsets.ISO_8859_1)
    lines1 should  have size(1)

    val lines2 = readFilesInDirectory(internalLinks, "part", StandardCharsets.ISO_8859_1)
    lines2 should  have size(18)
  }

  test("links per page test") {

    val redirects= createTempPath()
    val disambigs =createTempPath()
    val internalLinks = createTempPath()


    launch(LinksPerPage,AllLinksArgument("files/enwiki-sample.xml",redirects,disambigs,internalLinks, ""))

    testByLocalFile(redirects,disambigs,internalLinks)

  }
  test("links per page test sequence") {


    runWikiJob(SerializationType.SequenceFile)
  }
  test("links per page test object") {



    runWikiJob(SerializationType.ObjectFile)
  }
  def runWikiJob(serializationType: SerializationType.SerializationType) = {
    val wikiOut = createTempPath()

    SplitWikiFile.runLocal(ThreeArgument("files/enwiki-sample.xml", serializationType.toString, wikiOut))

    val redirects= createTempPath()
    val disambigs =createTempPath()
    val internalLinks = createTempPath()


    launch(LinksPerPage,AllLinksArgument(wikiOut,redirects,disambigs,internalLinks, serializationType.toString))

    testByLocalFile(redirects,disambigs,internalLinks)
  }
}