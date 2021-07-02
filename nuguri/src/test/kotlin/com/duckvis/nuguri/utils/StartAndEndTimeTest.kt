package com.duckvis.nuguri.utils

import com.duckvis.core.utils.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class StartAndEndTimeTest {

  @Test
  fun `12월 31일 22시 58분에 이 달의 마지막 날을 확인한다`() {
    // given
    val lastDay = LocalDateTime.now().withMonth(12).withDayOfMonth(31).withHour(22).withMinute(58)

    // when
    val lastDayOfMonth = lastDay.monthEndTime

    // then
    assertThat(lastDayOfMonth.monthValue).isEqualTo(1)
    assertThat(lastDayOfMonth.dayOfMonth).isEqualTo(31)
    assertThat(lastDayOfMonth.hour).isEqualTo(21)
    assertThat(lastDayOfMonth.minute).isEqualTo(0)
    assertThat(lastDayOfMonth.second).isEqualTo(0)
    assertThat(lastDayOfMonth.year).isEqualTo(lastDay.year + 1)
  }

  @Test
  fun `12월 31일 20시 58분에 이 달의 마지막 날을 확인한다`() {
    val lastDay = LocalDateTime.now().withMonth(12).withDayOfMonth(31).withHour(20).withMinute(58)

    // when
    val lastDayOfMonth = lastDay.monthEndTime

    // then
    assertThat(lastDayOfMonth.monthValue).isEqualTo(12)
    assertThat(lastDayOfMonth.dayOfMonth).isEqualTo(31)
    assertThat(lastDayOfMonth.hour).isEqualTo(21)
    assertThat(lastDayOfMonth.minute).isEqualTo(0)
    assertThat(lastDayOfMonth.second).isEqualTo(0)
    assertThat(lastDayOfMonth.year).isEqualTo(lastDay.year)
  }

  @Test
  fun `12월 31일 20시 58분에 이 달의 첫 날을 확인한다`() {
    val lastDay = LocalDateTime.now().withMonth(12).withDayOfMonth(31).withHour(20).withMinute(58)

    // when
    val lastDayOfMonth = lastDay.monthStartTime

    // then
    assertThat(lastDayOfMonth.monthValue).isEqualTo(11)
    assertThat(lastDayOfMonth.dayOfMonth).isEqualTo(30)
    assertThat(lastDayOfMonth.hour).isEqualTo(21)
    assertThat(lastDayOfMonth.minute).isEqualTo(0)
    assertThat(lastDayOfMonth.second).isEqualTo(0)
    assertThat(lastDayOfMonth.year).isEqualTo(lastDay.year)
  }


  @Test
  fun `1월 2일에 이 주의 첫 날을 확인한다`() {
    // given
    val day = LocalDateTime.of(2021, 1, 2, 11, 11, 11)

    // when
    val firstDayOfWeek = day.weekStartTime

    // then
    assertThat(firstDayOfWeek.monthValue).isEqualTo(12)
    assertThat(firstDayOfWeek.dayOfMonth).isEqualTo(27)
    assertThat(firstDayOfWeek.hour).isEqualTo(21)
    assertThat(firstDayOfWeek.minute).isEqualTo(0)
    assertThat(firstDayOfWeek.second).isEqualTo(0)
    assertThat(firstDayOfWeek.year).isEqualTo(2020)
  }

  @Test
  fun `일요일 21시 이후에 이 주의 첫 날을 확인한다`() {
    // given
    val day = LocalDateTime.of(2021, 3, 21, 21, 58, 34)

    // when
    val firstDayOfWeek = day.weekStartTime

    // then
    assertThat(firstDayOfWeek.monthValue).isEqualTo(3)
    assertThat(firstDayOfWeek.dayOfMonth).isEqualTo(21)
    assertThat(firstDayOfWeek.hour).isEqualTo(21)
    assertThat(firstDayOfWeek.minute).isEqualTo(0)
    assertThat(firstDayOfWeek.second).isEqualTo(0)
    assertThat(firstDayOfWeek.year).isEqualTo(2021)
  }

  @Test
  fun `일요일 21시 이후에 이 주의 마지막 날을 확인한다`() {
    // given
    val day = LocalDateTime.of(2021, 3, 21, 21, 58, 34)

    // when
    val lastDayOfWeek = day.weekEndTime

    // then
    assertThat(lastDayOfWeek.monthValue).isEqualTo(3)
    assertThat(lastDayOfWeek.dayOfMonth).isEqualTo(28)
    assertThat(lastDayOfWeek.hour).isEqualTo(21)
    assertThat(lastDayOfWeek.minute).isEqualTo(0)
    assertThat(lastDayOfWeek.second).isEqualTo(0)
    assertThat(lastDayOfWeek.year).isEqualTo(2021)
  }

  @Test
  fun `오늘 23시 59분에 오늘의 시작 시간을 확인한다`() {
    // given
    val now = DateTimeMaker.nowDateTime().withHour(23).withMinute(59)

    // when
    val startTime = now.dayStartTime

    // then
    assertThat(startTime.monthValue).isEqualTo(now.monthValue)
    assertThat(startTime.dayOfMonth).isEqualTo(now.dayOfMonth)
    assertThat(startTime.hour).isEqualTo(21)
    assertThat(startTime.minute).isEqualTo(0)
    assertThat(startTime.second).isEqualTo(0)
    assertThat(startTime.year).isEqualTo(now.year)
  }

  @Test
  fun `1월 1일 9시 00분에 오늘의 시작 시간을 확인한다`() {
    // given
    val day = LocalDateTime.of(2021, 1, 1, 9, 0, 0)

    // when
    val startTime = day.dayStartTime

    // then
    assertThat(startTime.monthValue).isEqualTo(12)
    assertThat(startTime.dayOfMonth).isEqualTo(31)
    assertThat(startTime.hour).isEqualTo(21)
    assertThat(startTime.minute).isEqualTo(0)
    assertThat(startTime.second).isEqualTo(0)
    assertThat(startTime.year).isEqualTo(2020)
  }

  @Test
  fun `오늘 20시 33분에 오늘의 끝 시간을 확인한다`() {
    // given
    val now = DateTimeMaker.nowDateTime().withHour(20).withMinute(33)

    // when
    val endTime = now.dayEndTime

    // then
    assertThat(endTime.monthValue).isEqualTo(now.monthValue)
    assertThat(endTime.dayOfMonth).isEqualTo(now.dayOfMonth)
    assertThat(endTime.hour).isEqualTo(21)
    assertThat(endTime.minute).isEqualTo(0)
    assertThat(endTime.second).isEqualTo(0)
    assertThat(endTime.year).isEqualTo(now.year)
  }

  @Test
  fun `12월 31일 23시 43분에 오늘의 끝 시간을 확인한다`() {
    // given
    val now = LocalDateTime.of(2021, 12, 31, 23, 43, 0)

    // when
    val endTime = now.dayEndTime

    // then
    assertThat(endTime.monthValue).isEqualTo(1)
    assertThat(endTime.dayOfMonth).isEqualTo(1)
    assertThat(endTime.hour).isEqualTo(21)
    assertThat(endTime.minute).isEqualTo(0)
    assertThat(endTime.second).isEqualTo(0)
    assertThat(endTime.year).isEqualTo(2022)
  }
}