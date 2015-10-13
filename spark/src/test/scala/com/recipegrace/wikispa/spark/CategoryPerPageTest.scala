package com.recipegrace.wikispa.spark

import java.nio.charset.StandardCharsets

import com.recipegrace.biglibrary.electric.ElectricJobTest

/**
 * Created by Ferosh Jacob on 10/10/15.
 */
class CategoryPerPageTest extends ElectricJobTest {

  test("category per  page  test") {

    val output = createOutPutFile(false)
    launch(CategoryCount, Map("output" -> output , "input"-> "files/enwiki-sample.xml"))

    val lines = readFilesInDirectory(output, "part", StandardCharsets.ISO_8859_1)
    lines should contain("Objectivists\t1")
    lines should contain("Russian essayists\t1")
  }

}
