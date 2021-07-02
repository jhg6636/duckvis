package com.duckvis.slack.service

import com.duckvis.core.SlackConstants
import com.duckvis.core.utils.splitByMaximumLetterCount
import com.google.gson.JsonObject
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

@Service
class PostMessageService {

  companion object {
    const val SLACK_MAX_LETTERS = 1500
  }

  private val restTemplate: RestTemplate = RestTemplate()

  fun post(message: String, channel: String) {
    message
      .splitByMaximumLetterCount(SLACK_MAX_LETTERS)
      .forEach { oneMessage ->
        postOneMessage(oneMessage, channel)
      }
  }

  private fun postOneMessage(message: String, channel: String) {
    val httpBody = JsonObject()
    httpBody.addProperty("channel", channel)
    httpBody.addProperty("text", message)
    val httpHeaders = HttpHeaders()
    httpHeaders.add("Content-type", "application/json; charset=utf-8")
    httpHeaders.add("Authorization", "Bearer ${SlackConstants.BOT_TOKEN}")

    val httpEntity = HttpEntity(httpBody.toString(), httpHeaders)
    restTemplate.exchange<String>(
      "https://slack.com/api/chat.postMessage",
      HttpMethod.POST,
      httpEntity,
      String::class
    )
  }

}