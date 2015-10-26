package com.recipegrace.wikispa.spark

import com.recipegrace.biglibrary.electric.ElectricContext
import com.recipegrace.biglibrary.electric.jobs.TwoInputJob
/**
 * Created by Ferosh Jacob on 10/25/15.
 */
object SplitWikiFile extends TwoInputJob with WikiAccess {
   object SerializationType extends Enumeration {
     type SerializationType = Value
     val  SequenceFile,ObjectFile, TextFile=Value
     def parse(txt:String):SerializationType.SerializationType = txt match {

       case "SequenceFile" => SequenceFile
       case "ObjectFile" => ObjectFile
       case _=>TextFile
     }
   }


  override def execute(input: String, serializationType: String, output: String)(implicit ec: ElectricContext): Unit = {

       val serialization = SerializationType.parse(serializationType)

       val  wikiPages=   wikiXMLStreaming(input)

        serialization match {
          case SerializationType.ObjectFile => wikiPages.saveAsObjectFile(output)
          case SerializationType.SequenceFile => {
            wikiPages.map(f=> (1, f.toString()))
            .saveAsSequenceFile(output)
          }
          case _ => wikiPages.saveAsTextFile(output)
        }

  }
}
