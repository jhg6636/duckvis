package com.duckvis.nuguri.domain.attendance.service.v2

import com.duckvis.core.domain.nuguri.AttendanceCard
import com.duckvis.core.domain.nuguri.AttendanceCardRepository
import com.duckvis.core.domain.nuguri.ProjectRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.Work
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.CommandMajorType
import com.duckvis.core.types.nuguri.service.params.NuguriAttendanceRequestParameterDto
import com.duckvis.core.types.shared.UserPathType
import com.duckvis.nuguri.domain.attendance.dtos.LogInResponseDto
import com.duckvis.nuguri.dtos.ServiceRequestDtoV2
import com.duckvis.nuguri.repository.AttendanceCardNuguriRepository
import com.duckvis.nuguri.repository.ProjectNuguriRepository
import com.duckvis.nuguri.repository.SubProjectNuguriRepository
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class LogInServiceV2(
  @Autowired private val attendanceCardRepository: AttendanceCardRepository,
  @Autowired private val attendanceCardNuguriRepository: AttendanceCardNuguriRepository,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val projectNuguriRepository: ProjectNuguriRepository,
  @Autowired private val subProjectNuguriRepository: SubProjectNuguriRepository,
  @Autowired private val projectRepository: ProjectRepository,
) : NuguriServiceV2 {

  override val majorType: CommandMajorType
    get() = CommandMajorType.ATTENDANCE

  override fun response(serviceRequestDto: ServiceRequestDtoV2): String {
    val params = serviceRequestDto.parameter
    parameterCheck(params)
    params as NuguriAttendanceRequestParameterDto

    val user = userRepository.findByCodeAndPath(params.userCode, UserPathType.SLACK) ?: throw NuguriException(
      ExceptionType.NO_SUCH_USER
    )
    val project =
      projectNuguriRepository.getProject(params.projectNameOrNickname ?: throw NuguriException(ExceptionType.TYPO))
        ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)
    val subProject = if (params.subProjectNameOrNickname == null) {
      null
    } else {
      subProjectNuguriRepository.getSubProject(params.subProjectNameOrNickname!!, project.id)
        ?: throw NuguriException(ExceptionType.NO_SUCH_SUB_PROJECT)
    }

    val work = Work(
      user.id,
      project.id,
      subProject?.id,
      params.workTypeDto
    )

    val alreadyCard = attendanceCardNuguriRepository.getMyWorkingCard(user.id)
    alreadyCard?.checkAlreadyWork(work)
    val logOutProject = projectRepository.findByIdOrNull(alreadyCard?.projectId)

    attendanceCardRepository.save(AttendanceCard(work))

    return LogInResponseDto(
      user.name,
      project.fullName,
      logOutProject?.fullName,
      subProject?.fullName
    ).responseString
  }

}