package com.duckvis.nuguri.domain.statistics.service.v2

import com.duckvis.core.domain.nuguri.AttendanceCard
import com.duckvis.core.domain.nuguri.ProjectRepository
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.WorkTypeDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.statistics.NuguriStatisticsRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.SpecialStatisticsType
import com.duckvis.core.types.shared.UserPathType
import com.duckvis.nuguri.domain.attendance.dtos.MemberWorkDuration
import com.duckvis.nuguri.domain.statistics.dtos.AdminStatisticsResponseDto
import com.duckvis.nuguri.domain.statistics.dtos.WorkTypeDuration
import com.duckvis.nuguri.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AdminStatisticsActionV2(
  @Autowired private val userRepository: UserRepository,
  @Autowired private val projectNuguriRepository: ProjectNuguriRepository,
  @Autowired private val subProjectNuguriRepository: SubProjectNuguriRepository,
  @Autowired private val statisticsNuguriRepository: StatisticsNuguriRepository,
  @Autowired private val userNuguriRepository: UserNuguriRepository,
  @Autowired private val userTeamNuguriRepository: UserTeamNuguriRepository,
  @Autowired private val userTeamRepository: UserTeamRepository,
  @Autowired private val projectRepository: ProjectRepository,
) {

  fun act(params: NuguriStatisticsRequestParameterDto): String {
    val admin = userRepository.findByCodeAndPath(params.userCode, UserPathType.SLACK)
      ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val project = projectNuguriRepository.getProject(params.projectNameOrNickname)
    val subProject = subProjectNuguriRepository.getSubProject(params.subProjectNameOrNickname, project?.id)
    val member = userNuguriRepository.getMember(params.memberName)
    val memberUserTeam = userTeamRepository.findAllByUserId(member?.id ?: 0L)

    if (!admin.isAdmin && memberUserTeam.all { userTeam -> params.userTeam?.teamId != userTeam.teamId }) {
      throw NuguriException(ExceptionType.NOT_TEAM_MEMBER)
    }
    val memberIds = when {
      member != null -> listOf(member.id)
      admin.isAdmin -> userRepository.findAll().filter { user -> !user.isBot && !user.isGone }
        .map { user -> user.id }
      else -> userTeamRepository.findAllByTeamId(params.userTeam?.teamId ?: 0L)
        .map { userTeam -> userTeam.userId }
    }
    if (params.statisticsType == SpecialStatisticsType.ALL_PROJECT) {
      val cards = statisticsNuguriRepository.getAllCheckableCards(params, admin, null, null, memberIds, params.from, params.to)
      return sortAllProjectCards(cards).entries.joinToString ("\n\n") { (projectId, memberWorkDurations) ->
        val thisProject = projectRepository.findByIdOrNull(projectId) ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)
        "해당 기간에 ${thisProject.name} 프로젝트에서 일한 기록이에요~\n\n" +
          AdminStatisticsResponseDto(memberWorkDurations, SpecialStatisticsType.NORMAL).responseString
      }
    }
    val cards = statisticsNuguriRepository.getAllCheckableCards(params, admin, project, subProject, memberIds, params.from, params.to)
    val memberWorkDurations = sortCards(cards)

    return AdminStatisticsResponseDto(memberWorkDurations, params.statisticsType)
      .responseString
  }

  private fun sortCards(cards: List<AttendanceCard>): List<MemberWorkDuration> {
    // userId -> List<WorkTypeDuration>
    val allUserDurations = cards
      .groupBy(
        { card -> card.userId },
        { card ->
          WorkTypeDuration(
            WorkTypeDto(card.isNight, card.isExtended, card.isHoliday),
            card.durationSeconds ?: 0
          )
        }
      )

    return allUserDurations.map { (userId, workTypeDurations) ->
      val userName = userRepository.findByIdOrNull(userId)?.name ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
      MemberWorkDuration.of(userName, workTypeDurations)
    }
  }

  private fun sortAllProjectCards(cards: List<AttendanceCard>): Map<Long, List<MemberWorkDuration>> {
    // projectId -> List<Card>
    val groupedByProject = cards.groupBy { card -> card.projectId }
    return groupedByProject.mapValues { (_, cards) ->
      sortCards(cards)
    }
  }

}