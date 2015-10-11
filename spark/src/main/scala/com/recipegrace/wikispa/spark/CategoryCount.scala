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
  org.apache.hadoop.mapred.FileInputFormat.addInputPaths(jobConf, "files/enwiki-sample.xml")

  // Load documents (one per line).
  val conf = new SparkConf().setAppName("Simple Application")
    conf.setMaster("local")
    val sc = new SparkContext(conf)
  val documents = sc.hadoopRDD(jobConf, classOf[org.apache.hadoop.streaming.StreamInputFormat],
    classOf[org.apache.hadoop.io.Text],
    classOf[org.apache.hadoop.io.Text])

    val texts = documents.map(_._1.toString)
      .map{ s =>
        val xml = XML.loadString(s)
        Categories.extract(xml)
      }
    .filter(f=> f.nonEmpty)
    .flatMap(f=> f.get._2.map(f=> (f,1)))
    .reduceByKey(_+_)
    println(documents)

  }

}
