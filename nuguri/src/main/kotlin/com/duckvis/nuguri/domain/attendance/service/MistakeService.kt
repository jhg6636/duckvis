package com.duckvis.nuguri.domain.attendance.service

import com.duckvis.core.domain.nuguri.*
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.Mistake
import com.duckvis.core.dtos.nuguri.WorkTypeDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.shared.UserPathType
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.core.utils.StartAndEndTime.ERROR_SAFE_MARGIN
import com.duckvis.core.utils.dayStartTime
import com.duckvis.core.utils.toDurationSeconds
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.transaction.Transactional

/**
 * 실수 기능
 */

@Service("MISTAKE")
class MistakeService(
  @Autowired private val workTimeService: WorkTimeService,
  @Autowired private val attendanceCardRepository: AttendanceCardRepository,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val projectRepository: ProjectRepository,
  @Autowired private val subProjectRepository: SubProjectRepository,
) : NuguriService {

  override val minimumRequestParams: Int
    get() = 2
  override val maximumRequestParams: Int
    get() = 6
  override val minimumPermission: ServicePermission
    get() = ServicePermission.MEMBER

  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    val projectSubProject = serviceRequestDto.params[0].split("_")
    val project = extractProject(projectSubProject)
    val subProjectname = projectSubProject.getOrNull(1)?.also { nameOrNickname ->
      val subProject = subProjectRepository.findByNameAndProjectId(nameOrNickname, project.id)
        ?: subProjectRepository.findByNicknameAndProjectId(nameOrNickname, project.id)
        ?: throw NuguriException(ExceptionType.NO_SUCH_SUB_PROJECT)
      "(${subProject.name} 서브 프로젝트)"
    } ?: ""
    val mistake = makeMistake(serviceRequestDto.params, serviceRequestDto.userCode)
    val params = serviceRequestDto.params - extractOptions(serviceRequestDto.params)
    val date = if (params.size == 3) {
      DateTimeMaker.stringToDate(params[2])
    } else {
      null
    }
    if (date != null && date > DateTimeMaker.nowDate()) {
      throw NuguriException(ExceptionType.MISTAKE_FUTURE)
    }
    saveMistakeCard(mistake, date)

    return "${project.name} 프로젝트${subProjectname}의 시간을 정정했어요~\n${workTimeService.workTimeSlackMessage(serviceRequestDto.userCode)}"
  }

  override fun checkTypo(serviceRequestDto: ServiceRequestDto) {
    super.checkTypo(serviceRequestDto)
    if ((serviceRequestDto.params - extractOptions(serviceRequestDto.params)).size > 3) {
      throw NuguriException(ExceptionType.TYPO)
    }
  }

  private fun makeMistake(requestParams: List<String>, userCode: String): Mistake {
    val params = requestParams - extractOptions(requestParams)
    val user = userRepository.findByCodeAndPath(userCode, UserPathType.SLACK)
      ?: throw NuguriException(ExceptionType.NO_SUCH_USER)

    val projectSubProject = params[0].split("_")
    val project = extractProject(projectSubProject)
    val durationSeconds = if (params[1].startsWith("-")) {
      -params[1].substringAfter("-").toDurationSeconds
    } else {
      params[1].toDurationSeconds
    }
    val workTypeDto = WorkTypeDto.of(extractOptions(requestParams))

    val subProjectNameOrNickname = projectSubProject.getOrNull(1)
      ?: return Mistake(user.id, project.id, null, durationSeconds, workTypeDto)

    val subProjectId = extractSubProjectId(subProjectNameOrNickname, project)

    return Mistake(user.id, project.id, subProjectId, durationSeconds, workTypeDto)
  }

  private fun extractProject(projectSubProject: List<String>): Project {
    val projectNameOrNickname = projectSubProject.first()
    return projectRepository.findByNameOrNickname(projectNameOrNickname, projectNameOrNickname)
      ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)
  }

  private fun extractSubProjectId(subProjectNameOrNickname: String, project: Project): Long {
    return subProjectRepository.findByNameAndProjectId(subProjectNameOrNickname, project.id)?.id
      ?: subProjectRepository.findByNicknameAndProjectId(subProjectNameOrNickname, project.id)?.id
      ?: throw NuguriException(ExceptionType.NO_SUCH_SUB_PROJECT)
  }

  private fun extractOptions(requestParams: List<String>): List<String> {
    return requestParams.filter { param -> param.startsWith("%") }
  }

  @Transactional
  fun saveMistakeCard(mistake: Mistake, date: LocalDate?): AttendanceCard {
    val localDateTime = if (date == null) {
      DateTimeMaker.nowDateTime().dayStartTime.plusHours(ERROR_SAFE_MARGIN)
    } else {
      LocalDateTime.of(date, LocalTime.MIDNIGHT)
    }
    return attendanceCardRepository.save(AttendanceCard(mistake, localDateTime))
  }

  override fun isAboutAttendance(): Boolean {
    return true
  }

}