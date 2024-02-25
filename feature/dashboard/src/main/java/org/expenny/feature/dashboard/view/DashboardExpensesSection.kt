package org.expenny.feature.dashboard.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import org.expenny.core.common.types.ChronoPeriod
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyDonutChart
import org.expenny.core.ui.components.ExpennySegmentedTabRow
import org.expenny.core.ui.data.ui.AmountUi
import org.expenny.core.ui.data.ui.ExpensesUi
import org.expenny.core.ui.extensions.label
import org.expenny.core.ui.foundation.ExpennyCard
import org.expenny.feature.dashboard.model.DashboardExpensesUi


@Composable
internal fun DashboardExpensesSection(
    modifier: Modifier = Modifier,
    chronoPeriods: ImmutableList<ChronoPeriod>,
    expensesData: DashboardExpensesUi,
    currentChronoPeriod: ChronoPeriod,
    onCategorySelect: (index: Int) -> Unit,
    onCategoryDeselect: () -> Unit,
    onTimePeriodChange: (ChronoPeriod) -> Unit
) {
    ExpennyCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            DashboardExpensesHeading(
                expensesAmount = expensesData.expensesAmount,
                expensesPercentage = expensesData.expensePercentage,
                selectedEntry = expensesData.selectedEntry
            )
            Row(
                modifier = Modifier
                    .height(140.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DashboardExpensesChart(
                    modifier = Modifier.weight(1f),
                    expensesData = expensesData,
                    onSliceSelect = onCategorySelect,
                    onSliceDeselect = onCategoryDeselect
                )
                DashboardExpensesChartLegend(
                    modifier = Modifier.weight(1f),
                    entries = expensesData.expensesLegend
                )
            }
            DashboardExpensesTimePeriodFilter(
                modifier = Modifier.fillMaxWidth(),
                chronoPeriods = chronoPeriods,
                currentChronoPeriod = currentChronoPeriod,
                onChange = onTimePeriodChange
            )
        }
    }
}

@Composable
private fun DashboardExpensesHeading(
    expensesAmount: AmountUi?,
    expensesPercentage: AmountUi?,
    selectedEntry: ExpensesUi?,
) {
    val expensesSubtitle = buildString {
        append(stringResource(R.string.expenses_label))
        selectedEntry?.label?.let {
            append(" • $it")
        }
    }
    val expensesTitle = buildAnnotatedString {
        append(expensesAmount?.displayValue ?: stringResource(R.string.na_label))
        expensesPercentage?.let {
            withStyle(
                MaterialTheme.typography.bodyMedium
                    .toSpanStyle()
                    .copy(color = MaterialTheme.colorScheme.primary)
            ) {
                append(" ${it.displayValue}")
            }
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        AnimatedContent(
            targetState = expensesSubtitle,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "ExpensesSubtitle"
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = stringResource(R.string.more_label),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        AnimatedContent(
            targetState = expensesTitle,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "ExpensesTitle"
        ) {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
private fun DashboardExpensesTimePeriodFilter(
    modifier: Modifier = Modifier,
    chronoPeriods: ImmutableList<ChronoPeriod>,
    currentChronoPeriod: ChronoPeriod,
    onChange: (ChronoPeriod) -> Unit
) {
    ExpennySegmentedTabRow(
        modifier = modifier.height(40.dp),
        tabs = chronoPeriods.map { it.label },
        selectedTabIndex = chronoPeriods.indexOf(currentChronoPeriod),
        onTabSelect = {
            onChange(chronoPeriods[it])
        }
    )
}

@Composable
private fun DashboardExpensesChart(
    modifier: Modifier = Modifier,
    expensesData: DashboardExpensesUi,
    onSliceSelect: (index: Int) -> Unit,
    onSliceDeselect: () -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    val transitionProgress = remember(expensesData.entries) {
        androidx.compose.animation.core.Animatable(initialValue = 0f)
    }

    LaunchedEffect(transitionProgress) {
        if (expensesData.entries.isNotEmpty()) {
            transitionProgress.animateTo(
                targetValue = 1f,
                animationSpec = TweenSpec(durationMillis = 700)
            )
        }
    }

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        ExpennyDonutChart(
            entries = expensesData.entries,
            progress = transitionProgress.value,
            selectedEntryIndex = expensesData.selectedEntryIndex,
            onSliceSelect = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onSliceSelect(it)
            },
            onSelectionRelease = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onSliceDeselect()
            }
        )

        DashboardExpensesChartSummary(
            modifier = Modifier.size(maxWidth / 2),
            expensesCount = expensesData.expensesCount
        )
    }
}

@Composable
private fun DashboardExpensesChartSummary(
    modifier: Modifier = Modifier,
    expensesCount: Int
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = expensesCount.toString(),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = stringResource(R.string.records_label),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun DashboardExpensesChartLegend(
    modifier: Modifier = Modifier,
    entries: List<ExpensesUi>,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
    ) {
        items(items = (0..4).toList()) { index ->
            DashboardExpensesChartLegendItem(
                entry = entries.getOrNull(index)
            )
        }
    }
}

@Composable
private fun DashboardExpensesChartLegendItem(entry: ExpensesUi?) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(entry?.color ?: MaterialTheme.colorScheme.outlineVariant)
        )
        Text(
            text = entry?.label ?: stringResource(R.string.na_label),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
