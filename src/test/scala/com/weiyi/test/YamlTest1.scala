package com.weiyi.test

import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

import scala.reflect.BeanProperty

object YamlTest1{
  val text = "accountName: Ymail Account"
  def main(args: Array[String]) {
    val yaml = new Yaml(new Constructor(classOf[EmailAccount]))
    val e = yaml.load(text).asInstanceOf[EmailAccount]
    println(e)
  }
}
/**
 * With the Snakeyaml Constructor approach shown in the main method,
 * this class must have a no-args constructor.
 */
class EmailAccount {

   @BeanProperty var accountName: String = null
  override def toString: String = {
     accountName
  }
}
