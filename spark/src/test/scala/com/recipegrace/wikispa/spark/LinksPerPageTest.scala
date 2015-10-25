package com.recipegrace.wikispa.spark

import java.nio.charset.StandardCharsets

import com.recipegrace.biglibrary.electric.jobs.TwoArgument
import com.recipegrace.biglibrary.electric.tests.{ElectricJobTest, SimpleJobTest}

/**
 * Created by Ferosh Jacob on 10/10/15.
 */
class LinksPerPageTest extends ElectricJobTest[AllLinksArgument] {

  test("category per  page  test") {

    val redirects= createTempPath()
    val disambigs =createTempPath()
    val internalLinks = createTempPath()
    launch(LinksPerPage, AllLinksArgument("files/enwiki-sample.xml",redirects,disambigs,internalLinks))

    val lines = readFilesInDirectory(redirects, "part", StandardCharsets.ISO_8859_1)
    lines should contain("10\tAccessibleComputing\tComputer_accessibility")

    val lines1 = readFilesInDirectory(disambigs, "part", StandardCharsets.ISO_8859_1)
    lines1 should  have size(1)

    val lines2 = readFilesInDirectory(internalLinks, "part", StandardCharsets.ISO_8859_1)
    lines2 should  have size(18)
  }

}
