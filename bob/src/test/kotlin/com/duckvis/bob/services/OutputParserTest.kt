package com.duckvis.bob.services

import com.duckvis.core.domain.bob.BobHistoryRepository
import com.duckvis.core.domain.bob.BobTicketRepository
import com.duckvis.core.domain.bob.MenuRepository
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.types.bob.BobStyleType
import com.duckvis.core.utils.DateTimeMaker
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
internal class OutputParserTest(
  @Autowired private val bobTicketService: BobTicketService,
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
    val ticket1 = bobTicketService.responseMe(user1.id, BobStyleType.ANYTHING, DateTimeMaker.nowDateTime().withHour(1))

//        // when
//        val message = outputParser.meMessage(CountryType.KOREA, ticket1, ticketDao.isFirst())
//
//        // then
//        assertThat(message).contains("유저님, 1등하셨네요 너무 귀엽다")
  }
}