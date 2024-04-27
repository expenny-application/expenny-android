package org.expenny.feature.daterangepicker

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.expenny.core.common.extensions.toDateString
import org.expenny.core.ui.base.ExpennyViewModel
import org.expenny.feature.daterangepicker.navigation.DateRangePickerNavArgs
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import org.threeten.extra.LocalDateRange
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import javax.inject.Inject

@HiltViewModel
class DateRangePickerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ExpennyViewModel<Action>(), ContainerHost<State, Event> {

    private val selectedStartDateMillsState = MutableStateFlow<Long?>(null)
    private val selectedEndDateMillsState = MutableStateFlow<Long?>(null)

    override val container = container<State, Event>(
        initialState = State(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            setNavArgs()
            launch { subscribeToDateRange() }
        }
    }

    override fun onAction(action: Action) {
        when (action) {
            is Action.OnStartDateSelect -> handleOnStartDateSelect(action)
            is Action.OnEndDateSelect -> handleOnEndDateSelect(action)
            is Action.OnClearClick -> handleOnClearClick()
            is Action.OnApplyClick -> handleOnApplyClick()
            is Action.OnCloseClick -> handleOnCloseClick()
        }
    }

    private fun handleOnStartDateSelect(action: Action.OnStartDateSelect) {
        selectedStartDateMillsState.value = action.ms
    }

    private fun handleOnEndDateSelect(action: Action.OnEndDateSelect) {
        selectedEndDateMillsState.value = action.ms
    }

    private fun handleOnClearClick() = intent {
        postSideEffect(Event.ClearDateRangeSelection)
    }

    private fun handleOnCloseClick() = intent {
        postSideEffect(Event.NavigateBack)
    }

    private fun handleOnApplyClick() = intent {
        val startDate = selectedStartDateMillsState.value!!.toLocalDateUtc()
        val endDate = selectedEndDateMillsState.value!!.toLocalDateUtc()
        val range = LocalDateRange.of(startDate, endDate)

        postSideEffect(Event.NavigateBackWithResult(range))
    }

    private fun setNavArgs() {
        savedStateHandle.navArgs<DateRangePickerNavArgs>().also { args ->
            val startDateMills = args.selectedDateRange?.start?.toEpochMsUtc()
            val endDateMills = args.selectedDateRange?.end?.toEpochMsUtc()

            intent {
                reduce {
                    state.copy(
                        initialSelectedStartDateEpoch = startDateMills,
                        initialSelectedEndDateEpoch = endDateMills
                    )
                }
            }
        }
    }

    private fun subscribeToDateRange() = intent {
        repeatOnSubscription {
            combine(
                selectedStartDateMillsState,
                selectedEndDateMillsState
            ) { startDateMills, endDateMills ->
                val startDate = startDateMills?.toLocalDateUtc()?.toDateString()
                val endDate = endDateMills?.toLocalDateUtc()?.toDateString()
                val range = Pair(startDate, endDate)
                val disableApplyButton = range.first == null || range.second == null
                val showClearButton = range.first != null || range.second != null

                reduce {
                    state.copy(
                        selectedRange = range,
                        enableApplyButton = !disableApplyButton,
                        showClearButton = showClearButton
                    )
                }
            }.collect()
        }
    }

    private fun Long.toLocalDateUtc(): LocalDate {
        return Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()
    }

    private fun LocalDate.toEpochMsUtc(): Long {
        return atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
    }
}