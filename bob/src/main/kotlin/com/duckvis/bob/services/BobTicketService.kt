package com.duckvis.bob.services

import com.duckvis.core.domain.bob.BobTicket
import com.duckvis.core.domain.bob.BobTicketQueryDslRepository
import com.duckvis.core.domain.bob.BobTicketRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.bob.BobTicketAlreadyExistsException
import com.duckvis.core.exceptions.bob.NoBobTicketException
import com.duckvis.core.exceptions.bob.NoSuchUserException
import com.duckvis.core.exceptions.bob.NotRegisteredUserException
import com.duckvis.core.types.bob.BobStyleType
import com.duckvis.core.types.bob.BobTimeType
import com.duckvis.core.types.shared.CityType
import com.duckvis.core.utils.DateTimeMaker
import org.hibernate.exception.ConstraintViolationException
import org.springframework.context.annotation.ComponentScan
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.retry.annotation.EnableRetry
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@ComponentScan(basePackages = ["com.duckvis"])
@EnableRetry
class BobTicketService(
  private val bobTicketRepository: BobTicketRepository,
  private val bobTicketQueryDslRepository: BobTicketQueryDslRepository,
  private val userRepository: UserRepository,
  private val bobMoneyService: BobMoneyService,
) {

  // TODO isolation 옵션 조정
  // TODO now를 테스트 코드를 잘 작성할 수 있으면서도 뺄 수 있을까? "랜덤한 요소"를 어떻게 테스트 할거냐
  @Transactional
  @Retryable(include = [ConstraintViolationException::class, DataIntegrityViolationException::class])
  fun responseMe(userId: Long, bobStyle: BobStyleType, now: LocalDateTime = DateTimeMaker.nowDateTime()): BobTicket {
    val user = userRepository.findByIdOrNull(userId) ?: throw NotRegisteredUserException()
    return saveTicket(userId, bobStyle, user.city, now)
  }

  @Transactional
  @Retryable(include = [ConstraintViolationException::class, DataIntegrityViolationException::class])
  fun responseMeSpecificPlace(
    userId: Long,
    bobStyle: BobStyleType,
    city: CityType,
    now: LocalDateTime = DateTimeMaker.nowDateTime()
  ): BobTicket {
    return saveTicket(userId, bobStyle, city, now)
  }

  @Transactional
  fun responseNotMe(userId: Long, now: LocalDateTime = DateTimeMaker.nowDateTime()) {
    bobTicketQueryDslRepository.getThisMealTicket(userId, BobTimeType.of(now))
      ?.let { bobTicketRepository.delete(it) }
      ?: throw NoBobTicketException()
  }

  private fun saveTicket(userId: Long, styleType: BobStyleType, city: CityType, now: LocalDateTime): BobTicket {
    if (bobTicketQueryDslRepository.isExistingTicket(userId, BobTimeType.of(now), styleType, city)) {
      throw BobTicketAlreadyExistsException()
    }

    val thisMealTicket = bobTicketQueryDslRepository.getThisMealTicket(userId, BobTimeType.of(now))
    if (thisMealTicket != null) {
      bobTicketRepository.delete(thisMealTicket)
    }

    val user = userRepository.findByIdOrNull(userId) ?: throw NoSuchUserException()
    val myOrderNumber = myOrderNumber(now)

    return bobTicketRepository.save(
      BobTicket(
        userId,
        user.name,
        user.tagString,
        now.toLocalDate(),
        now.toLocalTime(),
        BobTimeType.of(now),
        styleType,
        bobMoneyService.getPayType(userId),
        city,
        myOrderNumber
      )
    )
  }

  private fun myOrderNumber(now: LocalDateTime): Int {
    return (bobTicketQueryDslRepository.findThisMealAllTickets(BobTimeType.of(now))
      .maxOfOrNull { ticket -> ticket.issuedOrderNumber } ?: 0) + 1
  }

}
