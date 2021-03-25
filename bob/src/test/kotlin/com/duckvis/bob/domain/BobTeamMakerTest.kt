package com.duckvis.bob.domain

import com.duckvis.bob.exceptions.NotEnoughBobTicketException
import com.duckvis.bob.types.BobStyleType
import com.duckvis.bob.types.BobTimeType
import com.duckvis.bob.types.IssuedOrderType
import com.duckvis.core.domain.User
import com.duckvis.core.types.CityType
import com.duckvis.core.types.UserPathType
import com.duckvis.bob.services.BobTeamMaker
import com.duckvis.bob.services.CovidTeamDecisionLogic
import com.duckvis.bob.services.FifoTeamDecisionLogic
import com.duckvis.bob.services.FifoTeamSortStrategy
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import java.time.ZoneOffset

@ActiveProfiles("test")
class BobTeamMakerTest {

    private val today = LocalDateTime.now(ZoneOffset.UTC)

    private val covidStrategy = CovidTeamDecisionLogic()

    @Test
    fun `3명 같은 지역, 같은 식성으로 투표했다`() {
        // given
        val user1 = createUser("USER1")
        val user2 = createUser("USER2")
        val user3 = createUser("USER3")

        val ticket1 = createTodayLunchTicket(user1, issuedOrderType = IssuedOrderType.FIRST)
        val ticket2 = createTodayLunchTicket(user2, issuedOrderType = IssuedOrderType.NOT_FIRST)
        val ticket3 = createTodayLunchTicket(user3, issuedOrderType = IssuedOrderType.NOT_FIRST)
        val bobTeamMaker = BobTeamMaker(listOf(ticket1, ticket2, ticket3), FifoTeamSortStrategy)

        // when
        val teams = bobTeamMaker.make(covidStrategy)

        // then
        assertThat(teams.anythingTeams.size).isEqualTo(1)
        assertThat(teams.anythingTeams[0].memberTickets).hasSize(3)
        assertThat(teams.anythingTeams[0].leaderTicket.user).isNotEqualTo(user1)
    }

    @Test
    fun `4명 같은 지역, 같은 식성으로 투표했다`() {
        val user1 = createUser("USER1")
        val user2 = createUser("USER2")
        val user3 = createUser("USER3")
        val user4 = createUser("USER4")

        val ticket1 = createTodayLunchTicket(user1, issuedOrderType = IssuedOrderType.FIRST)
        val ticket2 = createTodayLunchTicket(user2, issuedOrderType = IssuedOrderType.NOT_FIRST)
        val ticket3 = createTodayLunchTicket(user3, issuedOrderType = IssuedOrderType.NOT_FIRST)
        val ticket4 = createTodayLunchTicket(user4, issuedOrderType = IssuedOrderType.NOT_FIRST)
        val bobTeamMaker = BobTeamMaker(listOf(ticket1, ticket2, ticket3, ticket4), FifoTeamSortStrategy)

        // when
        val teams = bobTeamMaker.make(covidStrategy)

        // then
        assertThat(teams.anythingTeams.size).isEqualTo(2)
        assertThat(teams.anythingTeams[0].memberTickets).hasSize(2)
        assertThat(teams.anythingTeams[0].leaderTicket.user).isNotEqualTo(user1)
        assertThat(teams.anythingTeams[1].leaderTicket.user).isNotEqualTo(user1)
    }

    @Test
    fun `5명 모두 같은 지역, 같은 식성으로 투표했다`() {
        val user1 = createUser("USER1")
        val user2 = createUser("USER2")
        val user3 = createUser("USER3")
        val user4 = createUser("USER4")
        val user5 = createUser("USER5")

        val ticket1 = createTodayLunchTicket(user1, issuedOrderType = IssuedOrderType.FIRST)
        val ticket2 = createTodayLunchTicket(user2, issuedOrderType = IssuedOrderType.NOT_FIRST)
        val ticket3 = createTodayLunchTicket(user3, issuedOrderType = IssuedOrderType.NOT_FIRST)
        val ticket4 = createTodayLunchTicket(user4, issuedOrderType = IssuedOrderType.NOT_FIRST)
        val ticket5 = createTodayLunchTicket(user5, issuedOrderType = IssuedOrderType.NOT_FIRST)
        val bobTeamMaker = BobTeamMaker(listOf(ticket1, ticket2, ticket3, ticket4, ticket5), FifoTeamSortStrategy)

        // when
        val teams = bobTeamMaker.make(covidStrategy)

        // then
        assertThat(teams.anythingTeams.size).isEqualTo(2)
        assertThat(teams.anythingTeams[0].memberTickets.size + teams.anythingTeams[1].memberTickets.size).isEqualTo(5)
        assertThat(teams.anythingTeams[0].leaderTicket.user).isNotEqualTo(user1)
        assertThat(teams.anythingTeams[1].leaderTicket.user).isNotEqualTo(user1)
    }

