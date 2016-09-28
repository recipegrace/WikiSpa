package com.recipegrace.wikispa.spark

import com.recipegrace.biglibrary.electric.{ElectricContext, ElectricJob, FileAccess}
/**
 * Created by Ferosh Jacob on 10/25/15.
 */
object SplitWikiFile extends ElectricJob[WikiFileAndSerialization] with  WikiAccess  with FileAccess {


  override def execute(argument:WikiFileAndSerialization)(implicit ec: ElectricContext)={


       val  wikiPages=   wikiXMLStreaming(argument.wikiFile)

        getSerializationType(argument.serializationType) match {
          case ObjectFile => wikiPages.saveAsObjectFile(argument.output)
          case SequenceFile => {
            wikiPages.map(f=> (1, f.toString()))
            .saveAsSequenceFile(argument.output)
          }
          case _ => wikiPages.saveAsTextFile(argument.output)
        }

  }
}
