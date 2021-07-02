package com.duckvis.nuguri.ui

import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.domain.nuguri.UserTeam
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.shared.UserPathType
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.core.utils.monthEndTime
import com.duckvis.core.utils.monthStartTime
import com.duckvis.core.utils.weekEndTime
import com.duckvis.nuguri.dtos.ServiceRequestDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.format.DateTimeFormatter

@Service
class CommandParser(
  @Autowired private val userRepository: UserRepository,
  @Autowired private val userTeamRepository: UserTeamRepository,
  @Autowired private val teamRepository: TeamRepository
) {

  /**
   * text 예시 ㄲ ㄱㅂ
   * THINKING MutableList 이상하다
   */
  @Transactional
  fun toRequestDto(text: String, userCode: String): ServiceRequestDto {
    val requestParams = text.split(" ").toMutableList()
    requestParams.removeAt(0)


    if (requestParams.size > 0 && (requestParams[0] == "상메" || requestParams[0] == "상태메시지")) {
      val message = requestParams.subList(1, requestParams.size).joinToString(" ")
      requestParams.clear()
      requestParams.addAll(listOf("상태메시지", message))
    }

    if (text.startsWith("!ㅇㄱㅌㄱ") || text.startsWith("!월간통계") || text.startsWith("!drxr")) {
      val monthEnd = DateTimeMaker.nowDateTime().monthEndTime.toLocalDate()
      val monthStart = monthEnd.withDayOfMonth(1)
      requestParams.add(0, monthEnd.format(DateTimeFormatter.ofPattern("MMdd")))
      requestParams.add(0, monthStart.format(DateTimeFormatter.ofPattern("MMdd")))
    }

    if (text.startsWith("!ㅈㄱㅌㄱ") || text.startsWith("!주간통계") || text.startsWith("!wrxr")) {
      val weekStart = DateTimeMaker.nowDate().minusDays(DateTimeMaker.nowDate().dayOfWeek.value.toLong() - 1)
      val weekEnd = DateTimeMaker.nowDateTime().weekEndTime.toLocalDate()
      requestParams.add(0, weekEnd.format(DateTimeFormatter.ofPattern("MMdd")))
      requestParams.add(0, weekStart.format(DateTimeFormatter.ofPattern("MMdd")))
    }

    val user = userRepository.findByCodeAndPath(userCode, UserPathType.SLACK)
      ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val userTeam = extractUserTeam(text, userCode)

    return ServiceRequestDto(
      text,
      requestParams,
      user.level,
      userTeam,
      user.name,
      user.code
    )
  }

  // THINKING, UserTaem을 ServiceRequestDto에 넣어줘야 하는가?!
  @Transactional
  fun extractUserTeam(text: String, userCode: String): UserTeam? {
    val user = userRepository.findByCodeAndPath(userCode, UserPathType.SLACK)
      ?: throw NuguriException(ExceptionType.NO_SUCH_USER)

    val userTeams = userTeamRepository.findAllByUserId(user.id)

    return when (userTeams.size) {
      1 -> userTeamRepository.findAllByUserId(user.id).single()
      0 -> null
      // Acommand Team1 User1
      else -> {
        val teamName = text.split(" ")
          .firstOrNull { teamRepository.findAll().map { team -> team.name }.contains(it) }
          ?: return userTeams.first()
        val team = teamRepository.findByName(teamName) ?: throw NuguriException(ExceptionType.NO_SUCH_TEAM)
        userTeamRepository.findByUserIdAndTeamId(user.id, team.id)
      }
    }
  }

}