package com.recipegrace.wikispa.spark

import com.recipegrace.biglibrary.electric.ElectricContext
import com.recipegrace.biglibrary.electric.jobs.TwoInputJob
/**
 * Created by Ferosh Jacob on 10/25/15.
 */
object SplitWikiFile extends TwoInputJob with WikiAccess {



  override def execute(input: String, serializationType: String, output: String)(implicit ec: ElectricContext): Unit = {



       val  wikiPages=   wikiXMLStreaming(input)

        getSerializationType(serializationType) match {
          case ObjectFile => wikiPages.saveAsObjectFile(output)
          case SequenceFile => {
            wikiPages.map(f=> (1, f.toString()))
            .saveAsSequenceFile(output)
          }
          case _ => wikiPages.saveAsTextFile(output)
        }

  }
}
