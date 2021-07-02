package com.duckvis.nuguri.domain.attendance.service

import com.duckvis.core.domain.nuguri.ProjectRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.shared.UserPathType
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.repository.AttendanceCardNuguriRepository
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * 퇴근 기능
 */
@Service("LOGOUT")
class LogOutService(
  @Autowired private val attendanceCardNuguriRepository: AttendanceCardNuguriRepository,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val projectRepository: ProjectRepository,
  @Autowired private val workTimeService: WorkTimeService,
) : NuguriService {

  override val minimumRequestParams: Int
    get() = 0
  override val maximumRequestParams: Int
    get() = 0
  override val minimumPermission: ServicePermission
    get() = ServicePermission.MEMBER

  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    val logOutMessage = logOut(serviceRequestDto.userCode)

    return "${logOutMessage}\n\n${workTimeService.workTimeSlackMessage(serviceRequestDto.userCode)}"
  }

  @Transactional
  fun logOut(userCode: String): String {
    val user = userRepository.findByCodeAndPath(userCode, UserPathType.SLACK)
      ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val nowWorkingCard =
      attendanceCardNuguriRepository.getMyWorkingCard(user.id) ?: throw NuguriException(ExceptionType.NOT_WORKING)
    val overTwelve = nowWorkingCard.logOut()
    val project =
      projectRepository.findByIdOrNull(nowWorkingCard.projectId) ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)
    val alertMessage = if (overTwelve) {
      "\n12시간 이상 출근해 계셨군요. 퇴근을 찍지 않으신 건지 확인 부탁드려요~\n"
    } else {
      ""
    }
    return "${project.fullName} 프로젝트에 대하여 퇴근하셨군요.\n$alertMessage\n${nowWorkingCard.durationString}" +
      " 동안 고생 많았어요."
  }

  override fun isAboutAttendance(): Boolean {
    return true
  }

}