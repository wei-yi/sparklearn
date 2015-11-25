package com.weiyi.abst

import org.apache.spark.{SparkConf, SparkContext}

/**
 *
 * @param g
 * @tparam C
 * @tparam A
 */
case class DataTransform[C, A](g: C => A){
  def apply(sc: C): A ={ println("----------")
    g(sc)}

  def map[B](f: A ⇒ B): DataTransform[C, B] = DataTransform(sc ⇒ f(g(sc)))

  def flatMap[B](f: A ⇒ DataTransform[C, B]): DataTransform[C, B] = DataTransform(sc ⇒ f(g(sc))(sc))
}

object test {

  def main(args: Array[String]): Unit = {
    val transform = DataTransform { sc: SparkContext ⇒ sc.textFile("hdfs://localhost:9000/inout.json")
    }.flatMap(rs ⇒ DataTransform(sc ⇒ rs.map(_ + "***")))
    val sc = new SparkContext(new SparkConf().setAppName("aaa").setMaster("local[4]"))
   val rdd = transform(sc).collect()
    rdd.foreach(println)
}
}