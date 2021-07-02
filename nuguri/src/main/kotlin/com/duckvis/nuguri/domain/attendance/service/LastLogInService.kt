package com.duckvis.nuguri.domain.attendance.service

import com.duckvis.core.domain.nuguri.AttendanceCard
import com.duckvis.core.domain.nuguri.ProjectRepository
import com.duckvis.core.domain.nuguri.SubProjectRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.shared.UserPathType
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.core.utils.dateTimeString
import com.duckvis.core.utils.dayEndTime
import com.duckvis.core.utils.secondsToString
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.repository.AttendanceCardNuguriRepository
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.Duration
import javax.transaction.Transactional

/**
 * !마지막퇴근 기능
 */
@Service("LAST_LOGIN")
class LastLogInService(
  @Autowired private val userRepository: UserRepository,
  @Autowired private val attendanceCardNuguriRepository: AttendanceCardNuguriRepository,
  @Autowired private val projectRepository: ProjectRepository,
  @Autowired private val subProjectRepository: SubProjectRepository,
) : NuguriService {

  override val minimumRequestParams: Int
    get() = 0
  override val maximumRequestParams: Int
    get() = 0
  override val minimumPermission: ServicePermission
    get() = ServicePermission.MEMBER

  @Transactional
  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    val lastLogInCard = lastLogIn(serviceRequestDto.userCode)
    val howLongAgo = Duration.between(lastLogInCard.loginDateTime, DateTimeMaker.nowDateTime()).seconds.toInt()
    return "${serviceRequestDto.userName}님은 ${lastProject(lastLogInCard)}에 마지막으로 출근하셨네요~\n" +
      ":mantelpiece_clock:${lastLogInCard.loginDateTime.dateTimeString}에 출근하셨네요~\n" +
      "지금으로부터 ${howLongAgo.secondsToString} 전이니 참고해 주세요~"
  }

  fun lastLogIn(userCode: String): AttendanceCard {
    val user = userRepository.findByCodeAndPath(userCode, UserPathType.SLACK)
      ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    return attendanceCardNuguriRepository.getMyCardsBetween(
      user.id,
      DateTimeMaker.nowDateTime().minusDays(15),
      DateTimeMaker.nowDateTime().dayEndTime
    )
      .maxByOrNull { attendanceCard -> attendanceCard.loginDateTime }
      ?: throw NuguriException(ExceptionType.NEVER_ATTENDED)
  }

  fun lastProject(attendanceCard: AttendanceCard): String {
    val project =
      projectRepository.findByIdOrNull(attendanceCard.projectId) ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)
    if (attendanceCard.subProjectId == null) {
      return "${project.fullName} 프로젝트"
    }
    val subProject = subProjectRepository.findByIdOrNull(attendanceCard.subProjectId)?.fullName ?: ""
    return "${project.fullName} 프로젝트 ($subProject)"
  }

}