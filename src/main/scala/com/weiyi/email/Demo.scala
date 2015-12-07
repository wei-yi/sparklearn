package com.weiyi.scala.confing

import com.weiyi.email.mail
import com.weiyi.scala.Config

object Demo extends App {
  import mail._
  import Config._

  send a new Mail (
    from = (emailConfig.fromEmail, emailConfig.name),
    to = emailConfig.to,
    cc = emailConfig.cc.get,
    bcc = emailConfig.bcc.get,
    subject = emailConfig.subject,
    message = "Dear Boss..."
  )

//  send a new Mail (
//    from = "john.smith@mycompany.com" -> "John Smith",
//    to = Seq("dev@mycompany.com", "marketing@mycompany.com"),
//    subject = "Our New Strategy (tm)",
//    message = "Please find attach the latest strategy document.",
//    richMessage = "Here's the <blink>latest</blink> <strong>Strategy</strong>..."
//  )
//
//  send a new Mail (
//    from = "john.smith@mycompany.com" -> "John Smith",
//    to = "dev@mycompany.com" :: "marketing@mycompany.com" :: Nil,
//    subject = "Our 5-year plan",
//    message = "Here is the presentation with the stuff we're going to for the next five years.",
//    attachment = new java.io.File("/home/boss/important-presentation.ppt")
//  )
}