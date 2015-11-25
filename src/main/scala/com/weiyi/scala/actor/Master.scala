package com.weiyi.scala.actor

import akka.actor.SupervisorStrategy.{Escalate, Stop, Restart, Resume}
import akka.actor.{OneForOneStrategy, ActorRef, Props, Actor}
import scala.concurrent.duration._

class Master extends  Actor {
  val aggreTask = context.actorOf(Props(new Aggregare), "aggreTask")
  val reduceTask = context.actorOf(Props(new ReduceTaskActor(aggreTask)), "reduceTask")
  val mapTask:ActorRef = context.actorOf(Props(new MapTask(reduceTask)), "mapTask")

  override def receive: Receive = {
  case line: String =>
    val future = mapTask ! line
  case num: Int => println(num)
  }

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
      case _: ArithmeticException      => Resume
      case _: NullPointerException     => Restart
      case _: IllegalArgumentException => Stop
      case _: Exception                => Escalate
    }
}