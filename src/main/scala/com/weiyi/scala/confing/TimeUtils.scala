package com.weiyi.scala.confing

import java.sql.Timestamp
import com.github.nscala_time.time.Imports._

/**
 * Utility for time
 *
 * Created by zhangzhonglai on 14-7-29.
 */
object TimeUtils {
  val SQL_DATE = """^([0123]\d{3}-(?:0[1-9]|1[012])-(?:0[1-9]|[12]\d|3[01]))""".r
  val SQL_DATETIME = """^([0123]\d{3}-(?:0[1-9]|1[012])-(?:0[1-9]|[12]\d|3[01]) [0-2]\d:[0-5]\d:[0-5]\d)(\.\d*)?""".r
  val YEAR_MONTH_06 = """^(\d{4}(?:0[1-9]|1[012]))""".r
  val YEAR_MONTH_DAY_08 = """^(\d{4}(?:0[1-9]|1[012])(?:0[1-9]|[12]\d|3[01]))""".r
  val UNIX_TIMESTAMP_10 = """^([12]\d{9})(\.\d*)?""".r
  val UNIX_TIMESTAMP_13 = """^([12]\d{12})(\.\d*)?""".r

  def toDateTime(s: Any): DateTime = s match {
    case x: DateTime ⇒ x
    case x: Timestamp ⇒ new DateTime(x)  // TODO: maybe need second param like DateTimeZone.forID("Asia/Shanghai")
    case x ⇒ x.toString match {
      case SQL_DATE(t) ⇒ DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(t)
      case SQL_DATETIME(t, _) ⇒ DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(t)
      case YEAR_MONTH_06(d) ⇒ DateTimeFormat.forPattern("yyyyMM").parseDateTime(d)
      case YEAR_MONTH_DAY_08(d) ⇒ DateTimeFormat.forPattern("yyyyMMdd").parseDateTime(d)
      case UNIX_TIMESTAMP_10(i, _) ⇒ (i.toLong * 1000).toDateTime
      case UNIX_TIMESTAMP_13(i, _) ⇒ i.toLong.toDateTime
    }
  }

  def getDateTimeAtStartOfMonth(s: Any): DateTime = toDateTime(toDateTime(s).toString("yyyyMM01"))

  def getMidnight(s: Any): DateTime = toDateTime(toDateTime(s).toString("yyyyMMdd"))

  def getMillisOfMidnight(s: Any): Long = getMidnight(s).getMillis

  def getUnixTimestampOfMidnight(s: Any): Long = getMillisOfMidnight(s) / 1000

  def now: DateTime = DateTime.now

  def toYM(s: Any): Int = toDateTime(s).toString("yyyyMM").toInt

  def lastMonthRange: DateRange = {
    val thisMonth = getDateTimeAtStartOfMonth(now)
    DateRange(thisMonth - 1.month, thisMonth)
  }
}
