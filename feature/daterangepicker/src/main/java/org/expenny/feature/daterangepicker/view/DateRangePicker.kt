package org.expenny.feature.daterangepicker.view

import androidx.compose.material3.*
import androidx.compose.material3.DateRangePicker
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DateRangePicker(
    modifier: Modifier = Modifier,
    state: DateRangePickerState,
) {
    DateRangePicker(
        modifier = modifier,
        state = state,
        title = null,
        headline = null,
        showModeToggle = false,
        colors = DatePickerDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface,
            weekdayContentColor = MaterialTheme.colorScheme.onSurface,
            dayContentColor = MaterialTheme.colorScheme.onSurface,
            selectedDayContainerColor = MaterialTheme.colorScheme.primary,
            todayContentColor = MaterialTheme.colorScheme.primary,
            todayDateBorderColor = MaterialTheme.colorScheme.primary,
            dayInSelectionRangeContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            dayInSelectionRangeContainerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}