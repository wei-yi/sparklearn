package com.weiyi.scala

import java.io.File

import akka.actor.{ActorRef, Props, ActorSystem, Actor}

case class FileToProcess(path: String)
case class FileSize(size: Long)
case class RequireAFile(actor: ActorRef)
case class FileIsDir(path: String)

class FileProcessActor(collectorActor: ActorRef) extends Actor {
//  val collActors = context.watch(context.actorOf(Props[CollectorActor], "collect"))
//  collActors ! RequireAFile()
  var size: Long = 0
  collectorActor !  RequireAFile(self)

  def receive = {
    case FileToProcess(path) => {
      val dir = new File(path)
      val listFile = dir.listFiles()
      for(file <- listFile) {
        if(!file.isDirectory) {
          size+=file.length()
         // println("file -------"+ file.getAbsolutePath +" size " + size)
        }else {
          collectorActor ! FileIsDir(file.getAbsolutePath)
        }
      }
      collectorActor !  FileSize(size)
      collectorActor !  RequireAFile(self)
    }
  }
}


class CollectorActor(path: String) extends  Actor{
  var totalSize: Long = 0
  var needToProcessFile: List[String] = List(path)
  var actors : List[ActorRef] = List.empty[ActorRef]

  def fileToprocess() : Unit = {
     while(needToProcessFile != null && !needToProcessFile.isEmpty
       && actors != null && !actors.isEmpty){
       actors.head ! FileToProcess(needToProcessFile.head)
       needToProcessFile = needToProcessFile.drop(1)
       actors = actors.drop(1)
     }
  }

   override def receive = {
    case FileSize(size) => {
    totalSize+=size
      println("totalSize **** "+ totalSize)
    }
    case FileIsDir(dir: String) => {
      needToProcessFile = dir :: needToProcessFile
//      System.err.println(dir)
    }
    case RequireAFile(actor)  =>  {
      actors = actor :: actors
      fileToprocess()
    }
    case  name: String => {
    println("ok")
    }
  }

}

object FileTotalSize{

  def main(args: Array[String]): Unit = {
    val startTime = System.currentTimeMillis()
    val system = ActorSystem("ActorSystem")
    val collectorActor = system.actorOf(Props(new CollectorActor("/Users/yuanyi/bigdata")), name = "collector")//no safe

    for(i <- 1 to 10) {
      val actor = system.actorOf(Props(new FileProcessActor(collectorActor)), name = "FileProcessActor"+i)
    }
    val endTime = System.currentTimeMillis()

    println("use time :" + (endTime -startTime)) //no use
  }

}