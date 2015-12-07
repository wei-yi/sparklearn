package com.weiyi.test

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext

object TestSc {

  def fun(sc: SparkContext) = {
    val sqlContext = new SQLContext(sc)
    sqlContext.read.json("hdfs://localhost:9000/test.json")
  }

  case class A[C, B](f: C ⇒ B) {
    def apply(sc: C): B = f(sc)

  }

  def main(args: Array[String]): Unit = {
   val a = A { sc: SparkContext =>
      val sqlContext = new SQLContext(sc)
      println(0)
      sqlContext.read.json("hdfs://localhost:9000/inout1.json").collect().foreach(println)
    }
    a(sc = new SparkContext(new SparkConf().setAppName("test").setMaster("local[4]")))

  }

}