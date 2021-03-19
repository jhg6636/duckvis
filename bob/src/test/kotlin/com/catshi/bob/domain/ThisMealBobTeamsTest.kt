package com.catshi.bob.domain

import com.catshi.bob.types.BobStyleType
import com.catshi.bob.types.BobTimeType
import com.catshi.bob.types.IssuedOrderType
import com.catshi.core.domain.User
import com.catshi.core.types.CityType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class ThisMealBobTeamsTest {

    @Test
    fun `아무거나 먹는 인간만 있을 때`() {
        // given
        val date = LocalDate.now()
        val time = LocalTime.now()
        val user1 = User("윤택", "윤택")
        val user2 = User("현규", "현규")
        val user3 = User("주섭", "주섭")
        val user4 = User("태현", "태현")
        val tickets = listOf(
            BobTicket(user1, date, time, BobTimeType.LUNCH, BobStyleType.ANYTHING, CityType.SEOUL, IssuedOrderType.FIRST),
            BobTicket(user2, date, time, BobTimeType.LUNCH, BobStyleType.ANYTHING, CityType.SEOUL),
            BobTicket(user3, date, time, BobTimeType.LUNCH, BobStyleType.ANYTHING, CityType.SEOUL),
            BobTicket(user4, date, time, BobTimeType.LUNCH, BobStyleType.ANYTHING, CityType.SEOUL),
        )
        val bobTeam = BobTeam(tickets)

        // when
        val string = ThisMealBobTeams(listOf(), listOf(bobTeam)).toString()

        // then
        assertThat(string).isEqualTo("서울의 오늘 밥팀!!! 오늘 1등은 윤택!!!\n" +
                "현규 팀 : <@윤택> <@현규> <@주섭> <@태현>")

    }

}