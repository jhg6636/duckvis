package com.duckvis.core.utils

import lombok.NoArgsConstructor
import java.time.*

@NoArgsConstructor
object TimeHandler {
    val dayStartAt = 21
    fun nowDateTime(): LocalDateTime {
        return LocalDateTime.now().atOffset(ZoneOffset.UTC).toLocalDateTime()
    }

    fun nowDate(): LocalDate {
        return LocalDateTime.now().atOffset(ZoneOffset.UTC).toLocalDate()
    }

    fun dayStartTime(now: LocalDateTime = nowDateTime()): LocalDateTime {
        return if (now.hour >= dayStartAt) {
            now.withHour(dayStartAt).withMinute(0).withSecond(0)
        } else {
            now.minusDays(1).withHour(dayStartAt).withMinute(0).withSecond(0)
        }
    }

    fun dayEndTime(now: LocalDateTime = nowDateTime()): LocalDateTime {
        return if (now.hour >= dayStartAt) {
            now.plusDays(1).withHour(dayStartAt).withMinute(0).withSecond(0)
        } else {
            now.withHour(dayStartAt).withMinute(0).withSecond(0)
        }
    }

    fun weekStartTime(now: LocalDateTime = nowDateTime()): LocalDateTime {
        return if (now.hour >= dayStartAt && now.dayOfWeek == DayOfWeek.SUNDAY) {
            now.withHour(dayStartAt).withMinute(0).withSecond(0)
        } else {
            now.minusDays(now.dayOfWeek.value.toLong()).withHour(dayStartAt).withMinute(0).withSecond(0)
        }
    }

    fun weekEndTime(now: LocalDateTime = nowDateTime()): LocalDateTime {
        return if (now.hour >= dayStartAt && now.dayOfWeek == DayOfWeek.SUNDAY) {
            now.plusDays(7).withHour(dayStartAt).withMinute(0).withSecond(0)
        } else {
            now.plusDays(7 - now.dayOfWeek.value.toLong()).withHour(21).withMinute(0).withSecond(0)
        }
    }

    fun monthStartTime(now: LocalDateTime = nowDateTime()): LocalDateTime {
        return if (now.hour >= dayStartAt && now.dayOfMonth == now.month.length(now.toLocalDate().isLeapYear)) {
            now.withHour(dayStartAt).withMinute(0).withSecond(0)
        } else {
            now.minusDays(now.dayOfMonth.toLong()).withHour(dayStartAt).withMinute(0).withSecond(0)
        }
    }

    fun monthEndTime(now: LocalDateTime = nowDateTime()): LocalDateTime {
        return if (now.hour >= dayStartAt && now.month == Month.DECEMBER && now.dayOfMonth == 31) {
            now.withYear(now.year + 1).withMonth(1).withDayOfMonth(31).withHour(dayStartAt).withMinute(0).withSecond(0)
        } else {
            now.plusDays(now.month.length(now.toLocalDate().isLeapYear) - now.dayOfMonth.toLong()).withHour(dayStartAt)
                .withMinute(0).withSecond(0)
        }
    }
}