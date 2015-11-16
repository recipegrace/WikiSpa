package com.recipegrace.wikispa.spark

import com.recipegrace.biglibrary.electric.{ElectricContext, ElectricJob}
import com.recipegrace.wikispa.extractors.{Pages, Categories}
import org.apache.hadoop.io.{Text, LongWritable}
import org.apache.hadoop.mapred.JobConf
import org.apache.spark.rdd.RDD
import org.dbpedia.extraction.sources.WikiPage

import scala.xml.{XML, Elem}

/**
 * Created by Ferosh Jacob on 10/12/15.
 */
trait WikiAccess {


  def wikiXMLStreaming(input:String)(implicit ec: ElectricContext): RDD[Elem] = {
    val jobConf = new JobConf()
    jobConf.set("stream.recordreader.class",
      "org.apache.hadoop.streaming.StreamXmlRecordReader")
    jobConf.set("stream.recordreader.begin", "<page")
    jobConf.set("stream.recordreader.end", "</page>")
    org.apache.hadoop.mapred.FileInputFormat.addInputPaths(jobConf, input)
    val documents = ec.sparkContext.hadoopRDD(jobConf, classOf[org.apache.hadoop.streaming.StreamInputFormat],
      classOf[org.apache.hadoop.io.Text],
      classOf[org.apache.hadoop.io.Text])

    documents.map(_._1.toString)
      .map{ s =>
        val xml = XML.loadString(s)
        <mediawiki>{xml}</mediawiki>
      }
  }
  private def loadWikiPageFromXML(xml:RDD[Elem]):RDD[WikiPage] = {
    xml.map(Pages.extractWikiPage)
      .filter(f=> f.nonEmpty)
      .map(f=> f.get)
  }
  def wikiPagesStreaming(input:String)(implicit ec: ElectricContext): RDD[WikiPage] = {

    loadWikiPageFromXML(wikiXMLStreaming(input))
  }

  def wikiPages(input:String,serialization:String)(implicit ec: ElectricContext): RDD[WikiPage] = {

     serialization match {
       case  "SequenceFile" => {

       val xmlRDD=
         ec.sparkContext.sequenceFile[LongWritable, Text](input)
         .map(f=> f._2)
         .map(f=>XML.loadString(f.toString))

         loadWikiPageFromXML(xmlRDD)
       }
       case "ObjectFile" => {
         val xmlRDD=ec.sparkContext.objectFile[Elem](input)
         loadWikiPageFromXML(xmlRDD)
       }
       case _ =>{
         wikiPagesStreaming(input)
       }
     }

  }

}
