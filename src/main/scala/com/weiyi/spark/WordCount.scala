package com.weiyi.spark

import org.apache.spark._

/**
 * @author yuanyi
 */
object WordCount {

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("hadoop wordcount").setMaster("local[3]")
    val sc = new SparkContext(conf)
    val hadoopRdd = sc.textFile(args(0), 1)
    val result = hadoopRdd.flatMap(_.split("\\s+")).map(word=> (word, 1)).reduceByKey (_ + _).
      map(w=> (w._2,w._1)).sortByKey(false).map(pair=> (pair._2, pair._1))
    //result.saveAsTextFile(args(1))
    println( result.collect())
  }

}