    @Test
    fun `6명 모두 같은 지역, 같은 식성으로 투표했다`() {
        val user1 = createUser("USER1")
        val user2 = createUser("USER2")
        val user3 = createUser("USER3")
        val user4 = createUser("USER4")
        val user5 = createUser("USER5")
        val user6 = createUser("USER6")

        val ticket1 = createTodayLunchTicket(user1, issuedOrderType = IssuedOrderType.FIRST)
        val ticket2 = createTodayLunchTicket(user2, issuedOrderType = IssuedOrderType.NOT_FIRST)
        val ticket3 = createTodayLunchTicket(user3, issuedOrderType = IssuedOrderType.NOT_FIRST)
        val ticket4 = createTodayLunchTicket(user4, issuedOrderType = IssuedOrderType.NOT_FIRST)
        val ticket5 = createTodayLunchTicket(user5, issuedOrderType = IssuedOrderType.NOT_FIRST)
        val ticket6 = createTodayLunchTicket(user6, issuedOrderType = IssuedOrderType.NOT_FIRST)
        val bobTeamMaker = BobTeamMaker(listOf(ticket1, ticket2, ticket3, ticket4, ticket5, ticket6), FifoTeamSortStrategy)

        // when
        val teams = bobTeamMaker.make(covidStrategy)

        // then
        assertThat(teams.anythingTeams.size).isEqualTo(2)
        assertThat(teams.anythingTeams[0].memberTickets).hasSize(3)
        assertThat(teams.anythingTeams[1].memberTickets).hasSize(3)
        assertThat(teams.anythingTeams[0].leaderTicket.user).isEqualTo(user2)
        assertThat(teams.anythingTeams[1].leaderTicket.user).isEqualTo(user4)

    }

    @Test
    fun `3명 미만 투표했다`() {
        // given
        val user1 = createUser("USER1")
        val user2 = createUser("USER2")

        val ticket1 = createTodayLunchTicket(user1, issuedOrderType = IssuedOrderType.FIRST)
        val ticket2 = createTodayLunchTicket(user2, issuedOrderType = IssuedOrderType.NOT_FIRST)

        val bobTeamMaker = BobTeamMaker(listOf(ticket1, ticket2), FifoTeamSortStrategy)

        // when & then
        assertThrows<NotEnoughBobTicketException> {
            bobTeamMaker.make(covidStrategy)
        }
    }

    @Test
    fun `채식 3명 미만, 아무거나 3명 미만, 합치면 3명 이상 투표`() {
        // given
        val user1 = createUser("USER1")
        val user2 = createUser("USER2")
        val user3 = createUser("USER3")

        val ticket1 = createTodayLunchTicket(user1, issuedOrderType = IssuedOrderType.FIRST)
        val ticket2 = createTodayLunchTicket(user2, BobStyleType.VEGETARIAN)
        val ticket3 = createTodayLunchTicket(user3)
        val bobTeamMaker = BobTeamMaker(listOf(ticket1, ticket2, ticket3), FifoTeamSortStrategy)

        // when
        val teams = bobTeamMaker.make(covidStrategy)

        // then
        assertThat(teams.vegetarianTeams).hasSize(0)
        assertThat(teams.anythingTeams).hasSize(1)
        assertThat(teams.anythingTeams[0].memberTickets).hasSize(3)
        assertThat(teams.anythingTeams[0].leaderTicket.user).isNotEqualTo(user1)
    }


    @Test
    fun `채식 3명 미만, 아무거나 3명 이상 투표`() {
        // given
        val user1 = createUser("USER1")
        val user2 = createUser("USER2")
        val user3 = createUser("USER3")
        val user4 = createUser("USER4")

        val ticket1 = createTodayLunchTicket(user1, issuedOrderType = IssuedOrderType.FIRST)
        val ticket2 = createTodayLunchTicket(user2, BobStyleType.VEGETARIAN)
        val ticket3 = createTodayLunchTicket(user3)
        val ticket4 = createTodayLunchTicket(user4)

        val bobTeamMaker = BobTeamMaker(listOf(ticket1, ticket2, ticket3, ticket4), FifoTeamSortStrategy)

        // when
        val teams = bobTeamMaker.make(covidStrategy)

        // then
        assertThat(teams.vegetarianTeams).hasSize(0)
        assertThat(teams.anythingTeams).hasSize(2)
        assertThat(teams.anythingTeams[0].memberTickets).hasSize(2)
        assertThat(teams.anythingTeams[1].memberTickets).hasSize(2)
        assertThat(teams.anythingTeams[0].leaderTicket.user).isNotEqualTo(user1)
        assertThat(teams.anythingTeams[1].leaderTicket.user).isNotEqualTo(user1)
    }

