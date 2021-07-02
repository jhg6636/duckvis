package com.duckvis.bob.domain

import com.duckvis.bob.services.BobTeamMaker
import com.duckvis.bob.services.CovidTeamDecisionLogic
import com.duckvis.bob.services.FifoTeamDecisionLogic
import com.duckvis.bob.services.FifoTeamSortStrategy
import com.duckvis.core.domain.bob.BobTicket
import com.duckvis.core.domain.shared.User
import com.duckvis.core.exceptions.bob.NotEnoughBobTicketException
import com.duckvis.core.types.bob.BobStyleType
import com.duckvis.core.types.bob.BobTimeType
import com.duckvis.core.types.bob.IssuedOrderType
import com.duckvis.core.types.bob.PayType
import com.duckvis.core.types.shared.CityType
import com.duckvis.core.types.shared.UserPathType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

/**
 * POJO Test
 * A B C Table을 적절히 긁어와 아주 그냥 지지고 볶고 잘 조합해서 반환해야
 * -> 쿼리로 애매해... select * from where group order -> N천만건 튜닝이 제한적
 * >> 지지고 볶고 잘 조합해서 반환해야 >> POJO로 만들고 테스트 빡빡하게
 */
//@ActiveProfiles("test")
class BobTeamMakerTest {

  private val today = LocalDateTime.now(ZoneOffset.UTC)

  private val covidStrategy = CovidTeamDecisionLogic()

  @Test
  fun `3명 같은 지역, 같은 식성으로 투표했다`() {
    // given
    val user1 = createUser("USER1", 1)
    val user2 = createUser("USER2", 2)
    val user3 = createUser("USER3", 3)

    val ticket1 = createTodayLunchTicket(user1, 1, issuedOrderType = IssuedOrderType.FIRST)
    val ticket2 = createTodayLunchTicket(user2, 2, issuedOrderType = IssuedOrderType.NOT_FIRST)
    val ticket3 = createTodayLunchTicket(user3, 3, issuedOrderType = IssuedOrderType.NOT_FIRST)
    val bobTeamMaker = BobTeamMaker(listOf(ticket1, ticket2, ticket3), FifoTeamSortStrategy)

    // when
    val teams = bobTeamMaker.make(covidStrategy, PayType.ALL)

    // then
    assertThat(teams.anythingTeams.size).isEqualTo(1)
    assertThat(teams.anythingTeams[0].memberTickets).hasSize(3)
    assertThat(teams.anythingTeams[0].leaderTicket).isNotEqualTo(ticket1)
  }

  @Test
  fun `4명 같은 지역, 같은 식성으로 투표했다`() {
    val user1 = createUser("USER1", 1)
    val user2 = createUser("USER2", 2)
    val user3 = createUser("USER3", 3)
    val user4 = createUser("USER4", 4)

    val ticket1 = createTodayLunchTicket(user1, 1, issuedOrderType = IssuedOrderType.FIRST)
    val ticket2 = createTodayLunchTicket(user2, 2, issuedOrderType = IssuedOrderType.NOT_FIRST)
    val ticket3 = createTodayLunchTicket(user3, 3, issuedOrderType = IssuedOrderType.NOT_FIRST)
    val ticket4 = createTodayLunchTicket(user4, 4, issuedOrderType = IssuedOrderType.NOT_FIRST)
    val bobTeamMaker = BobTeamMaker(listOf(ticket1, ticket2, ticket3, ticket4), FifoTeamSortStrategy)

    // when
    val teams = bobTeamMaker.make(covidStrategy, PayType.ALL)

    // then
    assertThat(teams.anythingTeams.size).isEqualTo(2)
    assertThat(teams.anythingTeams[0].memberTickets).hasSize(2)
    assertThat(teams.anythingTeams[0].leaderTicket).isNotEqualTo(ticket1)
    assertThat(teams.anythingTeams[1].leaderTicket).isNotEqualTo(ticket1)
  }

