package com.weiyi.spark.gamedata.tables

import com.weiyi.spark.gamedata.util.SparkTools
import org.apache.spark.sql.DataFrame

import scala.beans.BeanProperty

class TableDFImpl extends TableDF {

  @BeanProperty var  dfName: String = null
  @BeanProperty var  jsonFilePath: String = null
  @BeanProperty var  isCache: Boolean  = false

  override  def createTableDF(): DataFrame = {
    if(jsonFilePath != null) {
      SparkTools.getSqlContext().read.json(jsonFilePath)
    }else {
      null
    }
  }
  override  def toString(): String = {
    dfName + " " + jsonFilePath  + " " + isCache.toString
  }

}