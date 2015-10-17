package com.recipegrace.wikispa.spark

import com.recipegrace.biglibrary.electric.jobs.TwoArgument
import java.nio.charset.StandardCharsets

import com.recipegrace.biglibrary.electric.tests.SimpleJobTest

/**
 * Created by Ferosh Jacob on 10/10/15.
 */
class CategoryPerPageTest extends SimpleJobTest {

  test("category per  page  test") {

    val output = createTempPath()
    launch(CategoryCount, TwoArgument("files/enwiki-sample.xml",output))

    val lines = readFilesInDirectory(output, "part", StandardCharsets.ISO_8859_1)
    lines should contain("Objectivists\t1")
    lines should contain("Russian essayists\t1")
  }

}
