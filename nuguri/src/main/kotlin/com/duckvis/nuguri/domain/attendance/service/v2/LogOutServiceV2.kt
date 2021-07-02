package com.duckvis.nuguri.domain.attendance.service.v2

import com.duckvis.core.domain.nuguri.ProjectRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.CommandMajorType
import com.duckvis.core.types.nuguri.service.params.NuguriAttendanceRequestParameterDto
import com.duckvis.core.types.shared.UserPathType
import com.duckvis.nuguri.domain.attendance.service.WorkTimeService
import com.duckvis.nuguri.dtos.ServiceRequestDtoV2
import com.duckvis.nuguri.repository.AttendanceCardNuguriRepository
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class LogOutServiceV2(
  @Autowired private val attendanceCardNuguriRepository: AttendanceCardNuguriRepository,
  @Autowired private val projectRepository: ProjectRepository,
  @Autowired private val workTimeService: WorkTimeService,
  @Autowired private val userRepository: UserRepository,
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
    val nowWorkingCard =
      attendanceCardNuguriRepository.getMyWorkingCard(user.id) ?: throw NuguriException(ExceptionType.NOT_WORKING)
    val isOverTwelve = nowWorkingCard.logOut()
    val alertMessage = if (isOverTwelve) {
      "\n12시간 이상 출근해 계셨군요. 퇴근을 찍지 않으신 건지 확인 부탁드려요~"
    } else {
      ""
    }
    val project = projectRepository.findByIdOrNull(nowWorkingCard.projectId)!!

    return "${project.fullName} 프로젝트에 대하여 퇴근하셨군요.\n${nowWorkingCard.durationString} 동안 고생 많았어요.\n" +
      "${workTimeService.workTimeSlackMessage(user.code)}$alertMessage"
  }
}