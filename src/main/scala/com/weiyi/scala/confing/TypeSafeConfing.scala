package com.weiyi.scala.confing

import java.io.File

import com.typesafe.config.ConfigFactory
import com.weiyi.spark.gamedata.util.SparkTools
import spray.json._

object Settings {
  val config = ConfigFactory.parseFile(new File("/Users/yuanyi/bigdata/sparkSource/sparklearn/src/main/scala/com/weiyi/scala/confing/reference.conf"))
  // validate vs. reference.conf
  config.checkValid(ConfigFactory.defaultReference(), "GameConfig")
  val games = config.getAnyRefList("GameConfig.gamelist").toArray

  import MyJsonProtocol._

  def transform(): List[GameConfig] = {
    games.map(gc ⇒ config.getString(s"GameConfig.$gc")
      .parseJson
      .convertTo[GameConfig]).toList
  }

  def getgames(path: String): Unit = {
    for (game <- games)
      println(game)
  }

}

object TestConfing extends App {
  val gameconf = Settings.transform()
  Settings.games.foreach(println)
  for (conf ← gameconf) {
    Game(SparkTools.getSc(), conf).createTableDFs()
  }

}
