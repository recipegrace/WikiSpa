package com.recipegrace.wikispa.spark

import java.nio.charset.StandardCharsets

import com.recipegrace.biglibrary.electric.tests.SimpleJobTest

/**
 * Created by Ferosh Jacob on 10/10/15.
 */
import com.recipegrace.biglibrary.electric.jobs.TwoArgument
class CategoryCountTest extends SimpleJobTest {

  test("category count test") {


      val output = createTempPath()
      launch(CategoryCount, TwoArgument(  "files/enwiki-sample.xml", output))

      val lines = readFilesInDirectory(output, "part", StandardCharsets.ISO_8859_1)
      lines should contain("Objectivists\t1")
      lines should contain("Russian essayists\t1")
    }

}
