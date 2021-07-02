package com.duckvis.nuguri.domain.attendance.service

import com.duckvis.core.domain.nuguri.AttendanceCard
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.WorkTimeDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.shared.UserPathType
import com.duckvis.core.utils.*
import com.duckvis.nuguri.repository.AttendanceCardNuguriRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.transaction.Transactional

/**
 * attendanceCard를 받아서 workTimeDto 반환
 */
@Service
class WorkTimeService(
  @Autowired private val attendanceCardNuguriRepository: AttendanceCardNuguriRepository,
  @Autowired private val userRepository: UserRepository,
) {

  @Transactional
  fun workTimeSlackMessage(userCode: String): String {
    val user = userRepository.findByCodeAndPath(userCode, UserPathType.SLACK)
      ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val todayWorkTime =
      workTime(user.id, DateTimeMaker.nowDateTime().dayStartTime, DateTimeMaker.nowDateTime().dayEndTime)
    val thisWeekWorkTime =
      workTime(user.id, DateTimeMaker.nowDateTime().weekStartTime, DateTimeMaker.nowDateTime().weekEndTime)
    return ":mantelpiece_clock:오늘 ${user.name}님은 전체 프로젝트에서\n총 ${todayWorkTime.workTimeString} 근무하셨고\n" +
      ":mantelpiece_clock:이번 주에 ${user.name}님은 전체 프로젝트에서\n총 ${thisWeekWorkTime.workTimeString} 근무하셨어요"
  }

  @Transactional
  fun workTime(userId: Long, from: LocalDateTime, to: LocalDateTime): WorkTimeDto {
    val todayCards = attendanceCardNuguriRepository.getMyCardsBetween(userId, from, to)
    return getWorkTimeDto(todayCards)
  }

  fun getWorkTimeDto(cards: List<AttendanceCard>): WorkTimeDto {
    return WorkTimeDto(
      cards.sumBy { card -> card.durationSeconds ?: 0 },
      cards.filter { card -> card.isNight }.sumBy { it.durationSeconds ?: 0 },
      cards.filter { card -> card.isExtended }.sumBy { it.durationSeconds ?: 0 },
      cards.filter { card -> card.isHoliday }.sumBy { it.durationSeconds ?: 0 }
    )
  }

}