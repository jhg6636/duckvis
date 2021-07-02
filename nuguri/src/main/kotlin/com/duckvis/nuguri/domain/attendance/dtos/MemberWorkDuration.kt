package com.duckvis.nuguri.domain.attendance.dtos

import com.duckvis.core.dtos.nuguri.WorkTimeDto
import com.duckvis.nuguri.domain.statistics.dtos.WorkTypeDuration

data class MemberWorkDuration(
  val userName: String,
  val allDurationSeconds: Int,
  val extendedDurationSeconds: Int,
  val holidayDurationSeconds: Int,
  val nightDurationSeconds: Int,
) {

  companion object {
    fun of(userName: String, workTypeDurations: List<WorkTypeDuration>): MemberWorkDuration {
      return MemberWorkDuration(
        userName = userName,
        allDurationSeconds = workTypeDurations.sumBy { workTypeDuration -> workTypeDuration.durationSeconds },
        extendedDurationSeconds = workTypeDurations
          .filter { workTypeDuration -> workTypeDuration.workTypeDto.isExtended }
          .sumBy { workTypeDuration -> workTypeDuration.durationSeconds },
        holidayDurationSeconds = workTypeDurations
          .filter { workTypeDuration -> workTypeDuration.workTypeDto.isHoliday }
          .sumBy { workTypeDuration -> workTypeDuration.durationSeconds },
        nightDurationSeconds = workTypeDurations
          .filter { workTypeDuration -> workTypeDuration.workTypeDto.isNight }
          .sumBy { workTypeDuration -> workTypeDuration.durationSeconds },
      )
    }
  }

  val oneMemberString: String
    get() = ":pushpin:${userName}님은 전체 프로젝트에서\n총 $statisticsString"

  private val statisticsString: String
    get() = WorkTimeDto(
      allDurationSeconds,
      nightDurationSeconds,
      extendedDurationSeconds,
      holidayDurationSeconds
    ).workTimeString

}