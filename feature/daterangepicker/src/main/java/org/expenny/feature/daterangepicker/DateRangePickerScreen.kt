package org.expenny.feature.daterangepicker

import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import org.expenny.core.ui.foundation.transitions.VerticalSlideTransitionStyle
import org.expenny.feature.daterangepicker.navigation.DateRangePickerNavArgs
import org.expenny.feature.daterangepicker.view.DateRangePickerContent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import org.threeten.extra.LocalDateRange
import java.time.Instant
import java.time.LocalDate
import java.time.Year

@OptIn(ExperimentalMaterial3Api::class)
@Destination(
    navArgsDelegate = DateRangePickerNavArgs::class,
    style = VerticalSlideTransitionStyle::class
)
@Composable
fun DateRangePickerScreen(
    resultNavigator: ResultBackNavigator<LocalDateRange>
) {
    val vm: DateRangePickerViewModel = hiltViewModel()
    val state by vm.collectAsState()

    val pickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = state.initialSelectedStartDateEpoch,
        initialSelectedEndDateMillis = state.initialSelectedEndDateEpoch,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return Instant.ofEpochMilli(utcTimeMillis).isBefore(Instant.now())
            }

            override fun isSelectableYear(year: Int): Boolean {
                return year >= Year.now().value
            }
        }
    )

    vm.collectSideEffect {
        when (it) {
            is Event.ClearDateRangeSelection -> {
                pickerState.setSelection(null, null)
            }
            is Event.NavigateBackWithResult -> {
                resultNavigator.navigateBack(it.range)
            }
            is Event.NavigateBack -> {
                resultNavigator.navigateBack()
            }
        }
    }

    LaunchedEffect(pickerState.selectedStartDateMillis) {
        snapshotFlow { pickerState.selectedStartDateMillis }.collect {
            vm.onAction(Action.OnStartDateSelect(it))
        }
    }

    LaunchedEffect(pickerState.selectedEndDateMillis) {
        snapshotFlow { pickerState.selectedEndDateMillis }.collect {
            vm.onAction(Action.OnEndDateSelect(it))
        }
    }

    DateRangePickerContent(
        state = state,
        pickerState = pickerState,
        onAction = vm::onAction
    )
}