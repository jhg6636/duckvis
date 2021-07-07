package com.duckvis.nuguri.domain.statistics.service.v2

import com.duckvis.core.domain.nuguri.ProjectRepository
import com.duckvis.core.domain.nuguri.UserProfileRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.WorkTimeDto
import com.duckvis.core.dtos.nuguri.WorkTypeDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.statistics.NuguriStatisticsRequestParameterDto
import com.duckvis.core.types.nuguri.SpecialStatisticsType
import com.duckvis.core.types.shared.UserPathType
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.core.utils.monthEndTime
import com.duckvis.nuguri.domain.statistics.dtos.StatisticsResponseDto
import com.duckvis.nuguri.repository.AttendanceCardNuguriRepository
import com.duckvis.nuguri.repository.HolidayNuguriRepository
import com.duckvis.nuguri.repository.ProjectNuguriRepository
import com.duckvis.nuguri.repository.SubProjectNuguriRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class NormalStatisticsActionV2(
  @Autowired private val attendanceCardNuguriRepository: AttendanceCardNuguriRepository,
  @Autowired private val projectNuguriRepository: ProjectNuguriRepository,
  @Autowired private val subProjectNuguriRepository: SubProjectNuguriRepository,
  @Autowired private val userProfileRepository: UserProfileRepository,
  @Autowired private val holidayNuguriRepository: HolidayNuguriRepository,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val projectRepository: ProjectRepository,
) {

  fun act(params: NuguriStatisticsRequestParameterDto): String {
    val user = userRepository.findByCodeAndPath(params.userCode, UserPathType.SLACK) ?: throw NuguriException(
      ExceptionType.NO_SUCH_USER
    )
    val project = projectNuguriRepository.getProject(params.projectNameOrNickname)
    val subProject = subProjectNuguriRepository.getSubProject(params.subProjectNameOrNickname, project?.id)
    val myCards = attendanceCardNuguriRepository.getMyCardsBetween(user.id, params.from, params.to)
      .filter { card ->
        (params.workTypeDto == WorkTypeDto() || card.isSameWorkType(params.workTypeDto)) &&
          (project == null || project.id == card.projectId) &&
          (subProject == null || subProject.id == card.subProjectId)
      }

    val userProfile = userProfileRepository.findByUserId(user.id) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val leftDays =
      holidayNuguriRepository.leftDays(DateTimeMaker.nowDate(), DateTimeMaker.nowDateTime().monthEndTime.toLocalDate())

    if (params.statisticsType == SpecialStatisticsType.ALL_PROJECT) {
      val groupedByProjectId = myCards.groupBy { card -> card.projectId }
      groupedByProjectId.entries.joinToString("\n\n") { (projectId, cards) ->
        val project = projectRepository.findByIdOrNull(projectId) ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)
        "해당 기간에 ${project.name} 프로젝트에서 일한 기록이에요~\n" +
          StatisticsResponseDto(
            WorkTimeDto.of(cards),
            userProfile.targetWorkSeconds,
            SpecialStatisticsType.NORMAL,
            user.name,
            leftDays
          ).responseMessage
      }
    }

    return StatisticsResponseDto(
      WorkTimeDto.of(myCards),
      userProfile.targetWorkSeconds,
      params.statisticsType,
      user.name,
      leftDays
    )
      .responseMessage
  }

}