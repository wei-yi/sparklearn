package  com.weiyi.crontab

sealed trait Time


case class Minute(minute: String) extends Time {
  minute match  {
    case "([0-9]){1,2}" ⇒ println(minute)
  }
}

class Timer(month: String, weekday: String, day: String, hour: String, Minute: String  ) {

}