  @Test
  fun `5명 모두 같은 지역, 같은 식성으로 투표했다`() {
    val user1 = createUser("USER1", 1)
    val user2 = createUser("USER2", 2)
    val user3 = createUser("USER3", 3)
    val user4 = createUser("USER4", 4)
    val user5 = createUser("USER5", 5)

    val ticket1 = createTodayLunchTicket(user1, 1, issuedOrderType = IssuedOrderType.FIRST)
    val ticket2 = createTodayLunchTicket(user2, 2, issuedOrderType = IssuedOrderType.NOT_FIRST)
    val ticket3 = createTodayLunchTicket(user3, 3, issuedOrderType = IssuedOrderType.NOT_FIRST)
    val ticket4 = createTodayLunchTicket(user4, 4, issuedOrderType = IssuedOrderType.NOT_FIRST)
    val ticket5 = createTodayLunchTicket(user5, 5, issuedOrderType = IssuedOrderType.NOT_FIRST)
    val bobTeamMaker = BobTeamMaker(listOf(ticket1, ticket2, ticket3, ticket4, ticket5), FifoTeamSortStrategy)

    // when
    val teams = bobTeamMaker.make(covidStrategy, PayType.ALL)

    // then
    assertThat(teams.anythingTeams.size).isEqualTo(2)
    assertThat(teams.anythingTeams[0].memberTickets.size + teams.anythingTeams[1].memberTickets.size).isEqualTo(5)
    assertThat(teams.anythingTeams[0].leaderTicket).isNotEqualTo(ticket1)
    assertThat(teams.anythingTeams[1].leaderTicket).isNotEqualTo(ticket1)
  }

  @Test
  fun `6명 모두 같은 지역, 같은 식성으로 투표했다`() {
    val user1 = createUser("USER1", 1)
    val user2 = createUser("USER2", 2)
    val user3 = createUser("USER3", 3)
    val user4 = createUser("USER4", 4)
    val user5 = createUser("USER5", 5)
    val user6 = createUser("USER6", 6)

    val ticket1 = createTodayLunchTicket(user1, 1, issuedOrderType = IssuedOrderType.FIRST)
    val ticket2 = createTodayLunchTicket(user2, 2, issuedOrderType = IssuedOrderType.NOT_FIRST)
    val ticket3 = createTodayLunchTicket(user3, 3, issuedOrderType = IssuedOrderType.NOT_FIRST)
    val ticket4 = createTodayLunchTicket(user4, 4, issuedOrderType = IssuedOrderType.NOT_FIRST)
    val ticket5 = createTodayLunchTicket(user5, 5, issuedOrderType = IssuedOrderType.NOT_FIRST)
    val ticket6 = createTodayLunchTicket(user6, 6, issuedOrderType = IssuedOrderType.NOT_FIRST)
    val bobTeamMaker = BobTeamMaker(listOf(ticket1, ticket2, ticket3, ticket4, ticket5, ticket6), FifoTeamSortStrategy)

    // when
    val teams = bobTeamMaker.make(covidStrategy, PayType.ALL)

    // then
    assertThat(teams.anythingTeams.size).isEqualTo(2)
    assertThat(teams.anythingTeams[0].memberTickets).hasSize(3)
    assertThat(teams.anythingTeams[1].memberTickets).hasSize(3)
    assertThat(teams.anythingTeams[0].leaderTicket).isEqualTo(ticket2)
    assertThat(teams.anythingTeams[1].leaderTicket).isEqualTo(ticket4)

  }

  @Test
  fun `2명 미만 해당한다`() {
    // given
    val user1 = createUser("USER1", 1)

    val ticket1 = createTodayLunchTicket(user1, 1, issuedOrderType = IssuedOrderType.FIRST)

    val bobTeamMaker = BobTeamMaker(listOf(ticket1), FifoTeamSortStrategy)

    // when & then
    assertThrows<NotEnoughBobTicketException> {
      bobTeamMaker.make(covidStrategy, PayType.ALL)
    }
  }

