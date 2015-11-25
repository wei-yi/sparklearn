package com.weiyi.spark.gamedata.tables

import org.apache.spark.sql.DataFrame

abstract  class TableDF {

 def createTableDF(): DataFrame

}