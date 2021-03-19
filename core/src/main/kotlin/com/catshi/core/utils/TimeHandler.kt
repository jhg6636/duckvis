package com.catshi.core.utils

import lombok.NoArgsConstructor
import java.time.*

@NoArgsConstructor
object TimeHandler {
    fun nowDateTime(): LocalDateTime {
        return LocalDateTime.now().atOffset(ZoneOffset.UTC).toLocalDateTime()
    }

    fun nowDate(): LocalDate {
        return LocalDateTime.now().atOffset(ZoneOffset.UTC).toLocalDate()
    }

    fun isTodayWeekend(): Boolean {
        return (nowDate().dayOfWeek == DayOfWeek.SATURDAY ||
                nowDate().dayOfWeek == DayOfWeek.SUNDAY)
    }

    fun isTodayHoliday(): Boolean {
        return isTodayWeekend() ||
                (nowDate().month == Month.JANUARY && nowDate().dayOfMonth == 1) ||
                (nowDate().month == Month.MARCH && nowDate().dayOfMonth == 1) ||
                (nowDate().month == Month.MAY && nowDate().dayOfMonth == 5) ||
                (nowDate().month == Month.JUNE && nowDate().dayOfMonth == 6) ||
                (nowDate().month == Month.AUGUST && nowDate().dayOfMonth == 15) ||
                (nowDate().month == Month.OCTOBER && nowDate().dayOfMonth == 3) ||
                (nowDate().month == Month.OCTOBER && nowDate().dayOfMonth == 9) ||
                (nowDate().month == Month.DECEMBER && nowDate().dayOfMonth == 25)
    }
}