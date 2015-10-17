package com.recipegrace.wikispa.spark

import com.recipegrace.biglibrary.electric.jobs.SimpleJob
import com.recipegrace.biglibrary.electric.{ElectricContext}
import com.recipegrace.wikispa.extractors.Categories


/**
 * Created by Ferosh Jacob on 10/10/15.
 */
object CategoryPerPage extends SimpleJob with WikiAccess{


  override def execute(input: String, output: String)(ec: ElectricContext)= {
    implicit val context = ec
    val categoriesCount =
      wikiXML(input)
        .map(f => Categories.extract(f).getOrElse((0L, List(): List[String])))

        .filter(f => f._1 != 0 && f._2.nonEmpty)
        .map(f=> f._1 + "\t"+f._2.mkString("\u0001"))

    writeFile(categoriesCount,output)

  }
}
