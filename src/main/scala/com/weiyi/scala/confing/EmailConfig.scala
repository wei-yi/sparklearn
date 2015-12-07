package  com.weiyi.scala

import spray.json.DefaultJsonProtocol
import spray.json._
import com.typesafe.config.ConfigFactory

case class EmailConfig(userName: String, password: String, fromEmail: String, name: String, to: Seq[String],
                      cc: Option[Seq[String]] = None, bcc: Option[Seq[String]] = None, subject: String )

object MyJsonProtocol extends DefaultJsonProtocol {
  implicit  val MailConfigFormat =jsonFormat8(EmailConfig)
}

object Config{
  val emailConfig = {
    import MyJsonProtocol._
    ConfigFactory.load()
      .getString("GameConfig.config").parseJson
      .convertTo[EmailConfig]
  }
}