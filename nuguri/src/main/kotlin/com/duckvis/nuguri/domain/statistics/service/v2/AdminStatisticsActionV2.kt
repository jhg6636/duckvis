package com.duckvis.nuguri.domain.statistics.service.v2

import com.duckvis.core.domain.nuguri.AttendanceCard
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.WorkTypeDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.params.NuguriStatisticsRequestParameterDto
import com.duckvis.core.types.shared.UserPathType
import com.duckvis.nuguri.domain.attendance.dtos.MemberWorkDuration
import com.duckvis.nuguri.domain.statistics.dtos.AdminStatisticsResponseDto
import com.duckvis.nuguri.domain.statistics.dtos.WorkTypeDuration
import com.duckvis.nuguri.repository.ProjectNuguriRepository
import com.duckvis.nuguri.repository.StatisticsNuguriRepository
import com.duckvis.nuguri.repository.SubProjectNuguriRepository
import com.duckvis.nuguri.repository.UserNuguriRepository
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
) {

  fun act(params: NuguriStatisticsRequestParameterDto): String {
    val admin = userRepository.findByCodeAndPath(params.userCode, UserPathType.SLACK)
      ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val project = projectNuguriRepository.getProject(params.projectNameOrNickname)
    val subProject = subProjectNuguriRepository.getSubProject(params.subProjectNameOrNickname, project?.id)
    val member = userNuguriRepository.getMember(params.memberName)
    val cards = statisticsNuguriRepository.getAllCheckableCards(params, admin, project, subProject, member)
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

}