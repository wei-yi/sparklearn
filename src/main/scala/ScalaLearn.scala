package com.xindong.scala.io

import java.io.File

import scala.io.Source
import scala.util.MurmurHash


class ScalaLearn {

  def  readFile(filePath: String): Unit = {
    val file = Source.fromFile(new File(filePath))
    //file.addString(new StringBuilder("yuanyi"))
    file.getLines().foreach(println)
    val list = List(1,2,3,4).dropRight(2).foreach(println)
   // val in = Console.readLine("Type Either a string or an Int: ")
  }

  def testEither(input: Int): Unit = {

    val nameAgeMap: Either[String, Int] = try {
      Right(input.toInt)
    } catch {
      case e: Exception =>
        Left(input.toString)
    }
    nameAgeMap match {
      case Right(age) => println(age)
      case Left(name) => println("name "+name)
    }

  }

  def testMurmurHash() {
  val hash = MurmurHash

  }

  def testMatch() {
   val nums = List(1,2,3,4,5)

    val addNums: List[String] = nums match {
      case keys: List[Int] => keys.map(value => value+1+"a")
    }
    addNums.foreach(println)
  }


}



object ScalaLearn {
    def apply() = new  ScalaLearn()
  }

  object  mainObject {
    def main(args: Array[String]): Unit = {
//      val learn = new  ScalaLearn()
//      learn.readFile("/Users/yuanyi/bigdata/derby.log")
      ScalaLearn().readFile("/Users/yuanyi/bigdata/derby.log")
      ScalaLearn().testEither(100)
      ScalaLearn().testMatch()
    }
}