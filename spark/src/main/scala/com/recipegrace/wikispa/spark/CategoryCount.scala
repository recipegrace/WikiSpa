package com.recipegrace.wikispa.spark

import com.recipegrace.biglibrary.core.Argument
import com.recipegrace.biglibrary.electric.{SequentialFileAccess, ElectricContext, ElectricJob}
import com.recipegrace.wikispa.extractors.Categories

/**
 * Created by Ferosh Jacob on 10/10/15.
 */
object CategoryCount extends ElectricJob with WikiAccess with SequentialFileAccess {
  val outputArgument = Argument("output")
  val inputArgument = Argument("input")

  override  val namedArguments = Set(inputArgument,outputArgument)
  override def job(args: Map[Argument, String], sc: ElectricContext): Unit = {

    implicit val context=sc
    val categoriesCount =
      wikiPages(args(inputArgument))
        .map(f => Categories.extractByPage(f).getOrElse((0L, List(): List[String])))

        .filter(f => f._1 != 0 && f._2.nonEmpty)
        .flatMap(f => f._2.map(f => (f, 1)))
        .reduceByKey(_ + _)
        .map(f=> f._1 + "\t"+f._2)

    writeFile(categoriesCount, args(outputArgument))

  }


}
