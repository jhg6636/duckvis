package com.catshi.bob.services

import com.catshi.bob.domain.BobHistoryRepository
import com.catshi.bob.domain.BobTicket
import com.catshi.bob.domain.BobTicketRepository
import com.catshi.bob.domain.MenuRepository
import com.catshi.bob.exceptions.NeverEatThisMonthException
import com.catshi.bob.exceptions.NotEnoughBobTicketException
import com.catshi.bob.exceptions.NotTicketTimeException
import com.catshi.bob.types.BobStyleType
import com.catshi.bob.types.BobTimeType
import com.catshi.bob.types.IssuedOrderType
import com.catshi.bob.types.StatisticsOption
import com.catshi.core.domain.User
import com.catshi.core.domain.UserRepository
import com.catshi.core.types.CityType
import com.catshi.core.utils.TimeHandler
import org.assertj.core.api.Assertions.assertThat
import org.hibernate.exception.ConstraintViolationException
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import org.junit.jupiter.api.assertThrows
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
class BobServiceTest(
    @Autowired private val bobService: BobService,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val ticketRepository: BobTicketRepository,
    @Autowired private val menuRepository: MenuRepository,
    @Autowired private val bobHistoryRepository: BobHistoryRepository,
) {

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
        assertThrows<NotTicketTimeException> {
            bobService.responseMe(user.id, now)
        }
    }

    @Test
    @Transactional
    fun `점심 시간에 1등으로 밥투표를 신청했다`() {
        // given
        val user1 = createUser("user1")
        val user2 = createUser("user2")
        val user3 = createUser("user3")
        val now = LocalDateTime.now().withHour(BobTimeType.LUNCH.startHour)

        // when
        val ticket1 = bobService.responseMe(user1.id, now)
        val ticket2 = bobService.responseMe(user2.id, now)
        val ticket3 = bobService.responseMe(user3.id, now)

        bobService.determineBobTeam(CityType.SEOUL, BobTimeType.LUNCH)

        // then
        assertThat(ticket1.isFirst).isTrue
        assertThat(ticket2.isFirst).isFalse
        assertThat(ticket3.isFirst).isFalse
        assertThat(ticket1.bobTimeType).isEqualTo(BobTimeType.LUNCH)
    }

    @Test
    fun `메뉴 추천을 받는다`() {
        // given
        bobService.responseAddMenu("덮밥")
        bobService.responseAddMenu("국밥")

        // when
        val recommendedMenu = bobService.responseRecommendMenu(listOf())

        // then
        assertThat(recommendedMenu.name).isIn(listOf("덮밥", "국밥"))
    }

    @Test
    fun `제외한 메뉴가 메뉴 추천에 표시되지 않는다`() {
        // given
        bobService.responseAddMenu("덮밥")
        bobService.responseAddMenu("국밥")

        // when
        val recommendedMenu = bobService.responseRecommendMenu(listOf("덮밥"))

        // then
        assertThat(recommendedMenu.name).isEqualTo("국밥")
    }

    @Test
    fun `밥을 한 번도 안 먹은 상황에서 밥 통계를 확인한다`() {
        // given
        val user = createUser("user1")

        // when & then
        assertThrows<NeverEatThisMonthException> {
            bobService.responseStatistics(user.id, StatisticsOption.ALL)
        }
    }

    @Test
    fun `밥팀 기록`() {
        // given
        val user1 = createUser("user1")
        val user2 = createUser("user2")
        val user3 = createUser("user3")
        val user4 = createUser("user4")
        val now = LocalDateTime.now().withHour(BobTimeType.LUNCH.startHour)

        bobService.responseMe(user1.id, now)
        bobService.responseMe(user2.id, now)
        bobService.responseMe(user3.id, now)
        bobService.responseMe(user4.id, now)

        val bobTeams = bobService.determineBobTeam(CityType.SEOUL, BobTimeType.LUNCH, FifoTeamDecisionLogic(), FifoTeamSortStrategy)

        // when
        val bobHistories = bobService.archiveBobTeam(bobTeams)

        // then
        assertThat(bobHistories).hasSize(4)
    }

    @Test
    fun `밥부장 했을 때 밥 통계 확인`() {
        // given
        val user1 = createUser("user1")
        val user2 = createUser("user2")
        val user3 = createUser("user3")
        val user4 = createUser("user4")
        val now = LocalDateTime.now().withHour(BobTimeType.LUNCH.startHour)

        bobService.responseMe(user1.id, now)
        bobService.responseMe(user2.id, now)
        bobService.responseMe(user3.id, now)
        bobService.responseMe(user4.id, now)

        val bobTeams = bobService.determineBobTeam(CityType.SEOUL, BobTimeType.LUNCH, FifoTeamDecisionLogic(), FifoTeamSortStrategy)
        bobService.archiveBobTeam(bobTeams)

        // when
        val statisticsDto = bobService.responseStatistics(user3.id, StatisticsOption.ALL)

        // then
        assertThat(statisticsDto.leaderCount).isEqualTo(1)
        assertThat(statisticsDto.freqeuntTeammateName).isEqualTo("user4")
        assertThat(statisticsDto.frequency).isEqualTo(1)
    }

    @Test
    fun `밥팀 없을 때 밥팀 보여주기`() {
        // given

        // when & then
        assertThrows<NotEnoughBobTicketException> {
            bobService.determineBobTeam(CityType.SEOUL, BobTimeType.LUNCH)
        }
    }

    @Test
    fun `4명 밥 신청했을 때 밥팀 보여주기`() {
        // given
        val user1 = createUser("user1")
        val user2 = createUser("user2")
        val user3 = createUser("user3")
        val user4 = createUser("user4")

        // when
        val ticket1 = createTodayLunchTicket(user1, issuedOrderType = IssuedOrderType.FIRST)
        val ticket2 = createTodayLunchTicket(user2)
        val ticket3 = createTodayLunchTicket(user3)
        val ticket4 = createTodayLunchTicket(user4)
        ticketRepository.saveAll(listOf(ticket1, ticket2, ticket3, ticket4))

        val bobTeam = bobService.determineBobTeam(CityType.SEOUL, BobTimeType.LUNCH)

        // then
        assertThat(bobTeam.anythingTeams).hasSize(2)
    }

    @Test
    fun `1명이 티켓 2개 가지고 있을 때 밥팀 짜기`() {
        // given
        val user1 = createUser("user1")
        val now = LocalDateTime.now().withHour(BobTimeType.LUNCH.startHour)

        val ticket1 = createTodayLunchTicket(user1, issuedOrderType = IssuedOrderType.FIRST)
        val ticket2 = createTodayLunchTicket(user1)

        // when & then
        assertThrows<DataIntegrityViolationException> {
            ticketRepository.saveAll(listOf(ticket1, ticket2))
        }
    }

    private fun createUser(userCodeAndName: String): User {
        return User(userCodeAndName, userCodeAndName).let { userRepository.save(it) }
    }

    private fun createTodayLunchTicket(
        user: User,
        styleType: BobStyleType = BobStyleType.ANYTHING,
        issuedOrderType: IssuedOrderType = IssuedOrderType.NOT_FIRST,
    ): BobTicket {
        return BobTicket(
            user,
            TimeHandler.nowDate(),
            TimeHandler.nowDateTime().toLocalTime().withHour(1),
            BobTimeType.LUNCH,
            styleType,
            issuedOrder = issuedOrderType,
            city = user.city,
        )
    }
}