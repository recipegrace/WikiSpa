package com.recipegrace.wikispa.spark

import java.nio.charset.StandardCharsets

import com.recipegrace.biglibrary.electric.tests.ElectricJobTest

/**
 * Created by Ferosh Jacob on 10/10/15.
 */
class LinksPerPageTest extends ElectricJobTest {



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


    runWikiJob("SequenceFile")
  }
  test("links per page test object") {



    runWikiJob("ObjectFile")
  }
  def runWikiJob(serializationType: String) = {
    val wikiOut = createTempPath()

    SplitWikiFile.runLocal(WikiFileAndSerialization("files/enwiki-sample.xml", serializationType, wikiOut))

    val redirects= createTempPath()
    val disambigs =createTempPath()
    val internalLinks = createTempPath()


    launch(LinksPerPage,AllLinksArgument(wikiOut,redirects,disambigs,internalLinks, serializationType.toString))

    testByLocalFile(redirects,disambigs,internalLinks)
  }
}
