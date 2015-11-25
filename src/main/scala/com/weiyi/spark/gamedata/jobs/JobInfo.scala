package com.weiyi.spark.gamedata.jobs

import com.weiyi.spark.gamedata.tables.TableDFImpl

import scala.beans.BeanProperty

class JobInfo{

  @BeanProperty var jobName: String = null
  @BeanProperty var jobClassName: String = null
  @BeanProperty var jobType: String = null
  @BeanProperty var jobLeftDF: TableDFImpl = null
  @BeanProperty var jobRightDF: TableDFImpl = null
  @BeanProperty var joinColmn: String = null
  @BeanProperty var rightDFAs: String = null
  @BeanProperty var selectColumns: String = null
  @BeanProperty var whereCond: String = null
  override def toString: String = {
    jobName
  }
}
