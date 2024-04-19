package org.expenny.core.common.types

import org.expenny.core.common.extensions.toMonthRange
import org.expenny.core.common.extensions.toQuarterRange
import org.expenny.core.common.extensions.toWeekRange
import org.expenny.core.common.extensions.toYearRange
import org.threeten.extra.LocalDateRange
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Period

sealed class DateRangeSpan(val initialDateRange: LocalDateRange) {

    internal abstract val period: Period
    internal open val boundsStartDate: LocalDate by lazy { bounds.start }
    internal open val boundsEndDate: LocalDate by lazy { bounds.endInclusive }
    internal val bounds: LocalDateRange by lazy {
        LocalDateRange.ofClosed(
            LocalDate.of(1970, 1, 1),
            LocalDate.of(2099, 12, 31)
        )
    }

    private val dateRanges: List<LocalDateRange> by lazy {
        var currentDate = boundsStartDate
        buildList {
            while (currentDate <= boundsEndDate) {
                val rangeStartDate = currentDate
                val rangeEndDate = (currentDate + period).minusDays(1)
                val range = LocalDateRange.ofClosed(rangeStartDate, rangeEndDate)
                add(range)
                currentDate = range.end
            }
        }
    }

    fun atOffset(offset: Int): LocalDateRange? {
        if (offset == 0) return initialDateRange
        return dateRanges.getOrNull(dateRanges.indexOf(initialDateRange) + offset)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DateRangeSpan) return false

        if (initialDateRange != other.initialDateRange) return false
        if (period != other.period) return false

        return true
    }

    override fun hashCode(): Int {
        var result = initialDateRange.hashCode()
        result = 31 * result + period.hashCode()
        return result
    }


    class Day(date: LocalDate = LocalDate.now()) : DateRangeSpan(LocalDateRange.ofClosed(date, date)) {
        override val period: Period = Period.ofDays(1)
    }

    class Week(date: LocalDate = LocalDate.now()) : DateRangeSpan(date.toWeekRange()) {
        override val boundsStartDate: LocalDate = bounds.start.with(DayOfWeek.MONDAY)
        override val period: Period = Period.ofWeeks(1)
    }

    class Month(date: LocalDate = LocalDate.now()) : DateRangeSpan(date.toMonthRange()) {
        override val period: Period = Period.ofMonths(1)
    }

    class Quarter(date: LocalDate = LocalDate.now()) : DateRangeSpan(date.toQuarterRange()) {
        override val period: Period = Period.ofMonths(3)
    }

    class Year(date: LocalDate = LocalDate.now()) : DateRangeSpan(date.toYearRange()) {
        override val period: Period = Period.ofYears(1)
    }

    companion object {
        val spans = listOf(Day(), Week(), Month(), Quarter(), Year())
    }
}