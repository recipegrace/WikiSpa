package com.recipegrace.wikispa.spark

import com.recipegrace.biglibrary.electric.jobs.SimpleJob
import com.recipegrace.biglibrary.electric.{ElectricContext}
import com.recipegrace.wikispa.extractors.Categories

/**
 * Created by Ferosh Jacob on 10/10/15.
 */
object CategoryCount extends SimpleJob with WikiAccess  {


  override def execute(input: String, output: String)(ec: ElectricContext)={
    implicit val context = ec
    val categoriesCount =
      wikiPages(input)
        .map(f => Categories.extractByPage(f).getOrElse((0L, List(): List[String])))

        .filter(f => f._1 != 0 && f._2.nonEmpty)
        .flatMap(f => f._2.map(f => (f, 1)))
        .reduceByKey(_ + _)
        .map(f=> f._1 + "\t"+f._2)

    writeFile(categoriesCount, output)
  }
}
