package com.weiyi.test

import org.apache.hadoop.mapred.InvalidInputException
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext
import org.apache.spark.{Logging, SparkConf, SparkContext}


object TestScala extends Logging{

   def funcation(name: String, age: Int = 1): Unit = {
       println("name: " + name + " age: " + age)
   }
   case class Create_player(time: Int, ip: String, pid: Long, name: String)

   def main(String: Array[String]): Unit = {
     val conf = new SparkConf().setAppName("create Json").setMaster("local[4]")
     val sc = new SparkContext(conf)
     var line :RDD[String]= null
     try {
       val i : Int = 999999999
       val aaa : Long = 140720308486164l
       val a = "999999999"
       println("max Int " +Int.MaxValue)
       println("max Long " +Long.MaxValue)
      logInfo("lalalal logger")
       val sqlContext = new SQLContext(sc)
       import sqlContext.implicits._

       line = sc.textFile("hdfs://localhost:9000/test.json").unpersist(true)
       val create_player_table_DF = line.map(_.split(",")).map(key => Create_player(key(0).trim.toInt,key(1),key(2).trim().toLong,key(3))).toDF()
       //val create_player_table_DF1 = line.map(_.split(",")).map(key => Create_player(key(0).trim.toInt+10,key(1),key(2).trim().toLong,key(3))).toDF()
       //val newDF = create_player_table_DF.unionAll(create_player_table_DF1)
       line.foreach(println)
     } catch {

       case e: InvalidInputException => println("path doest exist")
     }

     println("xx.aa.xx.aa".matches("((\\w+)\\.(\\w+))+"))
     println("s3://big-data/primitive/kssg/item/ym=201101".matches("s3://big-data/primitive/kssg/item"))
   }

 }