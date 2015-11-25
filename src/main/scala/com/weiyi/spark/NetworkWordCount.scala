package com.weiyi.spark

import org.apache.spark._
import org.apache.spark.streaming.{Seconds, StreamingContext}

object NetworkWordCount {

  def init() {
    val conf = new SparkConf().setMaster("local[2]").setAppName("NetWorkWordcountCount")
    val ssc = new StreamingContext(conf, Seconds(1))
    val lines = ssc.socketTextStream("localhost", 9999)
    val result = lines.flatMap(_.split(" ")).map(word => (word,1)).reduceByKey(_ + _)
    result.print()
    ssc.start()
    ssc.awaitTermination()
  }

  def main(args: Array[String]){
    init
  }
}