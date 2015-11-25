package com.weiyi.scala.actor

import java.util

import akka.actor.{ActorRef, Props, Actor}
import akka.actor.Actor.Receive
import com.weiyi.scala.messages.Messages
import Messages.{MapData, ReduceData, Word}
import  scala.collection.JavaConversions._

class ReduceTaskActor(aggreTask: ActorRef) extends  Actor {

  override def receive: Receive = {
    case words: MapData => aggreTask ! reduceWords(words)

  }

  def reduceWords(words: MapData): ReduceData = {
    val reduceWord = new util.HashMap[String,Int]()
    for(key: Word <- words.words ) {
      if(  reduceWord.get(key.word) == null ) {
        reduceWord.put(key.word,1)
      }else {
        reduceWord.put(key.word,reduceWord.get(key.word)+1)
      }
    }
    println("recuece :"+ reduceWord)
    ReduceData(reduceWord)
  }
}