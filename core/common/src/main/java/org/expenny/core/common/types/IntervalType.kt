package org.expenny.core.common.types

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters.firstDayOfYear
import java.time.temporal.TemporalAdjusters.lastDayOfMonth
import java.time.temporal.TemporalAdjusters.lastDayOfYear

enum class IntervalType {
    Day,
    Week {
        override val boundsStartDate: LocalDate = bounds.start.with(DayOfWeek.MONDAY)
    },
    Month,
    Quarter,
    Year;

    internal open val boundsStartDate: LocalDate by lazy { bounds.start }
    internal open val boundsEndDate: LocalDate by lazy { bounds.endInclusive }

    val bounds: ClosedRange<LocalDate> by lazy {
        val startDate = LocalDate.now().minusYears(5).with(firstDayOfYear())
        val endDate = LocalDate.now().plusYears(5).with(lastDayOfYear())
        startDate.rangeTo(endDate)
    }

    val dateRangeAtZeroOffset: ClosedRange<LocalDate> by lazy {
        dateRanges.first { it.contains(LocalDate.now()) }
    }

    private val dateRanges: List<ClosedRange<LocalDate>> by lazy {
        var currentDate = boundsStartDate
        buildList {
            while (currentDate <= boundsEndDate) {
                val rangeStartDate = currentDate
                val rangeEndDate = when (this@IntervalType) {
                    Day -> currentDate
                    Week -> currentDate.with(DayOfWeek.SUNDAY)
                    Month -> currentDate.with(lastDayOfMonth())
                    Quarter -> currentDate.plusMonths(2).with(lastDayOfMonth())
                    Year -> currentDate.with(lastDayOfYear())
                }
                val range = rangeStartDate.rangeTo(rangeEndDate)

                add(range)
                currentDate = rangeEndDate.plusDays(1)
            }
        }
    }

    fun atOffset(offset: Int): ClosedRange<LocalDate> {
        val index = dateRanges.indexOf(dateRangeAtZeroOffset) + offset

        return if (offset == 0 || index < 0) {
            dateRangeAtZeroOffset
        } else if (index > dateRanges.lastIndex) {
            dateRanges.last()
        } else {
            dateRanges[index]
        }
    }
}
