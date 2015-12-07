package com.weiyi.scala.confing

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
  require(after - change == before, s"after($after) - change($change) must be before($before)")

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
}
