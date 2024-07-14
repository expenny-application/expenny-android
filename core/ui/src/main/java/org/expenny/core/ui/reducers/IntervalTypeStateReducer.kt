package org.expenny.core.ui.reducers

import kotlinx.coroutines.CoroutineScope
import org.expenny.core.common.types.IntervalType
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import java.time.LocalDate

class IntervalTypeStateReducer(
    scope: CoroutineScope,
    initialState: State = State(),
) : ContainerStateReducer<IntervalTypeStateReducer.State>(initialState, scope) {

    init {
        updateIntervalType(initialState.intervalType)
    }

    fun getNextInterval() = state.intervalType.atOffset(1)

    fun getPreviousInterval() = state.intervalType.atOffset(-1)

    fun onNextInterval() {
        updateOffset(state.offset + 1)
    }

    fun onPreviousInterval() {
        updateOffset(state.offset - 1)
    }

    fun onIntervalTypeChange(span: IntervalType) {
        if (span != state.intervalType) {
            updateIntervalType(span)
        } else {
            updateOffset(0)
        }
    }

    private fun updateIntervalType(intervalType: IntervalType) {
        intent {
            reduce {
                state.copy(intervalType = intervalType, offset = 0)
            }
        }
    }

    private fun updateOffset(offset: Int) {
        intent {
            reduce { state.copy(offset = offset) }
        }
    }

    data class State(
        val intervalType: IntervalType = IntervalType.Month,
        val offset: Int = 0,
    ) : ContainerStateReducer.State {

        val dateRange: ClosedRange<LocalDate>
            get() = intervalType.atOffset(offset)
    }
}