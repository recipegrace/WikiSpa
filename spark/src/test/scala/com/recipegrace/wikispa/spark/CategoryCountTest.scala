package com.recipegrace.wikispa.spark

import com.recipegrace.biglibrary.electric.ElectricJobTest

/**
 * Created by Ferosh Jacob on 10/10/15.
 */
class CategoryCountTest extends ElectricJobTest {

  test("wordcount test with spark") {
/*
    val input = createOutPutFile()
    createFile("hello world", input)
    val output = createOutPutFile(false)
    //launch(CategoryCount, Map("input" -> input, "output" -> output))

    val lines = readFilesInDirectory(output, "part")
    lines should contain("hello\t1")
    lines should contain("world\t1")
  */
    CategoryCount.main(Array())
  }

}
