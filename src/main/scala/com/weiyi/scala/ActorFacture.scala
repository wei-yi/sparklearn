package com.weiyi.scala

import akka.actor.{Props, ActorSystem, Actor}
import akka.event.LoggingReceive
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.Await


class Worker extends Actor {
  override def receive =LoggingReceive {
    case name: String => println(name)
       Thread.sleep(5000)
      sender() ! 1

  }
}

object ActorFacture {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("boss")
    val worker = system.actorOf(Props(new Worker), "worker")
    implicit val timeout = Timeout(50, SECONDS)
    val future = worker ? "BD_YY"
    val result = Await.result(future,timeout.duration).asInstanceOf[Int]
    println("will wait")
    print(result)



  }

}