package com.catshi.bob.domain

import com.catshi.bob.types.BobStyleType
import com.catshi.bob.types.BobTimeType
import com.catshi.bob.types.IssuedOrderType
import com.catshi.core.domain.User
import com.catshi.core.domain.UserRepository
import com.catshi.core.types.CityType
import com.catshi.core.utils.TimeHandler
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class BobTicketQueryDslRepositoryTest(
    @Autowired private val bobTicketQueryDslRepository: BobTicketQueryDslRepository,
    @Autowired private val bobTicketRepository: BobTicketRepository,
    @Autowired private val userRepository: UserRepository,
) {

    @Test
    fun `어제 투표한 티켓은 오늘 의미가 없다`() {
        // given
        val today = TimeHandler.nowDateTime()
        val yesterday = today.minusDays(1)
        val user = User("현규", "현규")
            .let { userRepository.save(it) }

        BobTicket(
            user,
            yesterday.toLocalDate(),
            yesterday.toLocalTime(),
            BobTimeType.LUNCH,
            BobStyleType.ANYTHING,
            CityType.SEOUL,
            IssuedOrderType.FIRST
        ).let { bobTicketRepository.save(it) }

        // when
        val tickets = bobTicketQueryDslRepository.findThisMealAllTickets(BobTimeType.LUNCH)

        // then
        assertThat(tickets).hasSize(0)
    }

    @Test
    fun `오늘 투표한 티켓은 잘 불러와진다`() {

    }


}