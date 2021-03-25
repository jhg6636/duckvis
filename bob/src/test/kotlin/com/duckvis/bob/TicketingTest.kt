package com.duckvis.bob

import com.duckvis.bob.domain.BobTicket
import com.duckvis.bob.domain.BobTicketQueryDslRepository
import com.duckvis.bob.domain.BobTicketRepository
import com.duckvis.bob.exceptions.BobTicketAlreadyExistsException
import com.duckvis.bob.exceptions.NoBobTicketException
import com.duckvis.bob.services.Bob
import com.duckvis.bob.types.BobStyleType
import com.duckvis.bob.types.BobTimeType
import com.duckvis.bob.types.IssuedOrderType
import com.duckvis.core.domain.User
import com.duckvis.core.domain.UserRepository
import com.duckvis.core.types.CityType
import com.duckvis.core.utils.TimeHandler
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import javax.transaction.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
open class TicketingTest @Autowired constructor(
    val bob: Bob,
    val bobTicketRepository: BobTicketRepository,
    val userRepository: UserRepository,
    val bobTicketQueryDslRepository: BobTicketQueryDslRepository,
) {
    lateinit var user1: User
    lateinit var user2: User
    lateinit var user3: User
    lateinit var daejeonUser: User

    @Transactional
    open fun ticketing(
        user: User,
        styleType: BobStyleType = BobStyleType.ANYTHING,
        cityType: CityType = CityType.SEOUL
    ): BobTicket {
        return BobTicket(user, TimeHandler.nowDate(), TimeHandler.nowDateTime().toLocalTime().withHour(7), BobTimeType.DINNER, styleType, cityType)
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
        return TimeHandler.nowDateTime().withHour(7)
    }

    @Test
    fun `이번 타임에 티켓이 없는데 저요 했다`() {
        // given

        // when
        val ticket = bob.responseMe(user1.id, fourOClock())

        // then
        assertThat(ticket.bobStyle).isEqualTo(BobStyleType.ANYTHING)
        assertThat(ticket.city).isEqualTo(CityType.SEOUL)
        assertThat(bobTicketRepository.count()).isEqualTo(1)
    }

    @Test
    fun `이번 타임에 티켓이 없는데 채식 했다`() {
        // given

        // when
        val ticket = bob.responseVegetarian(user1.id, fourOClock())

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
            bob.responseNotMe(user1.id, fourOClock())
        }
    }

    @Test
    fun `이번 타임에 티켓이 없는데 서울에서 했다`() {
        // given
        ticketing(user2)

        // when
        val ticket = bob.responseMeSpecificPlace(user1.id, CityType.SEOUL, fourOClock())

        // then
        assertThat(ticket.bobStyle).isEqualTo(BobStyleType.ANYTHING)
        assertThat(ticket.city).isEqualTo(CityType.SEOUL)
        assertThat(bobTicketRepository.count()).isEqualTo(2)
    }

    @Test
    fun `이번 타임에 티켓이 없는데 대전에서 했다`() {
        // given
        ticketing(user2)

        // when
        val ticket = bob.responseMeSpecificPlace(user1.id, CityType.DAEJEON, fourOClock())

        // then
        assertThat(ticket.bobStyle).isEqualTo(BobStyleType.ANYTHING)
        assertThat(ticket.city).isEqualTo(CityType.DAEJEON)
        assertThat(bobTicketRepository.count()).isEqualTo(2)
    }

    @Test
    fun `이번 타임에 티켓이 없는데 서울채식 했다`() {
        // given
        ticketing(user1)
        ticketing(user3)

        // when
        val ticket = bob.responseVegetarianSpecificPlace(user2.id, CityType.SEOUL, fourOClock())

        // then
        assertThat(ticket.bobStyle).isEqualTo(BobStyleType.VEGETARIAN)
        assertThat(ticket.city).isEqualTo(CityType.SEOUL)
        assertThat(bobTicketRepository.count()).isEqualTo(3)
    }

    @Test
    fun `이번 타임에 티켓이 없는데 대전채식 했다`() {
        // given
        ticketing(user1)
        ticketing(user3)

        // when
        val ticket = bob.responseVegetarianSpecificPlace(user2.id, CityType.DAEJEON, fourOClock())

        // then
        assertThat(ticket.bobStyle).isEqualTo(BobStyleType.VEGETARIAN)
        assertThat(ticket.city).isEqualTo(CityType.DAEJEON)
        assertThat(bobTicketRepository.count()).isEqualTo(3)
    }

    @Test
    fun `이번 타임에 아무거나 티켓이 있는데 안먹 했다`() {
        // given
        ticketing(user1)
        ticketing(user2)
        println(bobTicketRepository.count())
        println("user1 id")
        println(user1.id)
        // when
        bob.responseNotMe(user1.id, fourOClock())

        // then
        assertThat(bobTicketRepository.count()).isEqualTo(1)
    }

    @Test
    fun `이번 타임에 채식 티켓이 있는데 안먹 했다`() {
        // given
        ticketing(user1)
        ticketing(user2, BobStyleType.VEGETARIAN)

        // when
        bob.responseNotMe(user2.id, fourOClock())

        // then
        assertThat(bobTicketRepository.count()).isEqualTo(1)
    }

    @Test
    fun `이번 타임에 아무거나 티켓이 있는데 저요 했다`() {
        // given
        ticketing(user1)

        // when & then
        assertThrows<BobTicketAlreadyExistsException> {
            bob.responseMe(user1.id, fourOClock())
        }
    }

    @Test
    fun `이번 타임에 아무거나 티켓이 있는데 채식 했다`() {
        // given
        ticketing(user1)

        // when
        val ticket = bob.responseVegetarian(user1.id, fourOClock())

        // then
        assertThat(ticket.bobStyle).isEqualTo(BobStyleType.VEGETARIAN)
        assertThat(ticket.city).isEqualTo(CityType.SEOUL)
        assertThat(bobTicketRepository.count()).isEqualTo(1)
    }

    @Test
    fun `이번 타임에 채식 티켓이 있는데 저요 했다`() {
        // given
        ticketing(user1, BobStyleType.VEGETARIAN)

        // when
        val ticket = bob.responseMe(user1.id, fourOClock())

        // then
        assertThat(ticket.bobStyle).isEqualTo(BobStyleType.ANYTHING)
        assertThat(ticket.city).isEqualTo(CityType.SEOUL)
        assertThat(bobTicketRepository.count()).isEqualTo(1)
    }

    @Test
    fun `이번 타임에 채식 티켓이 있는데 채식 했다`() {
        // given
        ticketing(user1, BobStyleType.VEGETARIAN)

        // when & then
        assertThrows<BobTicketAlreadyExistsException> {
            bob.responseVegetarian(user1.id, fourOClock())
        }
    }

    @Test
    @Transactional
    fun `이번 타임에 아무도 투표 안한 상황에서 투표했을 때 1등이 된다`() {
        // given
        val ticket1 = bob.responseMe(user1.id, fourOClock())
        val ticket2 = bob.responseMe(user2.id, fourOClock())
        val ticket3 = bob.responseMe(user3.id, fourOClock())

        // when
        bob.determineBobTeam(CityType.SEOUL, BobTimeType.DINNER)

        // then
        assertThat(ticket1.issuedOrder).isEqualTo(IssuedOrderType.FIRST)
    }

    @Test
    fun `서울 사람이 이번 타임에 아무거나 서울 티켓이 있는데 저요 했다`() {
        // given
        ticketing(user1)

        // when & then
        assertThrows<BobTicketAlreadyExistsException> {
            bob.responseMe(user1.id, fourOClock())
        }
    }

    @Test
    fun `대전 사람이 이번 타임에 아무거나 서울 티켓이 있는데 저요 했다`() {
        // given
        ticketing(daejeonUser)

        // when
        val ticket = bob.responseMe(daejeonUser.id, fourOClock())

        // then
        assertThat(ticket.bobStyle).isEqualTo(BobStyleType.ANYTHING)
        assertThat(ticket.city).isEqualTo(CityType.DAEJEON)
        assertThat(bobTicketRepository.count()).isEqualTo(1)
    }

    @Test
    fun `이번 타임에 아무거나 서울 티켓이 있는데 서울에서 했다`() {
        // given
        ticketing(user1)

        // when & then
        assertThrows<BobTicketAlreadyExistsException> {
            bob.responseMeSpecificPlace(user1.id, CityType.SEOUL, fourOClock())
        }
    }

    @Test
    fun `이번 타임에 1명 이상 투표한 상황에서 투표했을 때 1등이 아니게 된다`() {
        // given
        ticketing(user1)
        ticketing(user2)

        // when
        val ticket = bob.responseMe(user3.id, fourOClock())

        // then
        assertThat(ticket.issuedOrder).isEqualTo(IssuedOrderType.NOT_FIRST)
    }
}