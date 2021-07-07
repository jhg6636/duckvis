package com.duckvis.core.dtos.nuguri.service.params.v2.domain.statistics

import com.duckvis.core.domain.nuguri.UserTeam
import com.duckvis.core.dtos.nuguri.WorkTypeDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.SpecialStatisticsType
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
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

      val statisticsType = when {
        listOf("!월간통계", "!ㅇㄱㅌㄱ", "!drxr").contains(splitText.first()) -> SpecialStatisticsType.MONTHLY
        listOf("!주간통계", "!ㅈㄱㅌㄱ", "!wrxr").contains(splitText.first()) -> SpecialStatisticsType.WEEKLY
        splitText.contains("^모든플젝") -> SpecialStatisticsType.ALL_PROJECT
        else -> SpecialStatisticsType.NORMAL
      }

      val oneQuote = splitText.filter { text -> text.startsWith("^") && !text.startsWith("^^") }
      val projectAndSubProject = when (oneQuote.size) {
        1 -> oneQuote.singleOrNull { text -> text != "^전체" }
        2 -> oneQuote.firstOrNull() { text -> text != "^전체" }
        0 -> null
        else -> throw NuguriException(ExceptionType.TYPO)
      }
      val project = projectAndSubProject?.split(" ")?.first()?.substring(1)
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
        isAdmin = splitText.any { text -> text.startsWith("^^") } || splitText.contains("^전체")
      )
    }

  }

}
