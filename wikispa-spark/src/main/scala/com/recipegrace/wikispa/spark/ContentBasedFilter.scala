package com.recipegrace.wikispa.spark

import com.recipegrace.biglibrary.electric.ElectricContext
import com.recipegrace.biglibrary.electric.jobs.{TwoInputJob, SimpleJob}
import com.recipegrace.wikispa.extractors.{Content, Categories}

/**
  * Created by Ferosh Jacob on 5/29/16.
  */
object ContentBasedFilter extends TwoInputJob with WikiAccess  {


  override def execute(input: String,serializationType:String,output: String)(implicit ec: ElectricContext)={
    val categoriesCount =
      wikiPages(input, "")
        .map(f => f.title.decoded->Content.extractByPage(f).map(f=> f._2.replace("\n", "").replace("\r", "")))
        .filter(f=> f._2.exists(g=> g.length() >500 && g.substring(0,500).contains(" or ")))
        .map(f=> f._1 -> f._2.get.substring(0,500))
    writeFile(categoriesCount,output)

  }
}
