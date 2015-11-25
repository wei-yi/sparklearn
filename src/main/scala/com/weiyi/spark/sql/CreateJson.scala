package com.weiyi.spark.sql

import java.sql.Timestamp
import java.util
import org.apache.spark.sql.{SaveMode, DataFrame, SQLContext, Row}
import org.apache.spark.sql.types.{StringType, StructField, StructType}


import scala.collection.JavaConversions._

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.io.{Text, LongWritable}
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat
import org.apache.spark.{SparkContext, SparkConf}

import scala.collection.immutable.Range
import scala.collection.mutable
case class Create_player(time: Int, ip: String, pid: Long, name: String)  //BigInt change long
case class Charge(pid: Long,time: Int,`type`: String, id: String, coins: Int )
case class Create_account(pid: Long, Account: String, time: Int, ip: String)
case class Login_logout(time: Int,`type`: Int,ip: String, pid: Long, level: Int,vip: Int)
case class Resource (pid: Long, coin_type: Int, after: Int, value: Int,`type`:Int, time: Int)

object CreateJson {

  val create_player = "create_player"
  val charge = "charge"
  val login_logout = "login_logout"
  val resource = "resource"

  val splitRegx = ","


  def  getFile(hdfsPath: String): util.LinkedList[String] = {
    val configuration  = new Configuration()
    val path = new Path(hdfsPath)
    val hdfs = path.getFileSystem(configuration)
    val  fileSttus =  hdfs.listStatus(path)
    val  allFiles: util.LinkedList[String] = new util.LinkedList[String]()
    for(file <- fileSttus ) {
      if(file.isFile) {
        allFiles.add(file.getPath.toString)
      }else {
       allFiles.addAll(getFile(file.getPath.toString))
      }
    }
    for(file <- allFiles ) {
      if (file.matches("hdfs://localhost:9000/10233/*/charge*")) {
        println("true")
      }

    }

    allFiles
  }

  def divideFiles(hdfsPath: String): mutable.Map[ String, util.List[String] ] = {
    val kindMap :mutable.Map[ String, util.List[String] ] = mutable.Map[ String, util.List[String]]()
    val  files = getFile(hdfsPath)

    for(i <- 0 to files.size() - 1) {
        if(files.get(i).contains(create_player)) {
              if(kindMap.contains(create_player)) {
                kindMap.get(create_player).get.add(files.get(i))
              }else {
                val list = new util.LinkedList[String]()
                list.add(files.get(i))
                kindMap.put(create_player,list)
              }
          } else if(files.get(i).contains(charge)) {
                if(kindMap.contains(charge)) {
                  kindMap.get(charge).get.add(files.get(i))
                 }else {
                   val list = new util.LinkedList[String]()
                   list.add(files.get(i))
                   kindMap.put(charge,list)
          }
        }else if(files.get(i).contains(login_logout)) {
          if(kindMap.contains(login_logout)) {
            kindMap.get(login_logout).get.add(files.get(i))
          }else {
            val list = new util.LinkedList[String]()
            list.add(files.get(i))
            kindMap.put(login_logout,list)
          }
        }else if(files.get(i).contains(resource)) {
          if(kindMap.contains(resource)) {
            kindMap.get(resource).get.add(files.get(i))
          }else {
            val list = new util.LinkedList[String]()
            list.add(files.get(i))
            kindMap.put(resource,list)
          }
        }
    }
    kindMap
  }

  def  addOrSub(num: Int): String ={
   var result: String = null
     if(num >= 0 ){
     result = "add"
    }else {
       result = "sub"
    }
    result
   }

  def isFilePath(path1: String): Boolean = {
    val configuration  = new Configuration()
    val path = new Path(path1)
    //val fs = FileSystem.get(configuration)
    val hdfs = path.getFileSystem(configuration)
    hdfs.exists(path)

  }


