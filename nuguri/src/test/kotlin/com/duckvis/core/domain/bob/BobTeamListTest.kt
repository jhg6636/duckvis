package com.duckvis.core.domain.bob

import com.duckvis.core.types.bob.BobStyleType
import com.duckvis.core.types.bob.BobTimeType
import com.duckvis.core.types.bob.IssuedOrderType
import com.duckvis.core.types.bob.PayType
import com.duckvis.core.types.shared.CityType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime


class BobTeamListTest {
  @Test
  fun `뭔 카드로 결제했는지 나온다`() {
    // given
    val bobTeam1 = BobTeam(
      listOf(
        BobTicket(
          1L,
          "1",
          "1",
          LocalDate.now(),
          LocalTime.now(),
          BobTimeType.LUNCH,
          BobStyleType.ANYTHING,
          PayType.SELECTSTAR,
          CityType.SEOUL,
          1,
          IssuedOrderType.FIRST
        ),
        BobTicket(
          2L,
          "2",
          "2",
          LocalDate.now(),
          LocalTime.now(),
          BobTimeType.LUNCH,
          BobStyleType.ANYTHING,
          PayType.SELECTSTAR,
          CityType.SEOUL,
          2
        ),
        BobTicket(
          3L,
          "3",
          "3",
          LocalDate.now(),
          LocalTime.now(),
          BobTimeType.LUNCH,
          BobStyleType.ANYTHING,
          PayType.SELECTSTAR,
          CityType.SEOUL,
          3
        )
      )
    )
    val bobTeamList = BobTeamList(listOf(bobTeam1))

    // when
    val bobString = bobTeamList.asString(0, PayType.SELECTSTAR)
    println(bobString)

    // then
    assertThat(bobString.contains("서울의 오늘 점심 밥 법카 결제"))
  }
}