  @Test
  fun `채식 3명 미만, 아무거나 3명 미만, 합치면 3명 이상 투표`() {
    // given
    val user1 = createUser("USER1", 1)
    val user2 = createUser("USER2", 2)
    val user3 = createUser("USER3", 3)

    val ticket1 = createTodayLunchTicket(user1, 1, issuedOrderType = IssuedOrderType.FIRST)
    val ticket2 = createTodayLunchTicket(user2, 2, BobStyleType.VEGETARIAN)
    val ticket3 = createTodayLunchTicket(user3, 3)
    val bobTeamMaker = BobTeamMaker(listOf(ticket1, ticket2, ticket3), FifoTeamSortStrategy)

    // when
    val teams = bobTeamMaker.make(covidStrategy, PayType.ALL)

    // then
    assertThat(teams.vegetarianTeams).hasSize(0)
    assertThat(teams.anythingTeams).hasSize(1)
    assertThat(teams.anythingTeams[0].memberTickets).hasSize(3)
    assertThat(teams.anythingTeams[0].leaderTicket).isNotEqualTo(ticket1)
  }


  @Test
  fun `채식 3명 미만, 아무거나 3명 이상 투표`() {
    // given
    val user1 = createUser("USER1", 1)
    val user2 = createUser("USER2", 2)
    val user3 = createUser("USER3", 3)
    val user4 = createUser("USER4", 4)

    val ticket1 = createTodayLunchTicket(user1, 1, issuedOrderType = IssuedOrderType.FIRST)
    val ticket2 = createTodayLunchTicket(user2, 2, BobStyleType.VEGETARIAN)
    val ticket3 = createTodayLunchTicket(user3, 3)
    val ticket4 = createTodayLunchTicket(user4, 4)

    val bobTeamMaker = BobTeamMaker(listOf(ticket1, ticket2, ticket3, ticket4), FifoTeamSortStrategy)

    // when
    val teams = bobTeamMaker.make(covidStrategy, PayType.ALL)

    // then
    assertThat(teams.vegetarianTeams).hasSize(0)
    assertThat(teams.anythingTeams).hasSize(2)
    assertThat(teams.anythingTeams[0].memberTickets).hasSize(2)
    assertThat(teams.anythingTeams[1].memberTickets).hasSize(2)
    assertThat(teams.anythingTeams[0].leaderTicket).isNotEqualTo(ticket1)
    assertThat(teams.anythingTeams[1].leaderTicket).isNotEqualTo(ticket1)
  }

  @Test
  fun `채식 3명 이상, 아무거나 3명 미만 투표`() {
    // given
    val user1 = createUser("USER1", 1)
    val user2 = createUser("USER2", 2)
    val user3 = createUser("USER3", 3)
    val user4 = createUser("USER4", 4)

    val ticket1 = createTodayLunchTicket(user1, 1, issuedOrderType = IssuedOrderType.FIRST)
    val ticket2 = createTodayLunchTicket(user2, 2, BobStyleType.VEGETARIAN)
    val ticket3 = createTodayLunchTicket(user3, 3, BobStyleType.VEGETARIAN)
    val ticket4 = createTodayLunchTicket(user4, 4, BobStyleType.VEGETARIAN)

    val bobTeamMaker = BobTeamMaker(listOf(ticket4, ticket1, ticket2, ticket3), FifoTeamSortStrategy)

    // when
    val teams = bobTeamMaker.make(covidStrategy, PayType.ALL)

    // then
    assertThat(teams.vegetarianTeams).hasSize(0)
    assertThat(teams.anythingTeams).hasSize(2)
    assertThat(teams.anythingTeams[0].memberTickets).hasSize(2)
    assertThat(teams.anythingTeams[1].memberTickets).hasSize(2)
    assertThat(teams.anythingTeams[0].leaderTicket).isNotEqualTo(ticket1)
    assertThat(teams.anythingTeams[1].leaderTicket).isNotEqualTo(ticket1)
  }

