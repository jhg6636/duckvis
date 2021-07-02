package com.duckvis.nuguri.external.mail

import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import java.io.File
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMultipart

@Component
class NuguriMailSender {

  private val javaMailSender = JavaMailSenderImpl()

  private fun prepare(fromEmail: String, fromPassword: String) {
    javaMailSender.host = "smtp.gmail.com"
    javaMailSender.port = 587
    javaMailSender.username = fromEmail
    javaMailSender.password = fromPassword

    val properties = javaMailSender.javaMailProperties
    properties["mail.transport.protocol"] = "smtp"
    properties["mail.smtp.auth"] = "true"
    properties["mail.smtp.starttls.enable"] = "true"
    properties["mail.debug"] = "true"
  }

  fun send(target: String, title: String, content: String, file: File) {
    prepare(MailConstant.EXTRACT.username, MailConstant.EXTRACT.password)
    val message = javaMailSender.createMimeMessage()
    val helper = MimeMessageHelper(message)
    helper.setTo(target)
    helper.setSubject(title)
    helper.setText(content)

    val mimeBodyPart = MimeBodyPart()
    val fileDataSource = FileDataSource(file.name)
    mimeBodyPart.dataHandler = DataHandler(fileDataSource)
    mimeBodyPart.fileName = fileDataSource.name

    val multiPart = MimeMultipart()
    multiPart.addBodyPart(mimeBodyPart)

    message.setContent(multiPart)

    javaMailSender.send(message)
  }

}
