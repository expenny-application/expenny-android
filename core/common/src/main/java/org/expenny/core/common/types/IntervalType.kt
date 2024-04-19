package org.expenny.core.common.types

import org.threeten.extra.LocalDateRange
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Period
import java.time.temporal.TemporalAdjusters.firstDayOfYear
import java.time.temporal.TemporalAdjusters.lastDayOfYear

enum class IntervalType {
    Day {
        override val period = Period.ofDays(1)
    },
    Week {
        override val period = Period.ofWeeks(1)
        override val boundsStartDate: LocalDate = bounds.start.with(DayOfWeek.MONDAY)
    },
    Month {
        override val period = Period.ofMonths(1)
    },
    Quarter {
        override val period: Period = Period.ofMonths(3)
    },
    Year {
        override val period: Period = Period.ofYears(1)
    };

    internal abstract val period: Period
    internal open val boundsStartDate: LocalDate by lazy { bounds.start }
    internal open val boundsEndDate: LocalDate by lazy { bounds.endInclusive }

    internal val bounds: LocalDateRange by lazy {
        LocalDateRange.ofClosed(
            LocalDate.now().minusYears(10).with(firstDayOfYear()),
            LocalDate.now().plusYears(10).with(lastDayOfYear()),
        )
    }

    val dateRangeAtZeroOffset: LocalDateRange by lazy {
        dateRanges.first { it.contains(LocalDate.now()) }
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
        if (offset == 0) return dateRangeAtZeroOffset
        return dateRanges.getOrNull(dateRanges.indexOf(dateRangeAtZeroOffset) + offset)
    }
}
