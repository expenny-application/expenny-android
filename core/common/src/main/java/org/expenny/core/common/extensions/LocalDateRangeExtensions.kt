package org.expenny.core.common.extensions

import org.expenny.core.common.utils.Constants
import org.threeten.extra.LocalDateRange
import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

fun LocalDateRange.toDateRangeString(): String {
    val default = "${start.toDateString()} - ${endInclusive.toDateString()}"
    return try {
        when(this) {
            start.toDayRange() ->
                start.format(DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT))
            start.toMonthRange() ->
                start.format(DateTimeFormatter.ofPattern(Constants.DEFAULT_MONTH_FORMAT))
                    .replaceFirstChar { it.titlecase(Locale.getDefault()) }
            start.toQuarterRange() ->
                start.format(DateTimeFormatter.ofPattern(Constants.DEFAULT_QUARTER_FORMAT))
            start.toYearRange() ->
                start.format(DateTimeFormatter.ofPattern(Constants.DEFAULT_YEAR_FORMAT))
            else -> default
        }
    } catch (e: DateTimeException) {
        default
    }
}

fun LocalDateRange.toDatesList(): List<LocalDate> {
    val daysCount = start.until(end, ChronoUnit.DAYS).toInt()
    return generateSequence(start) { it.plusDays(1) }
        .take(daysCount)
        .toList()
}

fun LocalDateRange.toClosedDateTimeRange(): ClosedRange<LocalDateTime> {
    return start.atStartOfDay().rangeTo(endInclusive.atEndOfDay())
}
