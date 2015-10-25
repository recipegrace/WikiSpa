package com.recipegrace.wikispa.spark

import java.nio.charset.StandardCharsets

import com.recipegrace.biglibrary.electric.jobs.TwoArgument
import com.recipegrace.biglibrary.electric.tests.SimpleJobTest

/**
 * Created by Ferosh Jacob on 10/10/15.
 */
class TitlePerPageTest extends SimpleJobTest {

  test("category per  page  test") {

    val output = createTempPath()
    launch(TitlePerPage, TwoArgument("files/enwiki-sample.xml",output))


    val lines = readFilesInDirectory(output, "part", StandardCharsets.ISO_8859_1)
    lines should contain("290\t-\tA")
  }

}
