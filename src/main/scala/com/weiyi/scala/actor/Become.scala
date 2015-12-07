package com.weiyi.scala.actor

import akka.actor.{Props, ActorSystem, Actor}
import akka.event.Logging
import akka.util.Timeout
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._
import akka.actor.ReceiveTimeout
import akka.pattern.ask


case class Message(name: String)
case class TestAsk(command: String)

class Become extends Actor  {
  import context._

  val log = Logging(context.system, this)
  val f = Future[String] {"return ask"}

  override def  receive = {
    case  mess: Message ⇒ log.info(mess.name)
      if(mess.name == "ask")
      context.setReceiveTimeout(1 seconds)
      become({
        case mess: Message ⇒ log.info("lala")
          unbecome()
      }
      )
    case taskAsk: TestAsk ⇒
      Thread.sleep(4000)
      sender() ! s"return ${taskAsk.command} without use future"

    case ReceiveTimeout =>  //no catch no throw ?
      // To turn it off
      context.setReceiveTimeout(Duration.Undefined)
      throw new RuntimeException("Receive timed out")
    case "stop" ⇒
      log.info("receive stop command")
      context stop self
  }

}

object  testMain extends App {

  val system = ActorSystem("become_or_unbecome_test")
  val become1 = system.actorOf(Props[Become], name="becomeTest")
  implicit val timeout = Timeout(5 seconds)
  val future = become1 ? TestAsk("ask") //blocking here

  become1 ! Message("mess")
  become1 ! Message("mess")
  Thread.sleep(10000)
  val result = Await.result(future, timeout.duration).asInstanceOf[String]
  println(result)
  become1 ! Message("mess")
  become1 ! Message("mess")
  become1 ! Message("mess")
  become1 ! Message("mess")
  become1 ! "stop"
  become1 ! Message("mess1")
  system.shutdown()
  system.awaitTermination(5 seconds)

}