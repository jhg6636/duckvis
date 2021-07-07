package com.duckvis.nuguri.domain.attendance.service.v2

import com.duckvis.core.domain.nuguri.AttendanceCard
import com.duckvis.core.domain.nuguri.ProjectRepository
import com.duckvis.core.domain.nuguri.SubProjectRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.NuguriServiceRequestNoParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.core.types.shared.UserPathType
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.core.utils.dateTimeString
import com.duckvis.core.utils.dayEndTime
import com.duckvis.core.utils.secondsToString
import com.duckvis.nuguri.repository.AttendanceCardNuguriRepository
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.Duration

@Service("LAST_LOGIN_V2")
class LastLogInServiceV2(
  private val userRepository: UserRepository,
  private val attendanceCardNuguriRepository: AttendanceCardNuguriRepository,
  private val projectRepository: ProjectRepository,
  private val subProjectRepository: SubProjectRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.LAST_LOGIN

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val params = serviceRequestDto as NuguriServiceRequestNoParameterDto
    val lastLogInCard = lastLogIn(params.userCode)
    val howLongAgo = Duration.between(lastLogInCard.loginDateTime, DateTimeMaker.nowDateTime()).seconds.toInt()
    return "${params.userName}님은 ${lastProject(lastLogInCard)}에 마지막으로 출근하셨네요~\n" +
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