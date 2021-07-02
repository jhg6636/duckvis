package com.duckvis.nuguri.services

import com.duckvis.core.domain.nuguri.Holiday
import com.duckvis.core.domain.nuguri.HolidayRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.types.nuguri.HolidayType
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.nuguri.domain.admin.service.holiday.SetWorkDaysService
import com.duckvis.nuguri.utils.AssertNuguriException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
@ActiveProfiles("test")
class SetWorkDaysServiceTest(
  @Autowired private val setWorkDaysService: SetWorkDaysService,
  @Autowired private val holidayRepository: HolidayRepository,
) {
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

  @Transactional
  fun saveHolidays(dates: List<LocalDate>) {
    dates.forEach { holidayRepository.save(Holiday(it, HolidayType.SPECIAL)) }
  }

  @Test
  fun `등록된 평일인 휴일들을 일하는 날로 바꾼다`() {
    // given
    saveHolidays(getThisMondayAndTuesDay())

    // when
    setWorkDaysService.setWorkDay(getThisMondayAndTuesDay())

    // then
    assertThat(holidayRepository.count()).isEqualTo(0)
  }

  @Test
  fun `등록된 주말인 휴일을 일하는 날로 바꾼다`() {
    // given
    saveHolidays(getWeekend())

    // when & then
    AssertNuguriException(ExceptionType.WEEKEND).assert {
      setWorkDaysService.setWorkDay(getWeekend().subList(0, 1))
    }
  }

  @Test
  fun `법정공휴일을 일하는 날로 바꾼다`() {
    // given
    saveHolidays(listOf(LocalDate.of(2020, 12, 25))) // 금요일이었음

    // when & then
    AssertNuguriException(ExceptionType.NATIONAL_HOLIDAY).assert {
      setWorkDaysService.setWorkDay(listOf(LocalDate.of(2020, 12, 25)))
    }
  }

  @Test
  fun `휴일로 등록되지 않은 날을 일하는 날로 바꾼다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.NO_SUCH_HOLIDAY).assert {
      setWorkDaysService.setWorkDay(listOf(DateTimeMaker.nowDate()))
    }
  }
}