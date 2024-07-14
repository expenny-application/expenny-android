package org.expenny.core.common.extensions

import org.expenny.core.common.utils.Constants
import org.threeten.extra.LocalDateRange
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import java.util.Locale

fun ClosedRange<LocalDate>.toDateTimeRange(): ClosedRange<LocalDateTime> {
    return start.atStartOfDay().rangeTo(endInclusive.atEndOfDay())
}

fun ClosedRange<LocalDate>.toList(): List<LocalDate> {
    val daysCount = start.until(endInclusive, ChronoUnit.DAYS).toInt()
    return generateSequence(start) { it.plusDays(1) }
        .take(daysCount)
        .toList()
}

fun ClosedRange<LocalDate>.toDateRangeString(): String {
    val days = ChronoUnit.DAYS.between(start, endInclusive) + 1

    val isDayRange: () -> Boolean = {
        start == endInclusive
    }

    val isMonthRange: () -> Boolean = {
        start.year == endInclusive.year && start.month == endInclusive.month && days == start.lengthOfMonth().toLong()
    }

    val isQuarterRange: () -> Boolean = {
        val startMonth = start.monthValue
        val endMonth = endInclusive.monthValue
        val isQuarter = (startMonth == 1 && endMonth == 3) ||
                (startMonth == 4 && endMonth == 6) ||
                (startMonth == 7 && endMonth == 9) ||
                (startMonth == 10 && endMonth == 12)
        start.year == endInclusive.year && isQuarter
    }

    val isYearRange: () -> Boolean = {
        start.year == endInclusive.year && days == 365L && start.dayOfYear == 1 && endInclusive.dayOfYear == 365
    }

    val isLeapYearRange: () -> Boolean = {
        start.year == endInclusive.year && days == 366L && start.dayOfYear == 1 && endInclusive.dayOfYear == 366 && start.isLeapYear
    }

    return when {
        isDayRange() ->
            start.format(DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT))
        isMonthRange() ->
            start.format(DateTimeFormatter.ofPattern(Constants.DEFAULT_MONTH_FORMAT)).replaceFirstChar { it.titlecase(Locale.getDefault()) }
        isQuarterRange() ->
            start.format(DateTimeFormatter.ofPattern(Constants.DEFAULT_QUARTER_FORMAT))
        isYearRange() || isLeapYearRange() ->
            start.format(DateTimeFormatter.ofPattern(Constants.DEFAULT_YEAR_FORMAT))
        else ->
            "${start.toDateString()} - ${endInclusive.toDateString()}"
    }
}

fun String.parseToClosedDateRange(): ClosedRange<LocalDate> {
    val dates = split("..")
    require(dates.size == 2) { "Invalid range format: $this" }

    val startDate = LocalDate.parse(dates[0].trim())
    val endDate = LocalDate.parse(dates[1].trim())

    require(!startDate.isAfter(endDate)) { "Start date must not be after end date" }

    return startDate.rangeTo(endDate)
}