  @Test
  fun `1등이 되면 밥부장을 면제 받는다`() {
    // given
    val user1 = createUser("USER1", 1)
    val user2 = createUser("USER2", 2)
    val user3 = createUser("USER3", 3)

    val ticket1 = createTodayLunchTicket(user1, 1, issuedOrderType = IssuedOrderType.FIRST)
    val ticket2 = createTodayLunchTicket(user2, 2)
    val ticket3 = createTodayLunchTicket(user3, 3)
    val bobTeamMaker = BobTeamMaker(listOf(ticket1, ticket2, ticket3), FifoTeamSortStrategy)

    // when
    val teams = bobTeamMaker.make(FifoTeamDecisionLogic(), PayType.ALL)

    // then
    assertThat(teams.anythingTeams.size).isEqualTo(1)
    assertThat(teams.anythingTeams[0].leaderTicket).isEqualTo(ticket2)
  }

  @Test
  fun `채식 1등, 아무거나 1등이 같은 3인 팀에 들어간다`() {
    // given
    val user1 = createUser("USER1", 1)
    val user2 = createUser("USER2", 2)
    val user3 = createUser("USER3", 3)
    val user4 = createUser("USER4", 4)
    val user5 = createUser("USER5", 5)

    val ticket1 = createTodayLunchTicket(user1, 1, issuedOrderType = IssuedOrderType.FIRST)
    val ticket2 = createTodayLunchTicket(user2, 2, BobStyleType.VEGETARIAN, IssuedOrderType.FIRST)
    val ticket3 = createTodayLunchTicket(user3, 3)
    val ticket4 = createTodayLunchTicket(user4, 4)
    val ticket5 = createTodayLunchTicket(user5, 5)
    val bobTeamMaker = BobTeamMaker(listOf(ticket1, ticket2, ticket3, ticket4, ticket5), FifoTeamSortStrategy)

    // when
    val teams = bobTeamMaker.make(FifoTeamDecisionLogic(), PayType.ALL)

    // then
    assertThat(teams.anythingTeams.size).isEqualTo(2)
    assertThat(teams.anythingTeams[0].leaderTicket).isEqualTo(ticket3)
  }

  @Test
  fun `채식 1등, 아무거나 1등이 같은 2인 팀에 들어간다`() {
    // given
    val user1 = createUser("USER1", 1)
    val user2 = createUser("USER2", 2)
    val user3 = createUser("USER3", 3)
    val user4 = createUser("USER4", 4)

    val ticket1 =
      createTodayLunchTicket(
        user1,
        1,
        issuedOrderType = IssuedOrderType.FIRST,
        time = today.minusMinutes(3).toLocalTime()
      )
    val ticket2 = createTodayLunchTicket(user2, 2, BobStyleType.VEGETARIAN, IssuedOrderType.FIRST)
    val ticket3 = createTodayLunchTicket(user3, 3)
    val ticket4 = createTodayLunchTicket(user4, 4)
    val bobTeamMaker = BobTeamMaker(listOf(ticket3, ticket4, ticket1, ticket2), FifoTeamSortStrategy)

    // when
    val teams = bobTeamMaker.make(FifoTeamDecisionLogic(), PayType.ALL)

    // then
    assertThat(teams.anythingTeams.size).isEqualTo(2)
    assertThat(teams.anythingTeams[1].leaderTicket).isEqualTo(ticket2)
  }

  @Test
  fun `채식만 3명 모였을 때도 채식팀끼리 배정이 되어야 된다`() {
    // given
    val user1 = createUser("USER1", 1)
    val user2 = createUser("USER2", 2)
    val user3 = createUser("USER3", 3)

    val ticket1 =
      createTodayLunchTicket(user1, 1, issuedOrderType = IssuedOrderType.FIRST, styleType = BobStyleType.VEGETARIAN)
    val ticket2 = createTodayLunchTicket(user2, 2, styleType = BobStyleType.VEGETARIAN)
    val ticket3 = createTodayLunchTicket(user3, 3, styleType = BobStyleType.VEGETARIAN)
    val bobTeamMaker = BobTeamMaker(listOf(ticket1, ticket2, ticket3), FifoTeamSortStrategy)

    // when
    val teams = bobTeamMaker.make(FifoTeamDecisionLogic(), PayType.ALL)

    println(teams)
    // then
    assertThat(teams.vegetarianTeams.size).isEqualTo(1)
    assertThat(teams.vegetarianTeams[0].leaderTicket).isEqualTo(ticket2)
  }

