package com.duckvis.slack.service

import com.duckvis.core.SlackConstants
import com.duckvis.slack.dtos.SlackUserInfoResponseDto
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

@Service
class GetUserNameService {

  private val restTemplate = RestTemplate()

  // THINKING 문자열을 받아와 직접 파싱하는게 아니라, 객체를 바로 받아올 수 있습니다
  fun get(userCode: String): String {
    val httpHeaders = HttpHeaders()
    httpHeaders.add("Authorization", "Bearer ${SlackConstants.BOT_TOKEN}")
    val responseEntity = restTemplate.exchange<SlackUserInfoResponseDto>(
      "https://slack.com/api/users.info?user=$userCode",
      HttpMethod.GET,
      HttpEntity<SlackUserInfoResponseDto>(httpHeaders),
    )

    return responseEntity.body.user.profile.displayName
  }

}