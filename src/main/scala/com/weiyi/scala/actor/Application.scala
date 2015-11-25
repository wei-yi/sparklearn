package com.weiyi.scala.actor

import akka.actor.{Props, ActorSystem}

object Application {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("wordCountApp")
    val master = system.actorOf(Props(new Master), "master")
    master ! "hello world hello hadoop hello spark"
    master ! "hello world1 hello hadoop hello spark"

    Thread.sleep(5000)
    system.shutdown()
  }
}