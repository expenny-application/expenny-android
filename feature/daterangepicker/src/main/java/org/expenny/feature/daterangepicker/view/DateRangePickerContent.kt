package org.expenny.feature.daterangepicker.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.expenny.feature.daterangepicker.Action
import org.expenny.feature.daterangepicker.State

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DateRangePickerContent(
    state: State,
    pickerState: DateRangePickerState,
    onAction: (Action) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DateRangePickerToolbar(
                showClearButton = state.showClearButton,
                onClearClick = { onAction(Action.OnClearClick) },
                onCloseClick = { onAction(Action.OnCloseClick) }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            DateRangePicker(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                state = pickerState,
            )
            DateRangePickerActionButton(
                selectedDateRange = state.selectedRange,
                enableApplyButton = state.enableApplyButton,
                onClick = { onAction(Action.OnApplyClick) }
            )
        }
    }
}
