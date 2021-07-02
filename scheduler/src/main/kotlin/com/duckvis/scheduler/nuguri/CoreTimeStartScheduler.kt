package com.duckvis.scheduler.nuguri

import com.duckvis.core.SlackConstants.Companion.NUGURI_CHANNEL
import com.duckvis.core.domain.nuguri.HolidayRepository
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.nuguri.repository.AttendanceCardNuguriRepository
import com.duckvis.slack.service.PostMessageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CoreTimeStartScheduler(
  @Autowired private val attendanceCardNuguriRepository: AttendanceCardNuguriRepository,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val holidayRepository: HolidayRepository,
  @Autowired private val postMessageService: PostMessageService,
) {

  @Scheduled(cron = "0 0 4 * * *")
  fun startCoreTime() {
    if (holidayRepository.existsByDateEquals(DateTimeMaker.nowDate())) {
      return
    }
    val notWorkingUsers = getNotLoggedIn()
    isEverybodyAbsent(notWorkingUsers)
    val message = if (notWorkingUsers.isEmpty()) {
      ":bell::bell:땡땡땡! 1시에요 1시! 코어타임 시작이에요~:bell::bell:\n모두 출근하셨군요~ 대단해요~!!"
    } else {
      ":bell::bell:땡땡땡! 1시에요 1시! 코어타임 시작이에요~:bell::bell:\n아직 출근하지 않은" +
        notWorkingUsers.joinToString(", ") { "<@${it.code}>" } +
        "님들은 얼른 출근 부탁해요~"
    }
    postMessageService.post(message, NUGURI_CHANNEL)
  }

  @Transactional
  fun getNotLoggedIn(): List<User> {
    val loggedInCards = attendanceCardNuguriRepository.getNowWorkingCards()
    return userRepository.findAll()
      .filter {
        !loggedInCards.map { card ->
          card.userId
        }.contains(it.id)
      }
      .filter {
        !it.isGone && !it.isBot
      }
  }

  @Transactional
  fun isEverybodyAbsent(notWorkingUsers: List<User>) {
    if (notWorkingUsers.size == userRepository.findAll().count { !it.isGone }) {
      throw NuguriException(ExceptionType.NOBODY_IS_WORKING)
    }
  }

}