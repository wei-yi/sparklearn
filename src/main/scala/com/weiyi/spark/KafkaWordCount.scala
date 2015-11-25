package com.weiyi.spark

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.kafka.KafkaUtils

import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}

object KafkaWordCount{

  def main(args: Array[String]): Unit ={
    val zkQuorum = "localhost:2181"
    val groupId =  "1"
    val  topics = Map("test" -> 1)  //need to learn
    val conf = new SparkConf().setMaster("local[2]").setAppName("KafkaStreaming")
    val ssc = new StreamingContext(conf, Seconds(1))
    ssc.checkpoint("hdfs://localhost:9000/checkpoint")
    val kafkaStream = KafkaUtils.createStream(ssc, zkQuorum, groupId, topics, StorageLevel.MEMORY_AND_DISK_SER)
    val lines = kafkaStream.map(_._2)
    val words = lines.flatMap(_.split(" "))
    val wordcount = words.map(x =>(x, 1L)).reduceByKeyAndWindow(_ + _, _ - _, Minutes(1), Seconds(3), 2)
    wordcount.print()
    ssc.start()
    ssc.awaitTermination()

  }

}