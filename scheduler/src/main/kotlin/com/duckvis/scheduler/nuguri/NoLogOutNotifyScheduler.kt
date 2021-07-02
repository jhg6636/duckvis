package com.duckvis.scheduler.nuguri

import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.utils.dateTimeString
import com.duckvis.nuguri.repository.AttendanceCardNuguriRepository
import com.duckvis.slack.service.PostMessageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class NoLogOutNotifyScheduler(
  @Autowired private val attendanceCardNuguriRepository: AttendanceCardNuguriRepository,
  @Autowired private val postMessageService: PostMessageService,
  @Autowired private val userRepository: UserRepository,
) {

  companion object {
    const val OVER_WORKING = 8 * 60 * 60
  }

  /**
   * 8시간 이상 퇴근하지 않은 사람에게 dm 알림
   */
  @Scheduled(cron = "0 0 0-12 * * *")
  fun noLogOutNotify() {
    val nowWorkingCards = attendanceCardNuguriRepository.getNowWorkingCards()
    val overWorked = nowWorkingCards.filter { card -> card.nowWorkingSeconds > OVER_WORKING }

    overWorked.forEach { attendanceCard ->
      val user =
        userRepository.findByIdOrNull(attendanceCard.userId) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
      val message = "${attendanceCard.loginDateTime.dateTimeString} 이후로 퇴근하지 않으셨어요~\n퇴근을 찍지 않으신 거 아닌지 확인 부탁드려요~"
      postMessageService.post(message, user.code)
    }
  }

}