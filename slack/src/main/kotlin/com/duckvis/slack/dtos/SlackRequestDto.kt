package com.duckvis.slack.dtos

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class SlackRequestDto @JsonCreator constructor(
  @JsonProperty("event") val slackEventDto: SlackEventDto
)