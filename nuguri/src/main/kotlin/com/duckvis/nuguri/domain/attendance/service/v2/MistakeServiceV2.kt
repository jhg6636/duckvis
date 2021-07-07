package com.duckvis.nuguri.domain.attendance.service.v2

import com.duckvis.core.domain.nuguri.AttendanceCard
import com.duckvis.core.domain.nuguri.AttendanceCardRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.Mistake
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.attendance.NuguriAttendanceRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.core.types.shared.UserPathType
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.core.utils.dayStartTime
import com.duckvis.nuguri.domain.attendance.service.WorkTimeService
import com.duckvis.nuguri.repository.ProjectNuguriRepository
import com.duckvis.nuguri.repository.SubProjectNuguriRepository
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.LocalTime

@Service("MISTAKE_V2")
class MistakeServiceV2(
  @Autowired private val attendanceCardRepository: AttendanceCardRepository,
  @Autowired private val workTimeService: WorkTimeService,
  @Autowired private val projectNuguriRepository: ProjectNuguriRepository,
  @Autowired private val subProjectNuguriRepository: SubProjectNuguriRepository,
  @Autowired private val userRepository: UserRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.MISTAKE

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val params = serviceRequestDto as NuguriAttendanceRequestParameterDto

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

    val mistake = Mistake(
      user.id,
      project.id,
      subProject?.id,
      params.mistakeSeconds ?: throw NuguriException(ExceptionType.TYPO),
      params.workTypeDto
    )

    val datetime = if (params.mistakeDate != null) {
      DateTimeMaker.nowDateTime().dayStartTime.plusHours(9)
    } else {
      LocalDateTime.of(params.mistakeDate, LocalTime.NOON)
    }

    attendanceCardRepository.save(AttendanceCard(mistake, datetime))

    return "시간을 정정했어요.\n${workTimeService.workTimeSlackMessage(user.code)}"
  }

}