package com.duckvis.bob.domain

import com.duckvis.core.domain.bob.BobTicket
import com.duckvis.core.domain.bob.BobTicketQueryDslRepository
import com.duckvis.core.domain.bob.BobTicketRepository
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.types.bob.BobStyleType
import com.duckvis.core.types.bob.BobTimeType
import com.duckvis.core.types.bob.IssuedOrderType
import com.duckvis.core.types.bob.PayType
import com.duckvis.core.types.shared.CityType
import com.duckvis.core.utils.DateTimeMaker
import org.assertj.core.api.Assertions.assertThat
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
    val today = DateTimeMaker.nowDateTime()
    val yesterday = today.minusDays(1)
    val user = User("현규", "현규")
      .let { userRepository.save(it) }

    BobTicket(
      user.id,
      user.name,
      user.tagString,
      yesterday.toLocalDate(),
      yesterday.toLocalTime(),
      BobTimeType.LUNCH,
      BobStyleType.ANYTHING,
      PayType.SELECTSTAR,
      CityType.SEOUL,
      1,
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