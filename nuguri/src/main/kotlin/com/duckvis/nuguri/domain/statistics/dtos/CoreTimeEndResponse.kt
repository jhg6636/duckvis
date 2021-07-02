package com.duckvis.nuguri.domain.statistics.dtos

import com.duckvis.core.utils.StartAndEndTime
import com.duckvis.core.utils.secondsToString
import com.fasterxml.jackson.annotation.JsonIgnore

data class CoreTimeEndResponse(
  val userCode: String,
  private val userName: String,
  private val duration: Int,
) {

  @get:JsonIgnore
  val slackString: String
    get() = "$userName 코어근무시간 미달(${duration.secondsToString}, ${duration * 100 / StartAndEndTime.CORE_TIME_DURATION}% 근무)"

  @get:JsonIgnore
  val dmString: String
    get() = "${userName}님은 오늘 ${duration.secondsToString}(코어타임의 ${duration * 100 / StartAndEndTime.CORE_TIME_DURATION}%) 근무하셨어요~ 확인 부탁드립니다~"

}
