package com.weiyi.spark.gamedata.util

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkContext, SparkConf}

object SparkTools {
  val conf = new SparkConf().setAppName("game data  mining").setMaster("local[4]").set("spark.ui.port","5050")
  val sc = new SparkContext(conf)
  val sQLContext =  new SQLContext(getSc())

  def getSc(): SparkContext = {
    sc
  }

  def getSqlContext(): SQLContext = {
    sQLContext
  }

}