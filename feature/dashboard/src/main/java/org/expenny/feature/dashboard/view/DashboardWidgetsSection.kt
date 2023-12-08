package org.expenny.feature.dashboard.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import org.expenny.core.common.types.DashboardWidget
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyIconBox
import org.expenny.core.ui.foundation.ExpennyCard
import org.expenny.core.ui.foundation.ExpennyText
import org.expenny.core.ui.extensions.icon
import org.expenny.core.ui.extensions.label


@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun DashboardWidgetsSection(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    widgets: ImmutableList<DashboardWidget>,
    onWidgetClick: (DashboardWidget) -> Unit
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
    widget: DashboardWidget,
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
            ExpennyIconBox(
                icon = widget.icon,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                background = MaterialTheme.colorScheme.secondaryContainer
            )
            ExpennyText(
                text = widget.label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