  @Test
  fun `채식, 아무거나, 늦먹 한명씩 투표한다`() {
    val user1 = createUser("USER1", 1)
    val user2 = createUser("USER2", 2)
    val user3 = createUser("USER3", 3)

    val ticket1 =
      createTodayLunchTicket(user1, 1, issuedOrderType = IssuedOrderType.FIRST, styleType = BobStyleType.VEGETARIAN)
    val ticket2 =
      createTodayLunchTicket(user2, 2, issuedOrderType = IssuedOrderType.FIRST, styleType = BobStyleType.ANYTHING)
    val ticket3 = createTodayLunchTicket(user3, 3, styleType = BobStyleType.LATE_EAT)
    val bobTeamMaker = BobTeamMaker(listOf(ticket1, ticket2, ticket3), FifoTeamSortStrategy)

    // when
    val teams = bobTeamMaker.make(FifoTeamDecisionLogic(), PayType.ALL)

    println(teams)
    // then
    assertThat(teams.anythingTeams.size).isEqualTo(1)
    assertThat(teams.anythingTeams[0].leaderTicket).isEqualTo(ticket3)
    assertThat(teams.vegetarianTeams).isEmpty()
    assertThat(teams.lateEatTeams).isEmpty()
  }

  @Test
  fun `아무거나 2, 늦먹 2 투표`() {
    // given
    val user1 = createUser("USER1", 1)
    val user2 = createUser("USER2", 2)
    val user3 = createUser("USER3", 3)
    val user4 = createUser("USER4", 4)

    val ticket1 = createTodayLunchTicket(user1, 1, issuedOrderType = IssuedOrderType.FIRST)
    val ticket2 = createTodayLunchTicket(user2, 2, BobStyleType.LATE_EAT)
    val ticket3 = createTodayLunchTicket(user3, 3, BobStyleType.LATE_EAT)
    val ticket4 = createTodayLunchTicket(user4, 4)
    val bobTeamMaker = BobTeamMaker(listOf(ticket1, ticket2, ticket3, ticket4), FifoTeamSortStrategy)

    // when
    val teams = bobTeamMaker.make(covidStrategy, PayType.ALL)

    // then
    assertThat(teams.lateEatTeams).hasSize(1)
    assertThat(teams.anythingTeams).hasSize(1)
    assertThat(teams.anythingTeams[0].memberTickets).hasSize(2)
    assertThat(teams.anythingTeams[0].leaderTicket).isEqualTo(ticket4)
    assertThat(teams.lateEatTeams[0].memberTickets).hasSize(2)
    assertThat(teams.lateEatTeams[0].leaderTicket).isEqualTo(ticket2)
  }

  @Test
  fun `채식 1, 아무거나 2, 늦먹 2 투표`() {
    val user1 = createUser("USER1", 1)
    val user2 = createUser("USER2", 2)
    val user3 = createUser("USER3", 3)
    val user4 = createUser("USER4", 4)
    val user5 = createUser("USER5", 5)

    val ticket1 = createTodayLunchTicket(user1, 1, issuedOrderType = IssuedOrderType.FIRST)
    val ticket2 = createTodayLunchTicket(user2, 2, BobStyleType.VEGETARIAN, IssuedOrderType.FIRST)
    val ticket3 = createTodayLunchTicket(user3, 3)
    val ticket4 = createTodayLunchTicket(user4, 4, BobStyleType.LATE_EAT)
    val ticket5 = createTodayLunchTicket(user5, 5, BobStyleType.LATE_EAT)
    val bobTeamMaker = BobTeamMaker(listOf(ticket1, ticket2, ticket3, ticket4, ticket5), FifoTeamSortStrategy)

    // when
    val teams = bobTeamMaker.make(covidStrategy, PayType.ALL)

    // then
    assertThat(teams.anythingTeams.size).isEqualTo(1)
    assertThat(teams.lateEatTeams.size).isEqualTo(1)
    assertThat(teams.anythingTeams[0].leaderTicket).isEqualTo(ticket3)
    assertThat(teams.lateEatTeams[0].leaderTicket).isEqualTo(ticket4)
    assertThat(teams.lateEatTeams[0].memberTickets).hasSize(2)
  }

