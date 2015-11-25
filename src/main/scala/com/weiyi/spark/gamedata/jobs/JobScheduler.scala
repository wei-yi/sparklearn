package com.weiyi.spark.gamedata.jobs

import java.io.{File, FileInputStream}

import akka.actor.{ Props, ActorSystem, ActorRef}
import org.slf4j.LoggerFactory
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

import scala.collection.mutable


object JobScheduler {

    val log = LoggerFactory.getLogger(JobScheduler.getClass)
    var jobs  = new mutable.LinkedList[ActorRef]()
    val  jobInfos: Array[JobInfo] = {
    val yaml = new Yaml(new Constructor(classOf[JobsDescribe]))
    val jobsDescribe = yaml.load(new FileInputStream(new File("conf/JobDescribe.yaml"))).asInstanceOf[JobsDescribe]
     jobsDescribe.getJobInfos
  }

  def initJobs(): Unit = {
    val system = ActorSystem("JobScheduler")
    for(jobInfo <- jobInfos) {
    //val instance = Class.forName(jobInfo.jobClassName).getConstructor(classOf[JobInfo]).newInstance(jobInfo).asInstanceOf[Job] //actor can't new
     jobs = jobs.:+(system.actorOf(Props(Class.forName(jobInfo.jobClassName).
       getConstructor(classOf[JobInfo]).newInstance(jobInfo).asInstanceOf[Job]),name = jobInfo.jobName))
      log.info("jobName "+  jobInfo.jobName+ " , JobClassName "+ jobInfo.jobClassName)
    }
  }

  def start(): Unit = {

    initJobs
    for( job <-jobs ) {
       job ! job
    }
  }

    def main(args: Array[String]) {
      start()
    }

}