  def createJson(hdfsPath: String, outPutPath: String): Unit = {

    val  kindMap =  divideFiles(hdfsPath)
    val conf = new SparkConf().setMaster("local[4]").setAppName("create Json")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    import sqlContext.implicits._
    kindMap.map { kv => {
      if (kv._1.equals(create_player)) {
        for (filePath <- kv._2) {
          val create_player_Line= sc.textFile(filePath)
          //val rowRDD = create_player_tableField.map(_.split(splitRegx)).map { fields => Row(fields(0).trim.toInt, fields(1), fields(2).trim.toInt, fields(3)) }

          //val create_player_DF = sqlContext.createDataFrame(rowRDD, create_player_schema)
         val create_player_table = create_player_Line.map(_.split(splitRegx)).map(key => Create_player(key(0).trim.toInt,key(1),key(2).trim().toLong,key(3))).toDF()
          create_player_table.write.mode(SaveMode.Append).json(outPutPath + "/" + create_player)
          //create_player_DF.save(outPutPath + "/" + create_player, SaveMode.Append)

        }
      }else if(kv._1.equals(charge)){
        for (filePath <- kv._2) {
          val charge_Line = sc.textFile(filePath)
          val charge_table = charge_Line.map(_.split(splitRegx)).map(key => Charge(key(0).trim.toLong,key(1).trim.toInt,key(2),key(3),key(4).trim.toInt)).toDF()
          charge_table.write.mode(SaveMode.Append).json(outPutPath + "/" + charge)
        }
      }else if(kv._1.equals(login_logout)){
        for (filePath <- kv._2) {
          val login_logout_Line = sc.textFile(filePath)
          val login_logout_table = login_logout_Line.map(_.split(splitRegx)).map(key => Login_logout(key(0).trim.toInt,key(1).trim.toInt,key(2),key(3).trim.toLong,key(4).trim.toInt,key(5).trim.toInt)).toDF()
          login_logout_table.write.mode(SaveMode.Append).json(outPutPath + "/" + login_logout)
        }
      }else if(kv._1.equals(resource)){
        for (filePath <- kv._2) {
          val resource_Line = sc.textFile(filePath)
          val  resource_table_DF = resource_Line.map(_.split(splitRegx)).map(key => Resource(key(0).trim.toLong,key(1).trim.toInt,(key(2).trim.toInt + key(3).trim.toInt),key(3).trim.toInt,key(4).trim.toInt,key(5).trim.toInt)).toDF()
          sqlContext.udf.register("addOrSub", addOrSub _)
          resource_table_DF.registerTempTable("resource_table")
         val result = sqlContext.sql("select addOrSub(value) as addOrSub, * from resource_table")
          result.write.format("json").mode(SaveMode.Append).partitionBy("addOrSub").save(outPutPath + "/" + resource)
        }
      }
    }
    }
  }

  def main (args: Array[String]) {
//    val job = new Job()
//    job.setOutputFormatClass(classOf[TextOutputFormat[LongWritable,Text]])
//    job.getConfiguration().set("mapred.output.compress", "true")
//    job.getConfiguration().set("mapred.output.compression.codec", "com.hadoop.compression.lzo.LzopCodec")
//    val conf = new SparkConf().setMaster("learn lzo")
//    val  sc = new SparkContext(conf)
//    val textFile = sc.newAPIHadoopFile(args(0), classOf[LzoTextInputFormat],classOf[LongWritable], classOf[Text],job.getConfiguration())
//    textFile.saveAsNewAPIHadoopFile(args(1), classOf[LongWritable], classOf[Text],classOf[TextOutputFormat[LongWritable,Text]],job.getConfiguration())
 //val  files =  createJson("hdfs://localhost:9000/10233","hdfs://localhost:9000/createJson")
    val  ispath = isFilePath("hdfs://localhost:9000/10233/10233/charge*")
    println(ispath)
// println(file.getPath.getName)
//    for(file <- files ) {
//      println(file)
//    }


  }
}