  @Test
  fun `채식 3, 늦먹 2 투표`() {
    val user1 = createUser("USER1", 1)
    val user2 = createUser("USER2", 2)
    val user3 = createUser("USER3", 3)
    val user4 = createUser("USER4", 4)
    val user5 = createUser("USER5", 5)

    val ticket1 = createTodayLunchTicket(user1, 1, BobStyleType.VEGETARIAN, IssuedOrderType.FIRST)
    val ticket2 = createTodayLunchTicket(user2, 2, BobStyleType.VEGETARIAN)
    val ticket3 = createTodayLunchTicket(user3, 3, BobStyleType.VEGETARIAN)
    val ticket4 = createTodayLunchTicket(user4, 4, BobStyleType.LATE_EAT)
    val ticket5 = createTodayLunchTicket(user5, 5, BobStyleType.LATE_EAT)
    val bobTeamMaker = BobTeamMaker(listOf(ticket1, ticket2, ticket3, ticket4, ticket5), FifoTeamSortStrategy)

    // when
    val teams = bobTeamMaker.make(covidStrategy, PayType.ALL)

    // then
    assertThat(teams.vegetarianTeams.size).isEqualTo(1)
    assertThat(teams.lateEatTeams.size).isEqualTo(1)
    assertThat(teams.vegetarianTeams[0].leaderTicket).isEqualTo(ticket2)
    assertThat(teams.lateEatTeams[0].leaderTicket).isEqualTo(ticket4)
    assertThat(teams.lateEatTeams[0].memberTickets).hasSize(2)
  }

  @Test
  fun `채식 2, 늦먹 2 투표`() {
    val user1 = createUser("USER1", 1)
    val user2 = createUser("USER2", 2)
    val user4 = createUser("USER4", 3)
    val user5 = createUser("USER5", 4)

    val ticket1 = createTodayLunchTicket(user1, 1, BobStyleType.VEGETARIAN, IssuedOrderType.FIRST)
    val ticket2 = createTodayLunchTicket(user2, 2, BobStyleType.VEGETARIAN)
    val ticket4 = createTodayLunchTicket(user4, 3, BobStyleType.LATE_EAT)
    val ticket5 = createTodayLunchTicket(user5, 4, BobStyleType.LATE_EAT)
    val bobTeamMaker = BobTeamMaker(listOf(ticket1, ticket2, ticket4, ticket5), FifoTeamSortStrategy)

    // when
    val teams = bobTeamMaker.make(covidStrategy, PayType.ALL)

    // then
    assertThat(teams.vegetarianTeams.size).isEqualTo(1)
    assertThat(teams.lateEatTeams.size).isEqualTo(1)
    assertThat(teams.anythingTeams.size).isEqualTo(0)
    assertThat(teams.vegetarianTeams[0].leaderTicket).isEqualTo(ticket2)
    assertThat(teams.lateEatTeams[0].leaderTicket).isEqualTo(ticket4)
    assertThat(teams.lateEatTeams[0].memberTickets).hasSize(2)
  }

  @Test
  fun `아무거나 2, 채식 2 투표`() {
    val user1 = createUser("USER1", 1)
    val user2 = createUser("USER2", 2)
    val user4 = createUser("USER4", 3)
    val user5 = createUser("USER5", 4)

    val ticket1 = createTodayLunchTicket(user1, 1, BobStyleType.VEGETARIAN, IssuedOrderType.FIRST)
    val ticket2 = createTodayLunchTicket(user2, 2, BobStyleType.VEGETARIAN)
    val ticket4 = createTodayLunchTicket(user4, 3, BobStyleType.ANYTHING, IssuedOrderType.FIRST)
    val ticket5 = createTodayLunchTicket(user5, 4, BobStyleType.ANYTHING)
    val bobTeamMaker = BobTeamMaker(listOf(ticket1, ticket2, ticket4, ticket5), FifoTeamSortStrategy)

    // when
    val teams = bobTeamMaker.make(covidStrategy, PayType.ALL)

    // then
    assertThat(teams.vegetarianTeams).hasSize(1)
    assertThat(teams.anythingTeams).hasSize(1)
    assertThat(teams.vegetarianTeams[0].leaderTicket).isEqualTo(ticket2)
    assertThat(teams.vegetarianTeams[0].memberTickets).hasSize(2)
    assertThat(teams.anythingTeams[0].leaderTicket).isEqualTo(ticket5)
    assertThat(teams.anythingTeams[0].memberTickets).hasSize(2)
  }

