package com.recipegrace.wikispa.spark

import com.recipegrace.wikispa.extractors.Categories
import org.apache.hadoop.mapred.JobConf
import org.apache.spark.{SparkContext, SparkConf}
import scala.xml.XML
/**
 * Created by Ferosh Jacob on 10/10/15.
 */
object CategoryCount {


  def main(args:Array[String])= {
  val jobConf = new JobConf()
  jobConf.set("stream.recordreader.class",
    "org.apache.hadoop.streaming.StreamXmlRecordReader")
  jobConf.set("stream.recordreader.begin", "<page")
  jobConf.set("stream.recordreader.end", "</page>")
  org.apache.hadoop.mapred.FileInputFormat.addInputPaths(jobConf, "/Users/associate/wiki/enwiki-20151002-pages-articles-multistream.xml")

  // Load documents (one per line).
  val conf = new SparkConf().setAppName("Simple Application")
    conf.setMaster("local")
    val sc = new SparkContext(conf)
  val documents = sc.hadoopRDD(jobConf, classOf[org.apache.hadoop.streaming.StreamInputFormat],
    classOf[org.apache.hadoop.io.Text],
    classOf[org.apache.hadoop.io.Text])

    documents.map(_._1.toString)
      .map{ s =>
        val xml = XML.loadString(s)
        val modifiedXML = <mediawiki>{xml}</mediawiki>
        Categories.extract(modifiedXML).getOrElse((0L,List():List[String]))

      }
    .filter(f=> f._1!=0)
    .flatMap(f=> f._2.map(g=>(g,1)))
    .aggregateByKey(0)((p,q)=> p+1, _+_)
    .map(f=> f._2+"\t"+ f._1)
    .saveAsTextFile(".tests/out2")

  }

}
