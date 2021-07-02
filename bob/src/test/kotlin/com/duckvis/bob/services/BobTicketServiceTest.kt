package com.duckvis.bob.services

import com.duckvis.core.domain.bob.BobHistoryRepository
import com.duckvis.core.domain.bob.BobTicket
import com.duckvis.core.domain.bob.BobTicketRepository
import com.duckvis.core.domain.bob.MenuRepository
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.bob.NeverEatThisMonthException
import com.duckvis.core.exceptions.bob.NotTicketTimeException
import com.duckvis.core.types.bob.BobStyleType
import com.duckvis.core.types.bob.BobTimeType
import com.duckvis.core.types.bob.StatisticsOption
import com.duckvis.core.types.shared.CityType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

@SpringBootTest
@ActiveProfiles("test")
class BobTicketServiceTest(
  @Autowired private val bobTicketService: BobTicketService,
  @Autowired private val bobTeamService: BobTeamService,
  @Autowired private val bobMenuService: BobMenuService,
  @Autowired private val bobGeneralService: BobGeneralService,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val ticketRepository: BobTicketRepository,
  @Autowired private val menuRepository: MenuRepository,
  @Autowired private val bobHistoryRepository: BobHistoryRepository,
) {

  // TODO beforeEach 활용 장점 : 중복된 given 절을 제거할 수 있다. 단점 -> 고민해보시면 좋을 것 같습니다

  // TODO deleteAllInBatch
  @AfterEach
  fun afterEach() {
    bobHistoryRepository.deleteAll()
    ticketRepository.deleteAll()
    userRepository.deleteAll()
    menuRepository.deleteAll()
  }

  @Test
  fun `점심 직전 시간에 갑자기 밥투표를 신청했다`() {
    // given
    val user = createUser("user1")
    val now = LocalDateTime.now().withHour(BobTimeType.LUNCH.startHour - 1)

    // when & then
    assertThrows<IllegalStateException> {
      bobTicketService.responseMe(user.id, BobStyleType.ANYTHING, now)
    }
    try {
      bobTicketService.responseMe(user.id, BobStyleType.ANYTHING, now)
    } catch (e: IllegalStateException) {
      assertThat(e.cause!!.javaClass).isEqualTo(NotTicketTimeException::class.java)
    }
  }

  // TODO 테스트 코드의 Transactional ?
  //  여기에 트랜잭션이 있으면 무슨 일이 벌어지는가, 의도하지 않은 기능이 잘 작동해버릴 수 있다 -> 테스트 코드 지양해야 한다
  //  지양함으로써 생기는 문제가 있다 >> 고민해보시면 좋을 것 같습니다.
  @Test
  @Transactional
  fun `점심 시간에 1등으로 밥투표를 신청했다`() {
    // given
    val user1 = createUser("user1")
    val user2 = createUser("user2")
    val user3 = createUser("user3")
    val now = LocalDateTime.now().withHour(BobTimeType.LUNCH.startHour)
//    ticketRepository.save(Ticket(1L, date, time))
//    ticketRepository.save(Ticket)
//    ticketRepository.save(Ticket)

    // when
    val ticket1 = bobTicketService.responseMe(user1.id, BobStyleType.ANYTHING, now)
    val ticket2 = bobTicketService.responseMe(user2.id, BobStyleType.ANYTHING, now)
    val ticket3 = bobTicketService.responseMe(user3.id, BobStyleType.ANYTHING, now)

    bobTeamService.determineBobTeam(CityType.SEOUL, BobTimeType.LUNCH)

    // then // TODO 중복
    assertThat(ticket1.isFirst).isTrue
    assertThat(ticket2.isFirst).isFalse
    assertThat(ticket3.isFirst).isFalse
    assertThat(ticket1.issuedOrderNumber).isEqualTo(1)
    assertThat(ticket2.issuedOrderNumber).isEqualTo(2)
    assertThat(ticket3.issuedOrderNumber).isEqualTo(3)
    assertThat(ticket3.issuedOrderNumber).isEqualTo(3)
    assertThat(ticket1.bobTimeType).isEqualTo(BobTimeType.LUNCH)
  }

//  private fun assertTicket(ticket: BobTicket, isFirst: Boolean, issuedOrdNumber: nt) {
//
//  }

  @Test
  fun `메뉴 추천을 받는다`() {
    // given
    bobMenuService.responseAddMenu("덮밥")
    bobMenuService.responseAddMenu("국밥")

    // when
    val recommendedMenu = bobMenuService.responseRecommendMenu(listOf())

    // then
    assertThat(recommendedMenu.name).isIn(listOf("덮밥", "국밥"))
  }

  @Test
  fun `제외한 메뉴가 메뉴 추천에 표시되지 않는다`() {
    // given
    bobMenuService.responseAddMenu("덮밥")
    bobMenuService.responseAddMenu("국밥")

    // when
    val recommendedMenu = bobMenuService.responseRecommendMenu(listOf("덮밥"))

    // then
    assertThat(recommendedMenu.name).isEqualTo("국밥")
  }

  @Test
  fun `밥을 한 번도 안 먹은 상황에서 밥 통계를 확인한다`() {
    // given
    val user = createUser("user1")

    // when & then
    assertThrows<NeverEatThisMonthException> {
      bobGeneralService.responseStatistics(user.id, StatisticsOption.ALL)
    }
  }

  @Test
  fun `밥부장 했을 때 밥 통계 확인`() {
    // given
    val user1 = createUser("user1")
    val user2 = createUser("user2")
    val user3 = createUser("user3")
    val user4 = createUser("user4")
    val now = LocalDateTime.now().withHour(BobTimeType.LUNCH.startHour)

    bobTicketService.responseMe(user1.id, BobStyleType.ANYTHING, now)
    bobTicketService.responseMe(user2.id, BobStyleType.ANYTHING, now)
    bobTicketService.responseMe(user3.id, BobStyleType.ANYTHING, now)
    bobTicketService.responseMe(user4.id, BobStyleType.ANYTHING, now)

    val bobTeams =
      bobTeamService.determineBobTeam(CityType.SEOUL, BobTimeType.LUNCH, FifoTeamDecisionLogic(), FifoTeamSortStrategy)

    // when
    val statisticsDto = bobGeneralService.responseStatistics(user3.id, StatisticsOption.ALL)

    // then
    assertThat(statisticsDto.leaderCount).isEqualTo(1)
    assertThat(statisticsDto.freqeuntTeammateName).isEqualTo("user4")
    assertThat(statisticsDto.frequency).isEqualTo(1)
  }

  @Test
  fun `동시에 2명이 밥 신청한다`() {
    // given
    val user1 = createUser("user1")
    val user2 = createUser("user2")
    val now = LocalDateTime.now().withHour(BobTimeType.LUNCH.startHour)

    val future1 = ticketingAsync(user1, now)
    val future2 = ticketingAsync(user2, now)

    // when
    CompletableFuture.allOf(future1, future2)
      .thenApply {
        listOf(future1, future2).map { future ->
          future.join()
        }
      }
      .join()

    // then

    val results = ticketRepository.findAll()

    assertThat(results).hasSize(2)
    assertThat(results).extracting("issuedOrderNumber").containsExactlyInAnyOrder(1, 2)
    assertThat(results).extracting("userId").containsExactlyInAnyOrder(user1.id, user2.id)
  }

  // TODO 테스트 코드 given 절 내에서 중복을 제거하는 방법
  @Transactional
  fun createUser(userCodeAndName: String): User {
    return userRepository.save(User(userCodeAndName, userCodeAndName))
  }

  private fun ticketingAsync(user: User, now: LocalDateTime): CompletableFuture<BobTicket> {
    val executor = Executors.newFixedThreadPool(10)
    return CompletableFuture.supplyAsync({
      println("before: " + LocalDateTime.now())
      val ticket = bobTicketService.responseMe(user.id, BobStyleType.ANYTHING, now)
      Thread.sleep(1000)
      println("after: " + LocalDateTime.now())
      ticket
    }, executor)
  }
}