package com.weiyi.scala.actor

import java.util

import akka.actor.{ActorRef, Props, Actor}
import com.weiyi.scala.messages.Messages
import Messages.{MapData, Word}

class MapTask(reduceTask: ActorRef)  extends Actor{

  override def receive: Receive = {
    case word:String =>
      reduceTask !  mapTask(word)
      sender() ! 1
  }

  def mapTask(line: String): MapData = {
    val words = new util.LinkedList[Word]()
    if(line != null) {
      words.clear()
      for ( word <- line.split(" ")) {
        words.add(new Word(word,1))
      }
    }
    MapData(words)
  }
}