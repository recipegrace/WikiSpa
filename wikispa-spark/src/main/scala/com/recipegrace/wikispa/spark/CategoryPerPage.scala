package com.recipegrace.wikispa.spark

import com.recipegrace.biglibrary.electric.{ElectricContext, ElectricJob, FileAccess}
import com.recipegrace.wikispa.extractors.Categories


/**
 * Created by Ferosh Jacob on 10/10/15.
 */
object CategoryPerPage extends ElectricJob[WikiFileAndSerialization] with  WikiAccess  with FileAccess {


  override def execute(argument:WikiFileAndSerialization)(implicit ec: ElectricContext)={
   val categoriesCount=   wikiPages(argument.wikiFile, argument.serializationType)
        .map(f => Categories.extractByPage(f).getOrElse((0L, List(): List[String])))

        .filter(f => f._1 != 0 && f._2.nonEmpty)
        .map(f=> f._1 + "\t"+f._2.mkString("\u0001"))

    writeFile(categoriesCount,argument.output)

  }
}
