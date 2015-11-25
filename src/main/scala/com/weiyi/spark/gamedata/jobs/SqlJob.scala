package com.weiyi.spark.gamedata.jobs

import com.weiyi.spark.gamedata.util.SparkTools
import org.apache.spark.sql.DataFrame
import org.slf4j.LoggerFactory


/**
 * test for example to deal with account and player data
 *
 */
class SqlJob(jobInfo: JobInfo) extends Job(jobInfo){

  val  log = LoggerFactory.getLogger(classOf[SqlJob])
  val  jobName = jobInfo.jobName
  val  leftTable  = jobInfo.jobLeftDF
  val  rightTable  = jobInfo.jobRightDF

  /**
   * to do something depend on ourselves
   * this just for test
   */
  override def run(): Unit = {
    //TUDO   scala   null need?
    var leftDF: DataFrame = null
    var rightDF: DataFrame = null
    leftTable.isCache match {
      case  true =>    leftDF = leftTable.createTableDF().cache()
      case false =>    leftDF = leftTable.createTableDF().cache()
    }
    rightTable.isCache match {
      case  true =>    rightDF = rightTable.createTableDF().cache()
      case false =>    rightDF = rightTable.createTableDF().cache()
    }
    // different job has different
    rightDF.registerTempTable("rTable")
    leftDF.registerTempTable("lTable")
    val sql = "select " + jobInfo.selectColumns +" from lTable , rTable where "+  jobInfo.whereCond
    log.debug(sql)
    SparkTools.getSqlContext().sql(sql).show()

  }
}