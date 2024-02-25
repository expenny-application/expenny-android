package org.expenny.core.common.extensions

import org.expenny.core.common.utils.Constants
import org.threeten.extra.LocalDateRange
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

fun LocalDateRange.toDateRangeString(): String {
    return "${start.toDateString()} - ${endInclusive.toDateString()}"
}

fun LocalDateRange.toMonthString(): String? {
    return if (this == start.toMonthRange()) {
        start.format(DateTimeFormatter.ofPattern(Constants.DEFAULT_MONTH_FORMAT))
            .replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
    } else null
}

fun LocalDateRange.toQuarterString(): String? {
    return if (this == start.toQuarterRange()) {
        start.format(DateTimeFormatter.ofPattern(Constants.DEFAULT_QUARTER_FORMAT))
    } else null
}

fun LocalDateRange.toYearString(): String? {
    return if (this == start.toYearRange()) {
        start.format(DateTimeFormatter.ofPattern(Constants.DEFAULT_YEAR_FORMAT))
    } else null
}

fun LocalDateRange.toDatesList(): List<LocalDate> {
    // stream().collect(Collectors.toList())
    val daysCount = start.until(end, ChronoUnit.DAYS).toInt()
    return generateSequence(start) { it.plusDays(1) }
        .take(daysCount)
        .toList()
}
