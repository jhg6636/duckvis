package com.duckvis.core.types.nuguri.service.params

import com.duckvis.core.domain.nuguri.UserTeam
import com.duckvis.core.dtos.nuguri.WorkTypeDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.SpecialStatisticsType
import com.duckvis.core.utils.*
import java.time.LocalDateTime

data class NuguriStatisticsRequestParameterDto(
  override val userCode: String,
  override val userName: String,
  val from: LocalDateTime,
  val to: LocalDateTime,
  val statisticsType: SpecialStatisticsType,
  val workTypeDto: WorkTypeDto = WorkTypeDto(),
  val userTeam: UserTeam?,
  val projectNameOrNickname: String?,
  val subProjectNameOrNickname: String?,
  val teamName: String?,
  val memberName: String?,
  val isEveryBody: Boolean = false,
  val isAdmin: Boolean = false,
) : NuguriServiceRequestParameterDto(userCode, userName) {

  companion object {

    fun of(
      splitText: List<String>,
      userTeam: UserTeam?,
      userCode: String,
      userName: String
    ): NuguriStatisticsRequestParameterDto {
      val now = DateTimeMaker.nowDateTime()
      val fromAndTo = when (splitText.first()) {
        "!월간통계", "!ㅇㄱㅌㄱ", "!drxr" -> listOf(now.monthStartTime, now.monthEndTime)
        "!주간통계", "!ㅈㄱㅌㄱ", "!wrxr" -> listOf(now.weekStartTime, now.weekEndTime)
        else -> listOf(
          splitText.getOrNull(1)?.toDayStartTime ?: throw NuguriException(ExceptionType.TYPO),
          splitText.getOrNull(2)?.toDayEndTime ?: splitText.getOrNull(1)?.toDayEndTime ?: throw NuguriException(
            ExceptionType.TYPO
          )
        )
      }

      val workTypeDto = WorkTypeDto.of(splitText)

      val statisticsType = when (splitText.first()) {
        "!월간통계", "!ㅇㄱㅌㄱ", "!drxr" -> SpecialStatisticsType.MONTHLY
        "!주간통계", "!ㅈㄱㅌㄱ", "!wrxr" -> SpecialStatisticsType.WEEKLY
        else -> SpecialStatisticsType.NORMAL
      }

      val projectAndSubProject = splitText.singleOrNull { text -> text.startsWith("^") && !text.startsWith("^^") }
      val project = projectAndSubProject?.split(" ")?.first()
      val subProject = projectAndSubProject?.split(" ")?.getOrNull(1)

      return NuguriStatisticsRequestParameterDto(
        userCode = userCode,
        userName = userName,
        from = fromAndTo.first(),
        to = fromAndTo.last(),
        statisticsType = statisticsType,
        workTypeDto = workTypeDto,
        userTeam = userTeam,
        projectNameOrNickname = project,
        subProjectNameOrNickname = subProject,
        teamName = splitText.singleOrNull { text -> text.startsWith("^^") && !text.startsWith("^^^") },
        memberName = splitText.singleOrNull { text -> text.startsWith("^^^") },
        isEveryBody = splitText.contains("^전체"),
        isAdmin = false // TODO
      )
    }

  }

}
