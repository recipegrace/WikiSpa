package com.recipegrace.wikispa.spark

import com.recipegrace.biglibrary.electric.{ElectricContext, ElectricJob, FileAccess}
import com.recipegrace.wikispa.extractors.Categories

/**
 * Created by Ferosh Jacob on 10/10/15.
 */

case class WikiFileAndSerialization(wikiFile:String, serializationType:String, output:String)
object CategoryCount extends ElectricJob[WikiFileAndSerialization] with  WikiAccess  with FileAccess {


  override def execute(argument:WikiFileAndSerialization)(implicit ec: ElectricContext)={
    val categoriesCount =
      wikiPages(argument.wikiFile, argument.serializationType)
        .map(f => Categories.extractByPage(f).getOrElse((0L, List(): List[String])))

        .filter(f => f._1 != 0 && f._2.nonEmpty)
        .flatMap(f => f._2.map(f => (f, 1)))
        .reduceByKey(_ + _)
        .map(f=> f._1 + "\t"+f._2)

    writeFile(categoriesCount, argument.output)
  }
}
