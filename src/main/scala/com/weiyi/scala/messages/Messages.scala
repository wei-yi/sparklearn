
package com.weiyi.scala.messages

import java.util

object Messages {

  case class Word(val word: String,val  one: Int )
  case class MapData (val words: util.LinkedList[Word])
  case class ReduceData(val words: util.HashMap[String, Int])
  case class Result
}