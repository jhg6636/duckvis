package com.duckvis.nuguri.services

import com.duckvis.core.domain.nuguri.HolidayRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.types.nuguri.HolidayType
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.nuguri.domain.admin.service.holiday.SetHolidaysService
import com.duckvis.nuguri.utils.AssertNuguriException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.DateTimeException
import java.time.LocalDate

@SpringBootTest
@ActiveProfiles("test")
class SetHolidaysServiceTest(
  @Autowired private val setHolidaysService: SetHolidaysService,
  @Autowired private val holidayRepository: HolidayRepository,
) {
  @BeforeEach
  @Transactional
  fun prepare() {

  }

  @AfterEach
  @Transactional
  fun clear() {
    holidayRepository.deleteAllInBatch()
  }

  private fun getThisMondayAndTuesDay(date: LocalDate = DateTimeMaker.nowDate()): List<LocalDate> {
    return listOf(
      date.minusDays(date.dayOfWeek.value.toLong() - 1),
      date.minusDays(date.dayOfWeek.value.toLong() - 2)
    )
  }

  private fun getWeekend(date: LocalDate = DateTimeMaker.nowDate()): List<LocalDate> {
    return listOf(
      date.minusDays(date.dayOfWeek.value.toLong()),
      date.minusDays(date.dayOfWeek.value.toLong() + 1)
    )
  }

  @Test
  fun `휴일로 이번주 월, 화요일을 지정한다`() {
    // given

    // when
    setHolidaysService.setHolidays(getThisMondayAndTuesDay(), HolidayType.SPECIAL)

    // then
    assertThat(holidayRepository.count()).isEqualTo(2)
  }

  @Test
  fun `휴일로 지정한 날을 다시 휴일로 지정한다`() {
    // given
    setHolidaysService.setHolidays(listOf(DateTimeMaker.nowDate()), HolidayType.SPECIAL)

    // when & then
    AssertNuguriException(ExceptionType.ALREADY_HOLIDAY).assert {
      setHolidaysService.setHolidays(listOf(DateTimeMaker.nowDate()), HolidayType.SPECIAL)
    }
  }

  @Test
  fun `4월 31일을 휴일로 지정한다`() {
    // given

    // when & then
    assertThrows<DateTimeException> {
      setHolidaysService.setHolidays(listOf(LocalDate.of(2021, 4, 31)), HolidayType.SPECIAL)
    }
  }

  @Test
  fun `이번주 주말을 휴일로 지정한다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.WEEKEND).assert {
      setHolidaysService.setHolidays(getWeekend(), HolidayType.SPECIAL)
    }
  }
}