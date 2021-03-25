package com.duckvis.bob.services

import com.duckvis.bob.domain.BobHistoryRepository
import com.duckvis.bob.domain.BobTicketRepository
import com.duckvis.bob.domain.MenuRepository
import com.duckvis.core.domain.User
import com.duckvis.core.domain.UserRepository
import com.duckvis.core.utils.TimeHandler
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
internal class OutputParserTest(
    @Autowired private val bob: Bob,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val ticketRepository: BobTicketRepository,
    @Autowired private val menuRepository: MenuRepository,
    @Autowired private val bobHistoryRepository: BobHistoryRepository,
) {
    @AfterEach
    fun clearAll() {
        bobHistoryRepository.deleteAll()
        ticketRepository.deleteAll()
        userRepository.deleteAll()
        menuRepository.deleteAll()
    }

    @Test
    fun `1등으로 신청했을 때 메시지 정상적으로 뜬다`() {
        // given
        val user1 = User("유저", "유저").let { userRepository.save(it) }
        val ticket1 = bob.responseMe(user1.id, TimeHandler.nowDateTime().withHour(1))

//        // when
//        val message = outputParser.meMessage(CountryType.KOREA, ticket1, ticketDao.isFirst())
//
//        // then
//        assertThat(message).contains("유저님, 1등하셨네요 너무 귀엽다")
    }
}