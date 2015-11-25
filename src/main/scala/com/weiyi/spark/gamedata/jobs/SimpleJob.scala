package com.weiyi.spark.gamedata.jobs

import org.apache.spark.sql.DataFrame

import scala.reflect.internal.util.TableDef.Column

/**
 * test for example to deal with account and player data
 * @param jobInfo
 */
class SimpleJob(jobInfo: JobInfo) extends Job(jobInfo){

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
    leftDF.join(rightDF ,jobInfo.joinColmn).where(rightDF("value") > 500).show()
  }
}