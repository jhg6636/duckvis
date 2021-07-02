package com.duckvis.slack.dtos

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class SlackUserInfoResponseDto @JsonCreator constructor(
  @JsonProperty("user") val user: SlackUserDto,
)

data class SlackUserDto @JsonCreator constructor(
  @JsonProperty("profile") val profile: SlackUserProfileDto,
)

data class SlackUserProfileDto @JsonCreator constructor(
  @JsonProperty("display_name") val displayName: String,
)
