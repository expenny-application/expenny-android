package org.expenny.core.common.extensions

import org.expenny.core.common.utils.Constants
import org.threeten.extra.LocalDateRange
import org.threeten.extra.YearQuarter
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.IsoFields
import java.time.temporal.TemporalAdjusters

fun LocalDate.toDateString(): String = format(DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT))

fun LocalTime.toTimeString(): String = format(DateTimeFormatter.ofPattern(Constants.DEFAULT_TIME_FORMAT))

fun LocalDateTime.toDateString(): String = format(DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT))

fun LocalDateTime.toTimeString(): String = format(DateTimeFormatter.ofPattern(Constants.DEFAULT_TIME_FORMAT))

fun LocalDateTime.toEpochSecondUtc(): Long = toEpochSecond(ZoneOffset.UTC)

fun LocalDate.toQuarterRange(): LocalDateRange {
    return LocalDateRange.ofClosed(
        YearQuarter.of(year, get(IsoFields.QUARTER_OF_YEAR)).atDay(1),
        YearQuarter.of(year, get(IsoFields.QUARTER_OF_YEAR)).atEndOfQuarter(),
    )
}

fun LocalDate.toYearRange(): LocalDateRange {
    return LocalDateRange.ofClosed(
        with(TemporalAdjusters.firstDayOfYear()), with(TemporalAdjusters.lastDayOfYear())
    )
}

fun LocalDate.toMonthRange(): LocalDateRange {
    return LocalDateRange.ofClosed(
        with(TemporalAdjusters.firstDayOfMonth()), with(TemporalAdjusters.lastDayOfMonth())
    )
}

fun LocalDate.toWeekRange(): LocalDateRange {
    return LocalDateRange.ofClosed(
        with(DayOfWeek.MONDAY), with(DayOfWeek.SUNDAY)
    )
}