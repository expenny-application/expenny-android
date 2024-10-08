package org.expenny.feature.daterangepicker.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R

@Composable
internal fun BoxScope.DateRangePickerActionButton(
    selectedDateRange: Pair<String?, String?>,
    enableApplyButton: Boolean,
    onClick: () -> Unit
) {
    val selectedDateRangeCaption = selectedDateRange.let {
        if (it.first == null && it.second == null) {
            stringResource(R.string.no_range_selected_label)
        } else {
            val startDate = it.first ?: stringResource(R.string.start_date_label)
            val endDate = it.second ?: stringResource(R.string.end_date_label)

            "$startDate — $endDate"
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter),
        shadowElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 24.dp,
                bottom = 32.dp,
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = selectedDateRangeCaption,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}