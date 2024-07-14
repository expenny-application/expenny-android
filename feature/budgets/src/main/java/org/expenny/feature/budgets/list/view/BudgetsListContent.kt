package org.expenny.feature.budgets.list.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.expenny.core.common.types.BudgetType
import org.expenny.core.resources.R
import org.expenny.core.ui.base.ExpennyDrawerManager
import org.expenny.core.ui.components.ExpennyCard
import org.expenny.core.ui.components.ExpennyLabel
import org.expenny.core.ui.components.ExpennySection
import org.expenny.core.ui.components.ExpennySegmentedSurfaceTabs
import org.expenny.core.ui.data.PeriodicBudgetUi
import org.expenny.core.ui.extensions.label
import org.expenny.feature.budgets.list.contract.BudgetsListAction
import org.expenny.feature.budgets.list.contract.BudgetsListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BudgetsListContent(
    state: BudgetsListState,
    periodicBudgetsLazyListState: LazyListState,
    onetimeBudgetsLazyListState: LazyListState,
    drawerState: ExpennyDrawerManager,
    onAction: (BudgetsListAction) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            BudgetsListToolbar(
                drawerState = drawerState,
                scrollBehavior = scrollBehavior,
                showAddButton = state.showAddButton,
                onAddClick = { onAction(BudgetsListAction.OnOnetimeBudgetCreateClick) },
                onBackClick = { onAction(BudgetsListAction.OnBackClick) }
            )
        },
        contentWindowInsets = WindowInsets.statusBars,
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = when (state.selectedBudgetType) {
                BudgetType.Periodic -> periodicBudgetsLazyListState
                BudgetType.Onetime -> onetimeBudgetsLazyListState
            },
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            )
        ) {
            item {
                ExpennySegmentedSurfaceTabs(
                    modifier = Modifier.fillMaxWidth(),
                    tabs = state.budgetTypes.map { it.label },
                    selectedTabIndex = state.budgetTypes.indexOf(state.selectedBudgetType),
                    onTabSelect = {
                        onAction(BudgetsListAction.OnBudgetTypeChange(state.budgetTypes[it]))
                    }
                )
            }

            when (state.selectedBudgetType) {
                BudgetType.Periodic -> {
                    items(
                        items = state.periodicBudgets,
                        key = { it.id },
                        contentType = { it }
                    ) {
                        ExpennySection(
                            title = it.intervalType.label,
                            isExpanded = true,
                            onClick = {}
                        ) {
                            BudgetItem(
                                data = it,
                                onClick = { onAction(BudgetsListAction.OnPeriodicBudgetClick(it.id, it.intervalType)) }
                            )
                        }
                    }
                    items(
                        items = state.availableBudgetIntervalType
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp)
                                .clickable { onAction(BudgetsListAction.OnPeriodicBudgetCreateClick(it)) },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = it.label,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Icon(
                                painter = painterResource(R.drawable.ic_add),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                contentDescription = null
                            )
                        }
                    }
                }
                BudgetType.Onetime -> {

                }
            }
        }
    }
}

@Composable
private fun BudgetItem(
    modifier: Modifier = Modifier,
    data: PeriodicBudgetUi,
    onClick: () -> Unit
) {
    ExpennyCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.remaining_funds_label),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = data.leftAmount.displayValue,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                ExpennyLabel(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    content = {
                        LabelText(text = "${data.favorabilityPercentage}%")
                    }
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    progress = { data.progressValue },
                    gapSize = 0.dp,
                    trackColor = MaterialTheme.colorScheme.surface,
                    color = MaterialTheme.colorScheme.primary,
                    drawStopIndicator = null
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = data.currentValue.toString(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = data.limitValue.toString(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            if (data.categories.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    data.categories.forEach {
                        ExpennyLabel(
                            leadingContent = {
                                LabelIcon(painter = painterResource(it.icon.iconResId))
                            },
                            content = {
                                LabelText(text = it.name)
                            }
                        )
                    }
                }
            }
        }
    }
}