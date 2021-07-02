package com.duckvis.nuguri.domain.admin.service

import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.repository.AttendanceCardNuguriRepository
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 코어타임 시작하는 기능
 */
@Service
class StartCoreTimeService(
  @Autowired private val attendanceCardNuguriRepository: AttendanceCardNuguriRepository,
  @Autowired private val userRepository: UserRepository,
) : NuguriService {
  override val minimumRequestParams: Int
    get() = 0
  override val maximumRequestParams: Int
    get() = 0
  override val minimumPermission: ServicePermission
    get() = ServicePermission.EVERYBODY

  override fun response(serviceRequestDto: ServiceRequestDto): String {
    val notWorkingUsers = getNotLoggedIn()
    isEverybodyAbsent(notWorkingUsers)
    return if (notWorkingUsers.isEmpty()) {
      ":bell::bell:땡땡땡! 1시에요 1시! 코어타임 시작이에요~:bell::bell:\n모두 출근하셨군요~ 대단해요~!!"
    } else {
      ":bell::bell:땡땡땡! 1시에요 1시! 코어타임 시작이에요~:bell::bell:\n아직 출근하지 않은" +
        notWorkingUsers.joinToString(", ") { "<@$it.code}>" } +
        "님들은 얼른 출근 부탁해요~"
    }
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
        !it.isGone
      }
  }

  @Transactional
  fun isEverybodyAbsent(notWorkingUsers: List<User>) {
    if (notWorkingUsers.size == userRepository.findAll().count { !it.isGone }) {
      throw NuguriException(ExceptionType.NOBODY_IS_WORKING)
    }
  }
}