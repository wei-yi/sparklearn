//package com.xindong
//
//import org.apache.spark.rdd.RDD
//import org.apache.spark.{SparkContext, SparkConf}
//import org.apache.spark.sql.{DataFrame, SQLContext}
//import org.apache.spark.sql.functions._
//
//
//case class MonthSnapshot(uuid: String, before: Long, after: Long,game: String, ym: Long) //int change to long
//
//
//object SnapShotBuilder {
//
//  def createMonthSnapshot(cIO: CurrencyInOut): MonthSnapshot = {
//     MonthSnapshot(cIO.uuid,cIO.before,cIO.after,cIO.game,cIO.ym)
//  }
//
//  def createMonthSnapshot(sqlContext: SQLContext) = {
//
//    import sqlContext.implicits._
//
//    var uuid: String = null
//    var before: Long= 0l
//    var after: Long = 0l
//    var game: String = null
//    var ym: Long= 0
//
//    val df = sqlContext.read.json("hdfs://localhost:9000/inout.json").toDF()
//
//    df.select(df("uuid"),df("before"),df("after"),df("game"),df("ym")).orderBy("uuid","game","ym").groupBy("uuid")
//      df.groupBy("").agg(first(""), last(""))
//      .map{ row =>
//      var result: MonthSnapshot = null
//      if(uuid == null) {
//        uuid = row.getAs[String]("uuid")
//        before = row.getAs[Long]("before")
//        after =  row.getAs[Long]("after")
//        game = row.getAs[String]("game")
//        ym = row.getAs[Long]("ym")
//
//      }else if ( uuid != row.getAs[String]("uuid") ||
//        game != row.getAs[String]("game") || ym != row.getAs[Long]("ym")){
//        result = MonthSnapshot(uuid,before,after,game,ym)
//        uuid = row.getAs[String]("uuid")
//        before = row.getAs[Long]("before")
//        after =  row.getAs[Long]("after")
//        game = row.getAs[String]("game")
//        ym = row.getAs[Long]("ym")
//      }else {
//        after =  row.getAs[Long]("after")
//      }
//      result
//    }.filter(_ != null)
//
//  }
//
//
//  def main(args : Array[String]): Unit = {
//   val rdd =  createMonthSnapshot({
//     val conf = new SparkConf().setAppName("createMonthSnapshot").setMaster("local[4]")
//     val sc = new SparkContext(conf)
//     new SQLContext(sc)
//   }).cache()
//
//    rdd.saveAsTextFile("hdfs://localhost:9000/inout")
//    rdd.foreach(println)
//  }
//
//}

package com.weiyi

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.sql.SQLContext


case class MonthSnapshot(uuid: String, before: Long, after: Long,game: String, ym: Int)

object SnapShotBuilder {

  def check(ba: Array[String]): Unit = {
    for( i <- 0 to (ba.length - 3); if(i%2 !=0)) {
      require(ba(i) == ba(i+1), s"before  after(${ba(i)}) must be follow'  before(${ba(i+1)})")
    }
  }

  def createMonthSnapshot(rdd: RDD[CurrencyInOut]) = {rdd
      .map(r => (r.game+ "|"+r.uuid+"|"+r.ym+"-"+r.unixTime, r.before+"|"+r.after+"|")).sortByKey()
      .map(kv => (kv._1.split("-")(0),kv._2)).reduceByKey(_+_)
      .map { kv =>
         val kSplits = kv._1.split("\\|")
         val vSplits = kv._2.split("\\|")
         MonthSnapshot(kSplits(1), vSplits(0).toLong, vSplits(vSplits.length - 1).toLong, kSplits(0), kSplits(2).toInt)
    }
  }

  def createMonthSnapshot1(rdd: RDD[CurrencyInOut]) = {rdd
    .groupBy(cIO => cIO.game+cIO.uuid+cIO.ym)
     .map{it => it._2.toArray.sortBy(c => c.unixTime)}
     .map{key =>
           val first = key(0)
           val last = key(key.length - 1)
           MonthSnapshot(first.uuid, first.before, last.after, first.game, first.ym)
     }
  }

  def createMonthSnapshot2(rdd: RDD[CurrencyInOut]) = {rdd
    .groupBy(cIO => cIO.game + cIO.uuid)
    .map{value => value._2
           .groupBy(cIO => cIO.ym)
           .map{it => it._2.toArray
               .sortBy(cIO => cIO.unixTime)
           }
          .map{key =>
            val first = key(0)
            val last = key(key.length - 1)
            MonthSnapshot(first.uuid, first.before, last.after, first.game, first.ym)
          }
      }
  }

  def buildMonthSnapshot(rdd: RDD[CurrencyInOut]): RDD[MonthSnapshot] = {
    rdd.groupBy(cIO => (cIO.ym, cIO.game, cIO.uuid)).map { case ((ym, game, uuid), cIOs) =>
      val first = cIOs.minBy(_.unixTime)
      val last = cIOs.maxBy(_.unixTime)
      MonthSnapshot(uuid, first.before, last.after, game, ym)
    }
  }

  def main(args : Array[String]): Unit = {
    val sqlContext = {
      val conf = new SparkConf().setAppName("createMonthSnapshot").setMaster("local[4]")
      val sc = new SparkContext(conf)
      new SQLContext(sc)
    }

    val  currencyInOutRDD = sqlContext.read.json("hdfs://localhost:9000/inout1.json").toDF()
      .map(r => CurrencyInOut(r.getAs[String]("uuid"),r.getAs[Long]("unixTime"),r.getAs[Long]("before"),
      r.getAs[Long]("change"),r.getAs[Long]("after"),r.getAs[String]("variety"),r.getAs[Long]("kind").toInt,
      r.getAs[String]("game"),r.getAs[Long]("ym").toInt))

    //val rdd =  buildMonthSnapshot(currencyInOutRDD)
    //currencyInOutRDD.foreach(println)
    val rdd = CurrencyInOutValidation.findValidCurrencyInOut(currencyInOutRDD).cache().collect()
    rdd.foreach(println)
       //rdd.saveAsTextFile("hdfs://localhost:9000/inout3")
   // rdd.foreach(println)
  }

}