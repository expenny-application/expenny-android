package org.expenny.feature.dashboard.view

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import org.expenny.core.common.types.DashboardWidgetType
import org.expenny.core.ui.components.ExpennyCard
import org.expenny.core.ui.components.ExpennyIconContainer
import org.expenny.core.ui.extensions.icon
import org.expenny.core.ui.extensions.label


@Composable
internal fun DashboardWidgetsSection(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    widgets: ImmutableList<DashboardWidgetType>,
    onWidgetClick: (DashboardWidgetType) -> Unit
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {
        val visibleWidgetsCount = 3
        val spaceBetweenItems = 16.dp
        val itemWidth = (maxWidth - spaceBetweenItems.times(visibleWidgetsCount - 1)).div(visibleWidgetsCount)

        LazyRow(
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(spaceBetweenItems),
            flingBehavior = rememberSnapFlingBehavior(listState)
        ) {
            items(items = widgets) { item ->
                DashboardWidgetCard(
                    modifier = Modifier.width(itemWidth),
                    widget = item,
                    onClick = {
                        onWidgetClick(item)
                    }
                )
            }
        }
    }
}

@Composable
private fun DashboardWidgetCard(
    modifier: Modifier = Modifier,
    widget: DashboardWidgetType,
    onClick: () -> Unit
) {
    ExpennyCard(
        modifier = modifier,
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ExpennyIconContainer(
                icon = widget.icon,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                background = MaterialTheme.colorScheme.secondaryContainer
            )
            Text(
                text = widget.label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
