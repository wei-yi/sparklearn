package com.weiyi.spark.sql

import org.apache.spark.sql.catalyst.plans.JoinType
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.sql.SQLContext

object SparkSQLAPI{

  val conf = new SparkConf().setAppName("Streaming api").setMaster("local[4]")
  val jsonPath = "hdfs://localhost:9000/test.json"
  val sc = new SparkContext(conf)

  def firstJson(): Unit = {

    val sqlContext = new SQLContext(sc)
    val  jsonRDD = sc.textFile(jsonPath)
    val  df = sqlContext.read.json(jsonRDD).as("table")
    val  df1 = sqlContext.read.json("hdfs://localhost:9000/test.json").as("Table1")
    df.registerTempTable("t1")
    df1.registerTempTable("t2")
    println("df-------------------------------")
   // df.show()
    println("df1-------------------------------")
   // df1.show()
    df.printSchema()
   // df.select("sid").show()
  //  df.select(df("sid") + 1).show()
   // df.groupBy("sid").count().show()
//    df.coalesce(4).write.partitionBy("sid").json("hdfs://localhost:9000/json/test")

    //join//and  df("pid") !== df1("pid")

    val joinResult = df.join(df1, (df("coins") === df1("coins") ) and (df("pid") !== df1("pid")),"inner").select( df("sid")).distinct().show()
    val  result = sqlContext.sql("select count(*) as  numcoins , t1.coins from t1,t2 where t1.coins = t2.coins  and t1.pid != t2.pid group by t1.coins ").show()
  }

  case class pair(cid: Int, sid: Int)

  def seconderJson(): Unit = {

    val sqlContext = new SQLContext(sc)
    import sqlContext.implicits._
    val  pair_df = sc.textFile(jsonPath).map(_.split(",")).map(p => pair(p(0).trim.toInt,p(1).trim.toInt)).toDF().show()
  }

  def hiveSql(): Unit = {
   // val hiveContext = new org.apache.spark.sql.hive.HiveContext(sc)
  }

  def main(args: Array[String]): Unit = {
      firstJson()
    //seconderJson
  }
}