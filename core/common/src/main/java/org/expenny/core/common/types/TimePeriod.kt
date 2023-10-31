package org.expenny.core.common.types

import org.threeten.extra.LocalDateRange
import org.threeten.extra.YearQuarter
import org.threeten.extra.YearWeek
import java.time.*
import java.time.temporal.IsoFields

enum class TimePeriod {
    Today,
    ThisWeek,
    ThisMonth,
    ThisQuarter,
    ThisYear;

    fun toLocalDateRange(): LocalDateRange {
        val today = LocalDate.now()

        return when (this) {
            Today -> {
                LocalDateRange.of(today, today.plusDays(1))
            }
            ThisWeek -> YearWeek.of(today.year, today.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)).let {
                LocalDateRange.of(it.atDay(DayOfWeek.MONDAY), it.plusWeeks(1).atDay(DayOfWeek.MONDAY))
            }
            ThisMonth -> YearMonth.of(today.year, today.month).let {
                LocalDateRange.of(it.atDay(1), it.atEndOfMonth())
            }
            ThisQuarter -> YearQuarter.of(today.year, today.get(IsoFields.QUARTER_OF_YEAR)).let {
                LocalDateRange.of(it.atDay(1), it.atEndOfQuarter())
            }
            ThisYear -> Year.of(today.year).let {
                LocalDateRange.of(it.atDay(1), it.atMonth(Month.DECEMBER).atEndOfMonth())
            }
        }
    }
}