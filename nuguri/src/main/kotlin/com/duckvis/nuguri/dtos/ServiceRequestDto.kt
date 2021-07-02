package com.duckvis.nuguri.dtos

import com.duckvis.core.domain.nuguri.UserTeam
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.shared.UserLevelType
import com.duckvis.core.utils.*
import java.time.LocalDateTime

/**
 * text는 ㄲ ㄱㅂ 이 들어간다
 * params에는 ['ㄱㅂ']이 들어간다
 *
 * val command: CommandDto
 * data class CommandDto(
 *   val baseCommand: "ㄲ"
 *   val options: List<String>
 * )
 *
 */
data class ServiceRequestDto(
  val text: String,
  val params: MutableList<String>,
  val userLevel: UserLevelType,
  val userTeam: UserTeam?,
  val userName: String,
  val userCode: String,
) {

  // for Statistics
  val dateTimes: List<LocalDateTime>
    get() {
      val typedDates = params.filter { param -> param.all { char -> char.isDigit() } }
      return when (typedDates.size) {
        0 -> {
          when (text.substring(0, 5)) {
            "!주간통계", "!ㅈㄱㅌㄱ" -> listOf(
              DateTimeMaker.nowDateTime().weekStartTime,
              DateTimeMaker.nowDateTime().weekEndTime
            )
            "!월간통계", "!ㅇㄱㅌㄱ" -> listOf(
              DateTimeMaker.nowDateTime().monthStartTime,
              DateTimeMaker.nowDateTime().monthEndTime
            )
            else -> throw NuguriException(ExceptionType.TYPO)
          }
        }
        1 -> listOf(typedDates.single().toDayStartTime, typedDates.single().toDayEndTime)
        2 -> listOf(typedDates[0].toDayStartTime, typedDates[1].toDayEndTime)
        else -> throw NuguriException(ExceptionType.TYPO)
      }
    }

}