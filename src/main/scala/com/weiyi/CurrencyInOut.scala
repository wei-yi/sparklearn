package com.weiyi

import org.apache.spark.rdd.RDD

/**
 * This class represent the model of game currency in and out, which would replace the Income class and Outgo class for
 * exactly description of the player's currency change
 *
 * kind of Income: {{{0 → UNDEFINED}}}
 * kind of Outgo: {{{0 → UNDEFINED, 1 → IMMEDIATE, 2 → CONTINUOUS, 3 → PERPETUAL, 4 → CONSUMED}}}
 * @author zhangzhonglai on 15/10/27
 */
case class CurrencyInOut(uuid: String, unixTime: Long, before: Long, change: Long, after: Long, variety: String,
                         kind: Int, game: String, ym: Int) {
  // require(after - change == before, s"after($after) - change($change) must be before($before)")

  /**
   * Predicate this is previous currency statement of that one
   */
  def isPreviousOf(that: CurrencyInOut): Boolean = {
    uuid == that.uuid && unixTime <= that.unixTime && after == that.before
  }

  /**
   * Predicate this is following currency statement of that one
   */
  def isFollowingBy(that: CurrencyInOut): Boolean = {
    uuid == that.uuid && unixTime >= that.unixTime && before == that.after
  }

}

object CurrencyInOutValidation {

  def isSequence(a: CurrencyInOut, b: CurrencyInOut): Boolean = a.isPreviousOf(b)

  def findInvalidCurrencyInOut(rdd: RDD[CurrencyInOut]): RDD[Array[CurrencyInOut]] = {
    rdd.groupBy(cIO => (cIO.uuid, cIO.game)).map {
      case ((uuid, game), cIOs) =>
        createInvalidCurrencyInOut(cIOs.toArray.sortBy(_.unixTime))
    }
  }

  def createInvalidCurrencyInOut(cIOs: Array[CurrencyInOut]): Array[CurrencyInOut] = {
    var invalidCIOs: List[CurrencyInOut] = Nil
    for (i <- 0 to cIOs.length - 2) {
      if (!isSequence(cIOs(i), cIOs(i + 1))) {
        invalidCIOs = cIOs(i) :: cIOs(i + 1) :: invalidCIOs
      }
    }
    invalidCIOs.toArray.distinct
  }

  def findInvalidCurrencyInOut1(rdd: RDD[CurrencyInOut]): RDD[CurrencyInOut] = {
    rdd.groupBy(cIO => (cIO.uuid, cIO.game)).flatMap {
      case ((uuid, game), cIOs) =>
        val sortedCIOS = cIOs.toArray.sortBy(_.unixTime)
        sortedCIOS.init.zip(sortedCIOS.tail).foldLeft(List.empty[CurrencyInOut]) { (list, c) ⇒
          if (!isSequence(c._1, c._2)) {
            c._1 :: c._2 :: list
          } else {
            list
          }
        }.reverse
    }
  }

  def findValidCurrencyInOut(rdd: RDD[CurrencyInOut]): RDD[CurrencyInOut] = {
    rdd.groupBy(cIO => (cIO.uuid, cIO.game)).flatMap {
      case ((uuid, game), cIOs) =>
        val sortedCIOS = cIOs.toArray.sortBy(_.unixTime)
        sortedCIOS.init.zip(sortedCIOS.tail).foldLeft(List.empty[CurrencyInOut]) { (list, c) ⇒
          if (!isSequence(c._1, c._2)) {
            val unixTime = (c._1.unixTime+c._2.unixTime)/2
            val change = c._2.before - c._1.after
            val validCIO = CurrencyInOut(c._1.uuid, unixTime, c._1.after,change,c._2.before, c._1.variety, c._1.kind, c._1.game, c._1.ym)
            validCIO :: list
          } else {
            list
          }
        }.reverse
    }
  }

  def temp0(list: List[CurrencyInOut], target: List[CurrencyInOut]): List[CurrencyInOut] = target match {
    case c1 :: c2 :: tail ⇒
      if (isSequence(c1, c2)) temp0(list, c2 :: tail) else temp0(c1 :: c2 :: list, c2 :: tail)
    case _ ⇒ list
  }


}
