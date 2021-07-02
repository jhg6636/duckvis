package com.duckvis.nuguri.services

import com.duckvis.core.domain.nuguri.Holiday
import com.duckvis.core.domain.nuguri.HolidayRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.types.nuguri.HolidayType
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.nuguri.domain.admin.service.holiday.ThisMonthInfoService
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
class ThisMonthInfoServiceTest(
  @Autowired private val thisMonthInfoService: ThisMonthInfoService,
  @Autowired private val holidayRepository: HolidayRepository,
) {
  @AfterEach
  @Transactional
  fun clear() {
    holidayRepository.deleteAllInBatch()
  }

  @Transactional
  fun setHoliday(day: LocalDate) {
    holidayRepository.save(Holiday(day, HolidayType.SPECIAL))
  }

  @Test
  fun `4월의 30일 중 10일이 휴일로 지정되어 있을 때, 달정보를 확인한다`() {
    // given
    val year = DateTimeMaker.nowDate().year
    for (day in 1..10) {
      setHoliday(LocalDate.of(year, 4, day))
    }

    // when
    val april = thisMonthInfoService.thisMonthWorkingDays(4, year)

    // then
    assertThat(april).isEqualTo(20)
  }

  @Test
  fun `13월의 달정보를 확인한다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.MONTH_TYPO).assert {
      thisMonthInfoService.thisMonthWorkingDays(13, 2021)
    }
  }
}