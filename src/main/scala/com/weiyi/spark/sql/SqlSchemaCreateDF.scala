package com.weiyi.spark.sql

import org.apache.spark.sql.{SQLContext, Row}
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.sql.types.{StringType, StructField, StructType}

object SqlSchemaCreateDF {

  def main(args :Array[String]): Unit = {

    val conf = new SparkConf().setAppName("schema").setMaster("local[4]")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    val  schemaString = "pid,value,account"
    val schema =
      StructType(schemaString.split(",").map(fieldName => StructField(fieldName, StringType, true)))
    val tableRdd = sc.textFile("/Users/yuanyi/bigdata/testData/user.txt")
    val rowRDD = tableRdd.map(_.split(" ")).map(p => Row(p(0).trim.toInt, p(1).trim.toInt,p(2)))
    val UserFrame = sqlContext.createDataFrame(rowRDD, schema)
    UserFrame.show()
  }
}
