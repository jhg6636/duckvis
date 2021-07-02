package com.duckvis.nuguri.domain.statistics.dtos

import com.duckvis.core.dtos.nuguri.WorkTimeDto
import com.duckvis.core.types.nuguri.SpecialStatisticsType
import com.duckvis.core.utils.secondsToString

data class StatisticsResponseDto(
  val workTimeDto: WorkTimeDto,
  val targetSeconds: Int,
  val type: SpecialStatisticsType,
  val userName: String,
  val leftDays: Int,
) {

  val responseMessage: String
    get() = if (workTimeDto.hasWorked) {
      "$headMessage:pushpin:${userName}님은 전체 프로젝트에서\n" +
        "총 ${workTimeDto.workTimeString}\n일하셨네요~\n$monthlyMessage"
    } else {
      ":clock12:해당 기간에 일한 기록이 없어요!"
    }

  private val headMessage: String
    get() = when (type) {
      SpecialStatisticsType.MONTHLY -> "어디 봅시다...\n이번달 통계에요~\n"
      SpecialStatisticsType.WEEKLY -> "음... 찾았어요!\n이번주 통계에요~\n"
      SpecialStatisticsType.NORMAL -> "어...앗!\n해당 기간의 통계에요~\n"
      SpecialStatisticsType.ALL_PROJECT -> "잠시만요...!\n플젝별 통계에요~\n"
    }

  private val leftTimeMessage: String
    get() = if (targetSeconds - workTimeDto.all > 0) {
      "이번 달 근무시간까지 ${(targetSeconds - workTimeDto.all).secondsToString}만큼 남으셨어요~\n"
    } else {
      "이번 달 근무 시간을 모두 채우셨네요~\n고생하셨어요~\n"
    }

  private val monthlyMessage: String
    get() = if (type == SpecialStatisticsType.MONTHLY) {
      "$leftTimeMessage$averageMessage"
    } else {
      ""
    }

  private val averageMessage: String
    get() = "앞으로 하루 평균 ${((targetSeconds - workTimeDto.all) / leftDays).secondsToString}만큼씩 일하시면 되겠습니다~"

}