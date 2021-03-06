package com.recipegrace.wikispa.spark

import java.nio.charset.StandardCharsets

/**
 * Created by Ferosh Jacob on 10/10/15.
 */
class CategoryPerPageTest extends BaseWikiJobTest {


  def testByLocalFile(output: String): Unit = {

    val lines = readFilesInDirectory(output, "part", StandardCharsets.ISO_8859_1)
    lines should contain("290\tISO basic Latin letters\u0001Vowel letters")
  }

  test("category per page test") {



    val output= createTempPath()

    launch(CategoryPerPage, WikiFileAndSerialization(  "files/enwiki-sample.xml","", output))

    testByLocalFile(output)

  }
  test("category per page test sequence") {


    runWikiJob(CategoryPerPage, "SequenceFile")
  }
  test("category per page test object") {



    runWikiJob(CategoryPerPage, "ObjectFile")
  }

}
