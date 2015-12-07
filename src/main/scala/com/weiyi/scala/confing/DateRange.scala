package com.weiyi.scala.confing

import com.github.nscala_time.time.Imports._

/**
 * Date Range
 *
 * @author zhangzhonglai on 14-7-18.
 */
object DateRange {
  def month(m: String): DateRange = {
    val monthStart = TimeUtils.toDateTime(m)
    new DateRange(monthStart, monthStart + 1.month)
  }

  /**
   * Returns a list of [[DateRange]] objects by splitting a start [[org.joda.time.DateTime]],
   * end [[org.joda.time.DateTime]] and [[org.joda.time.Period]], as so:
   * {{{
   * val dr = DateRange.split(new DateTime("2014-01-01"), new DateTime("2014-03-01"), 1.month)
   * dr: List[DateRange] = List(DateRange(2014-01-01,2014-02-01), DateRange(2014-02-01,2014-03-01))
   * }}}
   *
   * @param start a start time point
   * @param end a end time point
   * @param period unit of the period
   *
   * @return a list of [[DateRange]] objects
   */
  def split(start: DateTime, end: DateTime, period: Period = 10.days): List[DateRange] = {
    def loop(result: List[DateRange], begin: DateTime): List[DateRange] = {
      val pr = DateRange(begin, period)

      if (pr.endTime > end) {
        result
      } else {
        loop(result :+ pr, pr.endTime)
      }
    }

    loop(Nil, start)
  }

  /**
   * Returns a list of [[DateRange]] objects by specifying a start point and n periods
   * {{{
   * var dr = DateRange.nextN(new DateTime("2014-01-01"), 2, 1.month)
   * dr: List[util.DateRange] = List(DateRange(2014-01-01,2014-02-01), DateRange(2014-02-01,2014-03-01))
   * }}}
   *
   * @param start a start time point
   * @param num number of the period
   * @param period unit of the period
   *
   * @return a list of [[DateRange]] objects
   */
  def nextN(start: DateTime, num: Long, period: Period = 10.days): List[DateRange] = {
    def loop(result: List[DateRange], begin: DateTime, n: Long): List[DateRange] = {
      val pr = DateRange(begin, period)
      if (n <= 0) {
        result
      } else {
        loop(result :+ pr, pr.endTime, n - 1)
      }
    }

    loop(Nil, start, num)
  }

  /**
   * Returns a list of [[DateRange]] objects which represented as season
   * {{{
   * var dr = DateRange.seasons(new DateTime("2014-01-01"), new DateTime("2014-07-01"))
   * dr: List[util.DateRange] = List(DateRange(2014-01-01,2014-04-01), DateRange(2014-04-01,2014-07-01))
   * }}}
   *
   * @param start a start time point
   * @param end a end time point
   *
   * @return a list of [[DateRange]] objects
   */
  def seasons(start: DateTime, end: DateTime): List[DateRange] = {
    def loop(result: List[DateRange], begin: DateTime): List[DateRange] = {
      val pr = DateRange(begin, 3.months)
      if (pr.endTime > end) {
        result
      } else begin.getMonthOfYear match {
        case 1 | 4 | 7 | 10 ⇒
          loop(result :+ pr, pr.endTime)
        case _ ⇒ loop(result, begin + 1.month)
      }
    }

    loop(Nil, start)
  }

  /** Returns a [[DateRange]] object start from a time point with a specified period */
  def apply(start: DateTime, period: Period = 10.days): DateRange = {
    new DateRange(start, nextDateTimeOfPeriod(start, period))
  }

  private def nextDateTimeOfPeriod(start: DateTime, period: Period): DateTime = {
    val now = DateTime.now
    if (period.toDurationFrom(now) < 1.month.toDurationFrom(now)) {
      val next = start + period
      if (start.dayOfMonth().withMaximumValue() <= next + 3.days) {
        start.dayOfMonth().withMinimumValue() + 1.month
      } else {
        next
      }
    } else {
      start + period
    }
  }
}

case class DateRange(startTime: DateTime, endTime: DateTime) {
  require(startTime <= endTime, "startTime must earlier than endTime")

  val period        = new Period(startTime, endTime)
  val startUnixTime = startTime.getMillis / 1000
  val endUnixTime   = endTime.getMillis / 1000

  def toList(format: String): List[String] = List(startTime.toString(format), endTime.toString(format))

  def render(format: String): String = s"[${startTime.toString(format)}, ${endTime.toString(format)}]"

  def include(dateTime: DateTime): Boolean = dateTime >= startTime && dateTime < endTime

  def concat(other: DateRange): DateRange = {
    val start = if (startTime < other.startTime) startTime else other.startTime
    val end = if (endTime < other.endTime) other.endTime else endTime
    DateRange(start, end)
  }

  /**
   * Splits this object to a list of smaller DateRange objects with a specified period
   * @see [[DateRange.split]]
   */
  def split(period: Period): List[DateRange] = DateRange.split(startTime, endTime, period)

  /**
   * Returns a list of DateRange objects with a specified number of period from this object's end time
   * @see [[DateRange.nextN]]
   */
  def nextN(num: Long, period: Period): List[DateRange] = DateRange.nextN(endTime, num, period)

  /** Returns a tuple2 of DateRange which cut from the offset period */
  def cut(offset: Period): (DateRange, DateRange) = {
    (DateRange(startTime, offset), DateRange(startTime + offset, endTime))
  }

  /**
   * Returns a list of DateRange objects represented as season in this object
   * @see [[DateRange.seasons]]
   */
  def seasons: List[DateRange] = DateRange.seasons(startTime, endTime)

  def extendedTo(end: DateTime): DateRange = DateRange(startTime, end)
}

/**
 * Represent a natural season
 *
 * @author zhangzhonglai on 15-10-19.
 */
case class Season(year: Int, seasonNo: Int) {
  require((1 to 4).contains(seasonNo), s"Input season no is $seasonNo, but it only has 1, 2, 3 or 4")

  def this(time: DateTime) = this(time.getYear, Season.month2SeasonNo(time.getMonthOfYear))

  /**
   * @return start [[DateTime]] of this season, this is a closed interval(math)
   */
  def startTime = new DateTime(year, Season.seasonNo2Month(seasonNo), 1, 0, 0)

  /**
   * @return end [[DateTime]] of this season, this is a open interval(math)
   */
  def endTime = startTime + 3.months

  /**
   * @return a [[DateRange]] of this season
   */
  def range = DateRange(startTime, endTime)
}

object Season {
  val seasonNo2Month = Map(1 → 1, 2 → 4, 3 → 7, 4 → 10)
  val month2SeasonNo = (1 to 12).map(x ⇒ (x, (x + 2) / 3)).toMap

  def apply(time: DateTime) = new Season(time)
}
