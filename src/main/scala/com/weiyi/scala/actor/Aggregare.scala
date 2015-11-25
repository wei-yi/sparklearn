package com.weiyi.scala.actor

import java.util

import akka.actor.Actor
import com.weiyi.scala.messages.Messages
import Messages.ReduceData
import  scala.collection.JavaConversions._

class Aggregare extends Actor {
  val wordCountResult = new util.HashMap[String,Int]()

  override def receive: Receive = {
    case reduceData:ReduceData =>wordCount(reduceData)
      println("result: "+ wordCountResult)
  }

  def wordCount(reduceData:ReduceData):Unit = {
    for( key <- reduceData.words.keySet()) {
      if(wordCountResult.get(key) == null ) {
        wordCountResult.put(key,reduceData.words.get(key))
      }else {
        wordCountResult.put(key, wordCountResult.get(key)+reduceData.words.get(key))
      }
    }
  }

}
