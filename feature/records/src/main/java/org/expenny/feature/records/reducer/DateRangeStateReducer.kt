package org.expenny.feature.records.reducer

import kotlinx.coroutines.CoroutineScope
import org.expenny.core.common.extensions.*
import org.expenny.core.common.types.DateRecurrence
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.threeten.extra.LocalDateRange
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Period

class DateRangeStateReducer(
    scope: CoroutineScope,
    initialState: State = State(),
) : ContainerStateReducer<DateRangeStateReducer.State>(initialState, scope) {

    private val dateRange get() = stateFlow.value.dateRange
    private val dateRecurrence get() = stateFlow.value.dateRecurrence
    private var currentRanges: List<LocalDateRange> = emptyList()
    private val bounds: LocalDateRange = LocalDateRange.ofClosed(
        LocalDate.of(1970, 1, 1),
        LocalDate.of(2099, 12, 31)
    )

    init {
        updateDateRecurrence(initialState.dateRecurrence)
    }

    fun onNextDateRange() {
        currentRanges.getOrNull(currentRanges.indexOf(dateRange) + 1)?.also {
            updateDateRange(it)
        }
    }

    fun onPreviousDateRange() {
        currentRanges.getOrNull(currentRanges.indexOf(dateRange) - 1)?.also {
            updateDateRange(it)
        }
    }

    fun onDateRecurrenceChange(newDateRecurrence: DateRecurrence) {
        if (newDateRecurrence != dateRecurrence) {
            updateDateRecurrence(newDateRecurrence)
        } else {
            updateDateRange(newDateRecurrence.currentDateRange())
        }
    }

    private fun updateDateRecurrence(dateRecurrence: DateRecurrence) {
        val period = when (dateRecurrence) {
            DateRecurrence.Daily -> Period.ofDays(1)
            DateRecurrence.Weekly -> Period.ofWeeks(1)
            DateRecurrence.Monthly -> Period.ofMonths(1)
            DateRecurrence.Quarterly -> Period.ofMonths(3)
            DateRecurrence.Annually -> Period.ofYears(1)
        }

        var date = if (dateRecurrence == DateRecurrence.Weekly) bounds.start.with(DayOfWeek.MONDAY) else bounds.start
        val ranges = mutableListOf<LocalDateRange>()

        while (date <= bounds.endInclusive) {
            val range = LocalDateRange.ofClosed(date, (date + period).minusDays(1))
            ranges.add(range)
            date = range.end
        }

        this.currentRanges = ranges

        val currentRange = dateRecurrence.currentDateRange()
        val currentRangeString = currentRange.asString(dateRecurrence)

        intent {
            reduce {
                state.copy(
                    dateRecurrence = dateRecurrence,
                    dateRange = currentRange,
                    dateRangeString = currentRangeString
                )
            }
        }
    }

    private fun updateDateRange(dateRange: LocalDateRange) {
        intent {
            reduce {
                state.copy(
                    dateRange = dateRange,
                    dateRangeString = dateRange.asString(state.dateRecurrence)
                )
            }
        }
    }

    private fun LocalDateRange.asString(dateRecurrence: DateRecurrence): String {
        val defaultString = toDateRangeString()
        return when (dateRecurrence) {
            DateRecurrence.Daily -> start.toDateString()
            DateRecurrence.Weekly -> defaultString
            DateRecurrence.Monthly -> toMonthString() ?: defaultString
            DateRecurrence.Quarterly -> toQuarterString() ?: defaultString
            DateRecurrence.Annually -> toYearString() ?: defaultString
        }
    }

    data class State(
        val dateRecurrence: DateRecurrence = DateRecurrence.Monthly,
        val dateRange: LocalDateRange = LocalDateRange.ofUnbounded(),
        val dateRangeString: String = "",
    ) : ContainerStateReducer.State
}