  @Test
  fun `각 3명씩 투표`() {
    // given
    val user1 = createUser("USER1", 1)
    val user2 = createUser("USER2", 2)
    val user3 = createUser("USER3", 3)
    val user4 = createUser("USER4", 4)
    val user5 = createUser("USER5", 5)
    val user6 = createUser("USER6", 6)
    val user7 = createUser("USER7", 7)
    val user8 = createUser("USER8", 8)
    val user9 = createUser("USER9", 9)

    val ticket1 = createTodayLunchTicket(user1, 1, BobStyleType.ANYTHING, IssuedOrderType.FIRST)
    val ticket2 = createTodayLunchTicket(user2, 2, BobStyleType.ANYTHING)
    val ticket3 = createTodayLunchTicket(user3, 3, BobStyleType.ANYTHING)
    val ticket4 = createTodayLunchTicket(user4, 4, BobStyleType.VEGETARIAN, IssuedOrderType.FIRST)
    val ticket5 = createTodayLunchTicket(user5, 5, BobStyleType.VEGETARIAN)
    val ticket6 = createTodayLunchTicket(user6, 6, BobStyleType.VEGETARIAN)
    val ticket7 = createTodayLunchTicket(user7, 7, BobStyleType.LATE_EAT)
    val ticket8 = createTodayLunchTicket(user8, 8, BobStyleType.LATE_EAT)
    val ticket9 = createTodayLunchTicket(user9, 9, BobStyleType.LATE_EAT)
    val bobTeamMaker = BobTeamMaker(
      listOf(ticket1, ticket2, ticket3, ticket4, ticket5, ticket6, ticket7, ticket8, ticket9),
      FifoTeamSortStrategy
    )

    // when
    val teams = bobTeamMaker.make(covidStrategy, PayType.ALL)

    // then
    assertThat(teams.anythingTeams).hasSize(1)
    assertThat(teams.vegetarianTeams).hasSize(1)
    assertThat(teams.lateEatTeams).hasSize(1)
    assertThat(teams.anythingTeams[0].leaderTicket).isEqualTo(ticket2)
    assertThat(teams.vegetarianTeams[0].leaderTicket).isEqualTo(ticket5)
    assertThat(teams.lateEatTeams[0].leaderTicket).isEqualTo(ticket7)
    assertThat(teams.anythingTeams[0].memberTickets).hasSize(3)
    assertThat(teams.vegetarianTeams[0].memberTickets).hasSize(3)
    assertThat(teams.lateEatTeams[0].memberTickets).hasSize(3)
  }

  private fun createTodayLunchTicket(
    user: User,
    issuedOrderNumber: Int,
    styleType: BobStyleType = BobStyleType.ANYTHING,
    issuedOrderType: IssuedOrderType = IssuedOrderType.NOT_FIRST,
    time: LocalTime = today.toLocalTime()
  ): BobTicket {
    return BobTicket(
      user.id,
      user.name,
      user.tagString,
      today.toLocalDate(),
      time,
      BobTimeType.LUNCH,
      styleType,
      PayType.SELECTSTAR,
      issuedOrder = issuedOrderType,
      city = user.city,
      issuedOrderNumber = issuedOrderNumber
    )
  }

  private fun createUser(
    userCodeAndName: String,
    id: Long,
    city: CityType = CityType.SEOUL,
    path: UserPathType = UserPathType.SLACK,
  ): User {
    return User(userCodeAndName, userCodeAndName, city, path)
  }

}