package com.weiyi.spark.sql

import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.{SparkContext, SparkConf}


/**
 * Created by yuanyi on 15/10/20.
 */
object HdfsOP {

  def readAndWirte(inputPath: String, outputPath: String): Unit = {
    val conf  = new SparkConf().setAppName("test").setMaster("local[4]")
    val sc  = new SparkContext(conf)
    val  accountRDD = sc.wholeTextFiles(inputPath + "/*/" + "create_account*").flatMap{ case (path, content) => {
      val dir = path.split("/").takeRight(2)(0)
      content.split("\n").map(_ + "," + dir)
      //val test = Map("pid" -> "Int", "value" -> "Int", "account"-> "String")
    }
    }

    accountRDD.foreach(println)
   // accountRDD.saveAsTextFile(outputPath + "/" + "account")
  }
  val test = Map("pid" -> "Int", "value" -> "Int", "account"-> "String")

  case class Account(test: Int)
  def main(args :Array[String]): Unit = {
    readAndWirte("hdfs://localhost:9000/dddd", "hdfs://localhost:9000/output")
  }
}