    @Test
    fun `채식 3명 이상, 아무거나 3명 미만 투표`() {
        // given
        val user1 = createUser("USER1")
        val user2 = createUser("USER2")
        val user3 = createUser("USER3")
        val user4 = createUser("USER4")

        val ticket1 = createTodayLunchTicket(user1, issuedOrderType = IssuedOrderType.FIRST)
        val ticket2 = createTodayLunchTicket(user2, BobStyleType.VEGETARIAN)
        val ticket3 = createTodayLunchTicket(user3, BobStyleType.VEGETARIAN)
        val ticket4 = createTodayLunchTicket(user4, BobStyleType.VEGETARIAN)

        val bobTeamMaker = BobTeamMaker(listOf(ticket4, ticket1, ticket2, ticket3), FifoTeamSortStrategy)

        // when
        val teams = bobTeamMaker.make(covidStrategy)

        // then
        assertThat(teams.vegetarianTeams).hasSize(0)
        assertThat(teams.anythingTeams).hasSize(2)
        assertThat(teams.anythingTeams[0].memberTickets).hasSize(2)
        assertThat(teams.anythingTeams[1].memberTickets).hasSize(2)
        assertThat(teams.anythingTeams[0].leaderTicket.user).isNotEqualTo(user1)
        assertThat(teams.anythingTeams[1].leaderTicket.user).isNotEqualTo(user1)
    }

    @Test
    fun `1등이 되면 밥부장을 면제 받는다`() {
        // given
        val user1 = createUser("USER1")
        val user2 = createUser("USER2")
        val user3 = createUser("USER3")

        val ticket1 = createTodayLunchTicket(user1, issuedOrderType = IssuedOrderType.FIRST)
        val ticket2 = createTodayLunchTicket(user2)
        val ticket3 = createTodayLunchTicket(user3)
        val bobTeamMaker = BobTeamMaker(listOf(ticket1, ticket2, ticket3), FifoTeamSortStrategy)

        // when
        val teams = bobTeamMaker.make(FifoTeamDecisionLogic())

        // then
        assertThat(teams.anythingTeams.size).isEqualTo(1)
        assertThat(teams.anythingTeams[0].leaderTicket).isEqualTo(ticket2)
    }

    @Test
    fun `채식 1등, 아무거나 1등이 같은 3인 팀에 들어간다`() {
        // given
        val user1 = createUser("USER1")
        val user2 = createUser("USER2")
        val user3 = createUser("USER3")
        val user4 = createUser("USER4")
        val user5 = createUser("USER5")

        val ticket1 = createTodayLunchTicket(user1, issuedOrderType = IssuedOrderType.FIRST)
        val ticket2 = createTodayLunchTicket(user2, BobStyleType.VEGETARIAN, IssuedOrderType.FIRST)
        val ticket3 = createTodayLunchTicket(user3)
        val ticket4 = createTodayLunchTicket(user4)
        val ticket5 = createTodayLunchTicket(user5)
        val bobTeamMaker = BobTeamMaker(listOf(ticket1, ticket2, ticket3, ticket4, ticket5), FifoTeamSortStrategy)

        // when
        val teams = bobTeamMaker.make(FifoTeamDecisionLogic())

        // then
        assertThat(teams.anythingTeams.size).isEqualTo(2)
        assertThat(teams.anythingTeams[0].leaderTicket).isEqualTo(ticket3)
    }

    @Test
    fun `채식 1등, 아무거나 1등이 같은 2인 팀에 들어간다`() {
        // given
        val user1 = createUser("USER1")
        val user2 = createUser("USER2")
        val user3 = createUser("USER3")
        val user4 = createUser("USER4")

        val ticket1 = createTodayLunchTicket(user1, issuedOrderType = IssuedOrderType.FIRST)
        val ticket2 = createTodayLunchTicket(user2, BobStyleType.VEGETARIAN, IssuedOrderType.FIRST)
        val ticket3 = createTodayLunchTicket(user3)
        val ticket4 = createTodayLunchTicket(user4)
        val bobTeamMaker = BobTeamMaker(listOf(ticket1, ticket2, ticket3, ticket4), FifoTeamSortStrategy)

        // when
        val teams = bobTeamMaker.make(FifoTeamDecisionLogic())

        // then
        assertThat(teams.anythingTeams.size).isEqualTo(2)
        assertThat(teams.anythingTeams[0].leaderTicket).isEqualTo(ticket1)
    }

    @Test
    fun `채식만 3명 모였을 때도 채식팀끼리 배정이 되어야 된다`() {

    }

    private fun createTodayLunchTicket(
        user: User,
        styleType: BobStyleType = BobStyleType.ANYTHING,
        issuedOrderType: IssuedOrderType = IssuedOrderType.NOT_FIRST,
    ): BobTicket {
        return BobTicket(
            user,
            today.toLocalDate(),
            today.toLocalTime(),
            BobTimeType.LUNCH,
            styleType,
            issuedOrder = issuedOrderType,
            city = user.city,
        )
    }

    private fun createUser(
        userCodeAndName: String,
        city: CityType = CityType.SEOUL,
        path: UserPathType = UserPathType.SLACK
    ): User {
        return User(userCodeAndName, userCodeAndName, city, path)
    }

}