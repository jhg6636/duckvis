package com.duckvis.bob

import com.duckvis.bob.services.BobTeamService
import com.duckvis.bob.services.BobTicketService
import com.duckvis.core.domain.bob.BobTicket
import com.duckvis.core.domain.bob.BobTicketRepository
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.bob.BobTicketAlreadyExistsException
import com.duckvis.core.exceptions.bob.NoBobTicketException
import com.duckvis.core.types.bob.BobStyleType
import com.duckvis.core.types.bob.BobTimeType
import com.duckvis.core.types.bob.IssuedOrderType
import com.duckvis.core.types.bob.PayType
import com.duckvis.core.types.shared.CityType
import com.duckvis.core.utils.DateTimeMaker
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import javax.transaction.Transactional

@SpringBootTest
@ActiveProfiles("test")
open class TicketingTest @Autowired constructor(
  val bobTicketService: BobTicketService,
  val bobTicketRepository: BobTicketRepository,
  val bobTeamService: BobTeamService,
  val userRepository: UserRepository,
) {
  lateinit var user1: User
  lateinit var user2: User
  lateinit var user3: User
  lateinit var daejeonUser: User

  @Transactional
  fun ticketing(
    user: User,
    issuedOrderNumber: Int,
    styleType: BobStyleType = BobStyleType.ANYTHING,
    cityType: CityType = CityType.SEOUL
  ): BobTicket {
    return BobTicket(
      user.id,
      user.name,
      user.tagString,
      DateTimeMaker.nowDate(),
      DateTimeMaker.nowDateTime().toLocalTime().withHour(7),
      BobTimeType.DINNER,
      styleType,
      PayType.SELECTSTAR,
      cityType,
      issuedOrderNumber = issuedOrderNumber
    )
      .let {
        bobTicketRepository.save(it)
      }
  }

  @BeforeEach
  @Transactional
  open fun prepareEachTest() {
    user1 = User("u1", "1번 유저").let { userRepository.save(it) }
    user2 = User("u2", "2번 유저").let { userRepository.save(it) }
    user3 = User("u3", "3번 유저").let { userRepository.save(it) }
    daejeonUser = User("u4", "대전 유저", CityType.DAEJEON).let { userRepository.save(it) }
  }

  @AfterEach
  fun cleanUpEachTest() {
    bobTicketRepository.deleteAll()
    userRepository.deleteAll()
  }

  private fun fourOClock(): LocalDateTime {
    return DateTimeMaker.nowDateTime().withHour(7)
  }

  @Test
  fun `이번 타임에 티켓이 없는데 저요 했다`() {
    // given

    // when
    val ticket = bobTicketService.responseMe(user1.id, BobStyleType.ANYTHING, fourOClock())

    // then
    assertThat(ticket.bobStyle).isEqualTo(BobStyleType.ANYTHING)
    assertThat(ticket.city).isEqualTo(CityType.SEOUL)
    assertThat(bobTicketRepository.count()).isEqualTo(1)
  }

  @Test
  fun `이번 타임에 티켓이 없는데 채식 했다`() {
    // given

    // when
    val ticket = bobTicketService.responseMe(user1.id, BobStyleType.VEGETARIAN, fourOClock())

    // then
    assertThat(ticket.bobStyle).isEqualTo(BobStyleType.VEGETARIAN)
    assertThat(ticket.city).isEqualTo(CityType.SEOUL)
    assertThat(bobTicketRepository.count()).isEqualTo(1)
  }

  @Test
  fun `이번 타임에 티켓이 없는데 안먹 했다`() {
    // given

    // when & then
    assertThrows<NoBobTicketException> {
      bobTicketService.responseNotMe(user1.id, fourOClock())
    }
  }

  @Test
  fun `이번 타임에 티켓이 없는데 서울에서 했다`() {
    // given
    ticketing(user2, 1)

    // when
    val ticket = bobTicketService.responseMeSpecificPlace(user1.id, BobStyleType.ANYTHING, CityType.SEOUL, fourOClock())

    // then
    assertThat(ticket.bobStyle).isEqualTo(BobStyleType.ANYTHING)
    assertThat(ticket.city).isEqualTo(CityType.SEOUL)
    assertThat(bobTicketRepository.count()).isEqualTo(2)
  }

  @Test
  fun `이번 타임에 티켓이 없는데 대전에서 했다`() {
    // given
    ticketing(user2, 1)

    // when
    val ticket =
      bobTicketService.responseMeSpecificPlace(user1.id, BobStyleType.ANYTHING, CityType.DAEJEON, fourOClock())

    // then
    assertThat(ticket.bobStyle).isEqualTo(BobStyleType.ANYTHING)
    assertThat(ticket.city).isEqualTo(CityType.DAEJEON)
    assertThat(bobTicketRepository.count()).isEqualTo(2)
  }

  @Test
  fun `이번 타임에 티켓이 없는데 서울채식 했다`() {
    // given
    ticketing(user1, 1)
    ticketing(user3, 2)

    // when
    val ticket =
      bobTicketService.responseMeSpecificPlace(user2.id, BobStyleType.VEGETARIAN, CityType.SEOUL, fourOClock())

    // then
    assertThat(ticket.bobStyle).isEqualTo(BobStyleType.VEGETARIAN)
    assertThat(ticket.city).isEqualTo(CityType.SEOUL)
    assertThat(bobTicketRepository.count()).isEqualTo(3)
  }

  @Test
  fun `이번 타임에 티켓이 없는데 대전채식 했다`() {
    // given
    ticketing(user1, 1)
    ticketing(user3, 2)

    // when
    val ticket =
      bobTicketService.responseMeSpecificPlace(user2.id, BobStyleType.VEGETARIAN, CityType.DAEJEON, fourOClock())

    // then
    assertThat(ticket.bobStyle).isEqualTo(BobStyleType.VEGETARIAN)
    assertThat(ticket.city).isEqualTo(CityType.DAEJEON)
    assertThat(bobTicketRepository.count()).isEqualTo(3)
  }

  @Test
  fun `이번 타임에 아무거나 티켓이 있는데 안먹 했다`() {
    // given
    ticketing(user1, 1)
    ticketing(user2, 2)
    println(bobTicketRepository.count())
    println("user1 id")
    println(user1.id)
    // when
    bobTicketService.responseNotMe(user1.id, fourOClock())

    // then
    assertThat(bobTicketRepository.count()).isEqualTo(1)
  }

  @Test
  fun `이번 타임에 채식 티켓이 있는데 안먹 했다`() {
    // given
    ticketing(user1, 1)
    ticketing(user2, 2, BobStyleType.VEGETARIAN)

    // when
    bobTicketService.responseNotMe(user2.id, fourOClock())

    // then
    assertThat(bobTicketRepository.count()).isEqualTo(1)
  }

  @Test
  fun `이번 타임에 아무거나 티켓이 있는데 저요 했다`() {
    // given
    ticketing(user1, 1)

    // when & then
    assertThrows<IllegalStateException> {
      bobTicketService.responseMe(user1.id, BobStyleType.ANYTHING, fourOClock())
    }
    try {
      bobTicketService.responseMe(user1.id, BobStyleType.ANYTHING, fourOClock())
    } catch (e: IllegalStateException) {
      assertThat(e.cause!!.javaClass).isEqualTo(BobTicketAlreadyExistsException::class.java)
    }
  }

  @Test
  fun `이번 타임에 아무거나 티켓이 있는데 채식 했다`() {
    // given
    ticketing(user1, 1)

    // when
    val ticket = bobTicketService.responseMe(user1.id, BobStyleType.VEGETARIAN, fourOClock())

    // then
    assertThat(ticket.bobStyle).isEqualTo(BobStyleType.VEGETARIAN)
    assertThat(ticket.city).isEqualTo(CityType.SEOUL)
    assertThat(bobTicketRepository.count()).isEqualTo(1)
  }

  @Test
  fun `이번 타임에 채식 티켓이 있는데 저요 했다`() {
    // given
    ticketing(user1, 1, BobStyleType.VEGETARIAN)

    // when
    val ticket = bobTicketService.responseMe(user1.id, BobStyleType.ANYTHING, fourOClock())

    // then
    assertThat(ticket.bobStyle).isEqualTo(BobStyleType.ANYTHING)
    assertThat(ticket.city).isEqualTo(CityType.SEOUL)
    assertThat(bobTicketRepository.count()).isEqualTo(1)
  }

  @Test
  fun `이번 타임에 채식 티켓이 있는데 채식 했다`() {
    // given
    ticketing(user1, 1, BobStyleType.VEGETARIAN)

    // when & then
    assertThrows<IllegalStateException> {
      bobTicketService.responseMe(user1.id, BobStyleType.VEGETARIAN, fourOClock())
    }
    try {
      bobTicketService.responseMe(user1.id, BobStyleType.VEGETARIAN, fourOClock())
    } catch (e: IllegalStateException) {
      assertThat(e.cause!!.javaClass).isEqualTo(BobTicketAlreadyExistsException::class.java)
    }
  }

  @Test
  @Transactional
  fun `이번 타임에 아무도 투표 안한 상황에서 투표했을 때 1등이 된다`() {
    // given
    val ticket1 = bobTicketService.responseMe(user1.id, BobStyleType.ANYTHING, fourOClock())
    val ticket2 = bobTicketService.responseMe(user2.id, BobStyleType.ANYTHING, fourOClock())
    val ticket3 = bobTicketService.responseMe(user3.id, BobStyleType.ANYTHING, fourOClock())

    // when
    bobTeamService.determineBobTeam(CityType.SEOUL, BobTimeType.DINNER)

    // then
    assertThat(ticket1.issuedOrder).isEqualTo(IssuedOrderType.FIRST)
  }

  @Test
  fun `서울 사람이 이번 타임에 아무거나 서울 티켓이 있는데 저요 했다`() {
    // given
    ticketing(user1, 1)

    // when & then
    assertThrows<IllegalStateException> {
      bobTicketService.responseMe(user1.id, BobStyleType.ANYTHING, fourOClock())
    }
    try {
      bobTicketService.responseMe(user1.id, BobStyleType.ANYTHING, fourOClock())
    } catch (e: IllegalStateException) {
      assertThat(e.cause!!.javaClass).isEqualTo(BobTicketAlreadyExistsException::class.java)
    }
  }

  @Test
  fun `대전 사람이 이번 타임에 아무거나 서울 티켓이 있는데 저요 했다`() {
    // given
    ticketing(daejeonUser, 1)

    // when
    val ticket = bobTicketService.responseMe(daejeonUser.id, BobStyleType.ANYTHING, fourOClock())

    // then
    assertThat(ticket.bobStyle).isEqualTo(BobStyleType.ANYTHING)
    assertThat(ticket.city).isEqualTo(CityType.DAEJEON)
    assertThat(bobTicketRepository.count()).isEqualTo(1)
  }

  @Test
  fun `이번 타임에 아무거나 서울 티켓이 있는데 서울에서 했다`() {
    // given
    ticketing(user1, 1)

    // when & then
    assertThrows<IllegalStateException> {
      bobTicketService.responseMeSpecificPlace(user1.id, BobStyleType.ANYTHING, CityType.SEOUL, fourOClock())
    }
    try {
      bobTicketService.responseMeSpecificPlace(user1.id, BobStyleType.ANYTHING, CityType.SEOUL, fourOClock())
    } catch (e: IllegalStateException) {
      assertThat(e.cause!!.javaClass).isEqualTo(BobTicketAlreadyExistsException::class.java)
    }
  }

  @Test
  fun `이번 타임에 1명 이상 투표한 상황에서 투표했을 때 1등이 아니게 된다`() {
    // given
    ticketing(user1, 1)
    ticketing(user2, 2)

    // when
    val ticket = bobTicketService.responseMe(user3.id, BobStyleType.ANYTHING, fourOClock())

    // then
    assertThat(ticket.issuedOrder).isEqualTo(IssuedOrderType.NOT_FIRST)
    assertThat(ticket.issuedOrderNumber).isEqualTo(3)
  }
}