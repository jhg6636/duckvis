package com.duckvis.bob.services

import com.duckvis.core.domain.bob.*
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.bob.BobTeamAlreadyMatchedException
import com.duckvis.core.exceptions.bob.NeverMatchThemException
import com.duckvis.core.exceptions.bob.NotEnoughBobTicketException
import com.duckvis.core.types.bob.BobStyleType
import com.duckvis.core.types.bob.BobTimeType
import com.duckvis.core.types.bob.IssuedOrderType
import com.duckvis.core.types.bob.PayType
import com.duckvis.core.types.shared.CityType
import com.duckvis.core.utils.DateTimeMaker
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test")
class BobTeamServiceTest(
  @Autowired private val bobTeamService: BobTeamService,
  @Autowired private val bobTicketService: BobTicketService,
  @Autowired private val ticketRepository: BobTicketRepository,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val bobHistoryRepository: BobHistoryRepository,
  @Autowired private val overMoneyRepository: OverMoneyRepository,
) {

  @AfterEach
  fun clear() {
    ticketRepository.deleteAllInBatch()
    userRepository.deleteAllInBatch()
    bobHistoryRepository.deleteAllInBatch()
  }

  @Test
  fun `밥팀 기록`() {
    // given
    val user1 = createUser("user1", 1)
    val user2 = createUser("user2", 2)
    val user3 = createUser("user3", 3)
    val user4 = createUser("user4", 4)
    val now = LocalDateTime.now().withHour(BobTimeType.LUNCH.startHour)

    bobTicketService.responseMe(user1.id, BobStyleType.ANYTHING, now)
    bobTicketService.responseMe(user2.id, BobStyleType.ANYTHING, now)
    bobTicketService.responseMe(user3.id, BobStyleType.ANYTHING, now)
    bobTicketService.responseMe(user4.id, BobStyleType.ANYTHING, now)

    bobTeamService.determineBobTeam(CityType.SEOUL, BobTimeType.LUNCH, FifoTeamDecisionLogic(), FifoTeamSortStrategy)

    // when
    val bobHistories = bobHistoryRepository.findAll()

    // then
    assertThat(bobHistories).hasSize(4)
  }

  @Test
  fun `밥팀 없을 때 밥팀 보여주기`() {
    // given

    // when & then
    assertThrows<NotEnoughBobTicketException> {
      bobTeamService.determineBobTeam(CityType.SEOUL, BobTimeType.LUNCH)
    }
  }

  @Test
  fun `4명 밥 신청했을 때 밥팀 보여주기`() {
    // given
    val user1 = createUser("user1", 1)
    val user2 = createUser("user2", 2)
    val user3 = createUser("user3", 3)
    val user4 = createUser("user4", 4)

    // when
    val ticket1 = createTodayLunchTicket(user1, 1, issuedOrderType = IssuedOrderType.FIRST)
    val ticket2 = createTodayLunchTicket(user2, 2)
    val ticket3 = createTodayLunchTicket(user3, 3)
    val ticket4 = createTodayLunchTicket(user4, 4)
    ticketRepository.saveAll(listOf(ticket1, ticket2, ticket3, ticket4))

    val bobTeam = bobTeamService.determineBobTeam(CityType.SEOUL, BobTimeType.LUNCH)

    // then
    assertThat(bobTeam.teams[0].anythingTeams).hasSize(2)
  }

  @Test
  fun `1명이 티켓 2개 가지고 있을 때 밥팀 짜기`() {
    // given
    val user1 = createUser("user1", 1)

    val ticket1 = createTodayLunchTicket(user1, 1, issuedOrderType = IssuedOrderType.FIRST)

    // when & then
    org.junit.jupiter.api.assertThrows<DataIntegrityViolationException> {
      createTodayLunchTicket(user1, 1)
    }
  }

  @Test
  fun `2명이 밥 신청할 때`() {
    // given
    val user1 = createUser("user1", 1)
    val user2 = createUser("user2", 2)

    val ticket1 = createTodayLunchTicket(user1, 1, issuedOrderType = IssuedOrderType.FIRST)
    val ticket2 = createTodayLunchTicket(user2, 2)
    ticketRepository.saveAll(listOf(ticket1, ticket2))

    assertThrows<NotEnoughBobTicketException> {
      bobTeamService.determineBobTeam(CityType.SEOUL, BobTimeType.LUNCH)
    }
  }

  @Test
  @Transactional
  fun `밥팀 짜진 후 짜졌는지 확인한다`() {
    // given
    val user1 = createUser("user1", 1)
    val user2 = createUser("user2", 2)
    val user3 = createUser("user3", 3)
    val user4 = createUser("user4", 4)

    val ticket1 = createTodayLunchTicket(user1, 1, issuedOrderType = IssuedOrderType.FIRST)
    val ticket2 = createTodayLunchTicket(user2, 2)
    val ticket3 = createTodayLunchTicket(user3, 3)
    val ticket4 = createTodayLunchTicket(user4, 4)
    ticketRepository.saveAll(listOf(ticket1, ticket2, ticket3, ticket4))

    bobTeamService.determineBobTeam(CityType.SEOUL, BobTimeType.LUNCH)

    // when & then
    assertThrows<BobTeamAlreadyMatchedException> {
      bobTeamService.determineBobTeam(CityType.SEOUL, BobTimeType.LUNCH)
    }
  }

  @Test
  fun `개인팀 3명, 법카팀 3명이 밥 신청한다`() {
    // given
    val user1 = createUser("user1", 1)
    val user2 = createUser("user2", 2)
    val user3 = createUser("user3", 3)
    val over1 = createUser("over1", 4)
    val over2 = createUser("over2", 5)
    val over3 = createUser("over3", 6)
    addLunchMoney(over1, 10000)
    addLunchMoney(over2, 10000)
    addLunchMoney(over3, 10000)

    val ticket1 = createTodayLunchTicket(user1, 1, issuedOrderType = IssuedOrderType.FIRST)
    val ticket2 = createTodayLunchTicket(user2, 2)
    val ticket3 = createTodayLunchTicket(user3, 3)
    val ticket4 = createTodayLunchTicket(over1, 4, PayType.MEMBER_SELF)
    val ticket5 = createTodayLunchTicket(over2, 5, PayType.MEMBER_SELF)
    val ticket6 = createTodayLunchTicket(over3, 6, PayType.MEMBER_SELF)

    // when
    val teams =
      bobTeamService.determineBobTeam(CityType.SEOUL, BobTimeType.LUNCH, FifoTeamDecisionLogic(), FifoTeamSortStrategy)

    // then
    assertThat(teams.teams[0].anythingTeams).hasSize(1)
    assertThat(teams.teams[0].anythingTeams[0].leaderTicket.id).isEqualTo(ticket2.id)
    assertThat(teams.teams[0].anythingTeams[0].memberTickets).hasSize(3)
    assertThat(teams.teams[1].anythingTeams).hasSize(1)
    assertThat(teams.teams[1].anythingTeams[0].leaderTicket.id).isEqualTo(ticket4.id)
    assertThat(teams.teams[1].anythingTeams[0].memberTickets).hasSize(3)
    assertThat(allMoney(over1.id)).isEqualTo(10000)
    assertThat(allMoney(over2.id)).isEqualTo(10000)
    assertThat(allMoney(over3.id)).isEqualTo(10000)
  }

  @Test
  fun `개인팀 3명, 법카팀 2명이 밥 신청한다`() {
    // given
    val user1 = createUser("user1", 1)
    val user2 = createUser("user2", 2)
    val over1 = createUser("over1", 4)
    val over2 = createUser("over2", 5)
    val over3 = createUser("over3", 6)
    addLunchMoney(over1, 10000)
    addLunchMoney(over2, 12000)
    addLunchMoney(over3, 10000)

    val ticket1 = createTodayLunchTicket(user1, 1, issuedOrderType = IssuedOrderType.FIRST)
    val ticket2 = createTodayLunchTicket(user2, 2)
    val ticket4 = createTodayLunchTicket(over1, 4, PayType.MEMBER_SELF)
    val ticket5 = createTodayLunchTicket(over2, 5, PayType.MEMBER_SELF)
    val ticket6 = createTodayLunchTicket(over3, 6, PayType.MEMBER_SELF)

    // when
    val teams =
      bobTeamService.determineBobTeam(CityType.SEOUL, BobTimeType.LUNCH, FifoTeamDecisionLogic(), FifoTeamSortStrategy)

    // then
    assertThat(teams.teams[0].anythingTeams).hasSize(2)
    assertThat(teams.teams[0].anythingTeams[0].leaderTicket.id).isEqualTo(ticket2.id)
    assertThat(teams.teams[0].anythingTeams[0].memberTickets).hasSize(3)
    assertThat(teams.teams[0].anythingTeams[1].leaderTicket.id).isEqualTo(ticket5.id)
    assertThat(teams.teams[0].anythingTeams[1].memberTickets).hasSize(2)
    val savedTicket = ticketRepository.findByIdOrNull(ticket1.id)
    assertThat(savedTicket?.isSelfPay).isFalse
    assertThat(allMoney(over1.id)).isEqualTo(10000)
    assertThat(allMoney(over2.id)).isEqualTo(12000)
    assertThat(allMoney(over3.id)).isEqualTo(10000)
  }

  @Test
  fun `개인팀 1명, 법카팀 2명이 밥 신청한다`() {
    // given
    val user1 = createUser("user1", 1)
    val user2 = createUser("user2", 2)
    val over1 = createUser("over1", 4)
    addLunchMoney(over1, 10000)

    val ticket1 = createTodayLunchTicket(user1, 1, issuedOrderType = IssuedOrderType.FIRST)
    val ticket2 = createTodayLunchTicket(user2, 2)
    val ticket4 = createTodayLunchTicket(over1, 4, PayType.MEMBER_SELF)

    // when
    val teams =
      bobTeamService.determineBobTeam(CityType.SEOUL, BobTimeType.LUNCH, FifoTeamDecisionLogic(), FifoTeamSortStrategy)

    // then
    assertThat(teams.teams[0].anythingTeams).hasSize(1)
    assertThat(teams.teams[0].anythingTeams[0].leaderTicket.id).isEqualTo(ticket2.id)
    assertThat(teams.teams[0].anythingTeams[0].memberTickets).hasSize(3)
    val savedTicket = ticketRepository.findByIdOrNull(ticket1.id)
    assertThat(savedTicket?.isSelfPay).isFalse
    assertThat(allMoney(over1.id)).isEqualTo(10000)
    assertThat(allMoney(user1.id)).isEqualTo(0)
  }

  @Test
  fun `팀 짜지면 안 될 때`() {
    // given
    val user1 = createUser("바롬", 1)
    val user2 = createUser("남길", 2)
    val user3 = createUser("솔아", 3)
//    val user4 = createUser("윤택", 4)
    val ticket1 = createTodayLunchTicket(user1, 1)
    val ticket2 = createTodayLunchTicket(user2, 2)
    val ticket3 = createTodayLunchTicket(user3, 3)
//    val ticket4 = createTodayLunchTicket(user4, 4)

    // when & then
    assertThrows<NeverMatchThemException> {
      println(bobTeamService.determineBobTeam(CityType.SEOUL, BobTimeType.LUNCH).teams[0].asString)
    }
  }

  @Test
  fun `sortTickets가 잘 작동한다`() {
    // given
    val user1 = createUser("user1", 1)
    val user2 = createUser("user2", 2)
    val user3 = createUser("user3", 3)
    val user4 = createUser("user4", 4)
    val user5 = createUser("user5", 5)
    val user6 = createUser("user6", 6)
    addBobHistory(user5, 1L, 1)
    addBobHistory(user6, 2L, 1)
    addBobHistory(user5, 6L, 3)
    addBobHistory(user6, 7L, 3)
    addBobHistory(user5, 8L, 5)
    addBobHistory(user2, 9L, 5)

    val ticket1 = createTodayLunchTicket(user1, 1)
    val ticket2 = createTodayLunchTicket(user2, 2)
    val ticket3 = createTodayLunchTicket(user3, 3)
    val ticket4 = createTodayLunchTicket(user4, 4)
    val ticket5 = createTodayLunchTicket(user5, 5)
    val ticket6 = createTodayLunchTicket(user6, 6)

    // when
    val sorted = bobTeamService.sortTickets(listOf(ticket1, ticket2, ticket3, ticket4, ticket5, ticket6))

    // then
    assertThat(sorted.map { it.issuedOrderNumber }).containsExactly(5, 6, 2, 1, 3, 4)
  }

  @Transactional
  fun createUser(userCodeAndName: String, id: Long): User {
    return userRepository.save(User(userCodeAndName, userCodeAndName))
  }

  @Transactional
  fun createTodayLunchTicket(
    user: User,
    orderNumber: Int,
    payType: PayType = PayType.SELECTSTAR,
    styleType: BobStyleType = BobStyleType.ANYTHING,
    issuedOrderType: IssuedOrderType = IssuedOrderType.NOT_FIRST,
  ): BobTicket {
    return ticketRepository.save(
      BobTicket(
        user.id,
        user.name,
        user.tagString,
        DateTimeMaker.nowDate(),
        DateTimeMaker.nowDateTime().toLocalTime().withHour(1),
        BobTimeType.LUNCH,
        styleType,
        payType,
        issuedOrder = issuedOrderType,
        city = user.city,
        issuedOrderNumber = orderNumber
      )
    )
  }

  @Transactional
  fun addLunchMoney(user: User, money: Int): OverMoney {
    return overMoneyRepository.save(OverMoney(user.id, money, DateTimeMaker.nowDateTime(), BobTimeType.LUNCH))
  }

  @Transactional
  fun allMoney(userId: Long): Int {
    return overMoneyRepository.findAllByUserId(userId).sumBy { overMoney -> overMoney.money }
  }

  @Transactional
  fun addBobHistory(user: User, bobTicketId: Long, bobTeamNumber: Int): BobHistory {
    return bobHistoryRepository.save(
      BobHistory(
        bobTicketId,
        user.id,
        false,
        bobTeamNumber,
        PayType.SELECTSTAR
      )
    )
  }

}