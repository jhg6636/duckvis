package com.duckvis.slack.dtos

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class SlackEventDto @JsonCreator constructor(
  @JsonProperty("text") val text: String,
  @JsonProperty("user") val userCode: String,
  @JsonProperty("channel") val channel: String,
) {

  fun trim(): SlackEventDto {
    return SlackEventDto(text.trim(), userCode.trim(), channel.trim())
  }

}