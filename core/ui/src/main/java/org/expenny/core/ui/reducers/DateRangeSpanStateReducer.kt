package org.expenny.core.ui.reducers

import kotlinx.coroutines.CoroutineScope
import org.expenny.core.common.extensions.toDateRangeString
import org.expenny.core.common.extensions.toDateString
import org.expenny.core.common.extensions.toMonthString
import org.expenny.core.common.extensions.toQuarterString
import org.expenny.core.common.extensions.toYearString
import org.expenny.core.common.types.DateRangeSpan
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.threeten.extra.LocalDateRange

class DateRangeSpanStateReducer(
    scope: CoroutineScope,
    initialState: State = State(),
) : ContainerStateReducer<DateRangeSpanStateReducer.State>(initialState, scope) {

    init {
        updateDateRangeSpan(initialState.dateRangeSpan)
    }

    fun onNextDateRange() {
        updateOffset(state.offset + 1)
    }

    fun onPreviousDateRange() {
        updateOffset(state.offset - 1)
    }

    fun onDateRangeSpanChange(span: DateRangeSpan) {
        if (span != state.dateRangeSpan) {
            updateDateRangeSpan(span)
        } else {
            updateOffset(0)
        }
    }

    private fun updateDateRangeSpan(dateRangeSpan: DateRangeSpan) {
        intent {
            reduce {
                state.copy(dateRangeSpan = dateRangeSpan, offset = 0)
            }
        }
    }

    private fun updateOffset(offset: Int) {
        intent {
            if (state.dateRangeSpan.atOffset(offset) != null) {
                reduce { state.copy(offset = offset) }
            }
        }
    }

    data class State(
        val dateRangeSpan: DateRangeSpan = DateRangeSpan.Month(),
        val offset: Int = 0,
    ) : ContainerStateReducer.State {

        val dateRange: LocalDateRange
            get() {
                return dateRangeSpan.atOffset(offset)
                    ?: throw IllegalArgumentException("Offset is out of range")
            }

        val dateRangeString: String
            get() = with(dateRange) {
                val defaultString = toDateRangeString()
                when (dateRangeSpan) {
                    is DateRangeSpan.Day -> start.toDateString()
                    is DateRangeSpan.Week -> defaultString
                    is DateRangeSpan.Month -> toMonthString() ?: defaultString
                    is DateRangeSpan.Quarter -> toQuarterString() ?: defaultString
                    is DateRangeSpan.Year -> toYearString() ?: defaultString
                }
            }
    }
}