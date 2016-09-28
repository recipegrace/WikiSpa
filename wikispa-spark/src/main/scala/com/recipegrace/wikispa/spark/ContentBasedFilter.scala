package com.recipegrace.wikispa.spark

import com.recipegrace.biglibrary.electric.{ElectricContext, ElectricJob, FileAccess}
import com.recipegrace.wikispa.extractors.Content

/**
  * Created by Ferosh Jacob on 5/29/16.
  */
object ContentBasedFilter extends ElectricJob[WikiFileAndSerialization] with  WikiAccess  with FileAccess {


  override def execute(argument:WikiFileAndSerialization)(implicit ec: ElectricContext)={
    val categoriesCount =
      wikiPages(argument.wikiFile, "")
        .map(f => f.title.decoded->Content.extractByPage(f).map(f=> f._2.replace("\n", "").replace("\r", "")))
        .filter(f=> f._2.exists(g=> g.length() >500 && g.substring(0,500).contains(" or ")))
        .map(f=> f._1 -> f._2.get.substring(0,500))
    writeFile(categoriesCount,argument.output)

  }
}
