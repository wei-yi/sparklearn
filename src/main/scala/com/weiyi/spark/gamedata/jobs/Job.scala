package com.weiyi.spark.gamedata.jobs

import akka.actor.{ActorRef, Actor}

abstract class Job(jobInfo: JobInfo) extends Actor{

  override def receive: Receive = {
    case job :ActorRef => run()
  }
  def setup() {}

  def run()

  def end() {}

}