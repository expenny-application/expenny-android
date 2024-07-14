package org.expenny.feature.budgets.overview.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.expenny.core.common.extensions.toMonetaryString
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyAccountsFilter
import org.expenny.core.ui.components.ExpennyCard
import org.expenny.core.ui.components.ExpennyDateRangeFilter
import org.expenny.core.ui.components.ExpennyMessage
import org.expenny.core.ui.components.ExpennyVerticalListPaddingValues
import org.expenny.core.ui.data.BudgetUi
import org.expenny.core.ui.extensions.asRawString
import org.expenny.core.ui.extensions.isScrollingUp
import org.expenny.feature.budgets.overview.contract.BudgetOverviewAction
import org.expenny.feature.budgets.overview.contract.BudgetOverviewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BudgetOverviewContent(
    state: BudgetOverviewState,
    accountsFilterListState: LazyListState,
    listState: LazyListState,
    onAction: (BudgetOverviewAction) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val toolbarTitle by rememberUpdatedState(
        newValue = stringResource(
            R.string.periodic_budget_label,
            stringArrayResource(R.array.budget_period_type)[state.intervalTypeState.intervalType.ordinal]
        )
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            BudgetOverviewToolbar(
                scrollBehavior = scrollBehavior,
                title= toolbarTitle,
                displayCurrency = state.displayCurrency,
                onDisplayCurrencyClick = { onAction(BudgetOverviewAction.OnDisplayCurrencyClick) },
                onBackClick = { onAction(BudgetOverviewAction.OnBackClick) }
            )
        },
        floatingActionButton = {
            ExpennyDateRangeFilter(
                modifier = Modifier.navigationBarsPadding(),
                isVisible = listState.isScrollingUp(),
                bounds = state.intervalTypeState.bounds,
                dateRange = state.intervalTypeState.dateRange,
                onPreviousDateRangeClick = { onAction(BudgetOverviewAction.OnPreviousIntervalClick) },
                onNextDateRangeClick = { onAction(BudgetOverviewAction.OnNextIntervalClick) }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        contentWindowInsets = WindowInsets.statusBars,
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            state = listState,
            contentPadding = ExpennyVerticalListPaddingValues,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                ExpennyAccountsFilter(
                    modifier = Modifier.fillMaxWidth(),
                    listState = accountsFilterListState,
                    state = state.accountsFilterState,
                    onSelect = { onAction(BudgetOverviewAction.OnAccountSelect(it)) },
                    onSelectAll = { onAction(BudgetOverviewAction.OnAllAccountsSelect) }
                )
            }
            if (state.isReadonly) {
                item {
                    ExpennyMessage {
                        MessageText(text = stringResource(id = R.string.readonly_budget_overview_paragraph))
                    }
                }
            }
            item {
                BudgetOverviewProgressIndicator(
                    progress = state.overview.progressValue,
                    lowerBound = state.overview.spentValue,
                    upperBound = state.overview.limitValue,
                    value = state.overview.leftAmount?.displayValue ?: stringResource(R.string.not_assigned_label)
                )
            }
            item {
                BudgetLimitsList(
                    limits = state.overview.budgets,
                    onClick = { onAction(BudgetOverviewAction.OnBudgetLimitClick(it)) },
                    onAddNewClick = { onAction(BudgetOverviewAction.OnAddBudgetLimitClick) },
                )
            }
        }
    }
}

@Composable
private fun BudgetLimitsList(
    modifier: Modifier = Modifier,
    limits: List<BudgetUi>,
    onClick: (Long) -> Unit,
    onAddNewClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.budget_limits_label),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                modifier = Modifier.clickable { onAddNewClick() },
                text = stringResource(R.string.add_new_button),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            limits.forEach {
                BudgetLimitItem(
                    data = it,
                    onClick = {
                        onClick(it.id)
                    }
                )
            }
        }
    }
}

@Composable
private fun BudgetLimitItem(
    modifier: Modifier = Modifier,
    data: BudgetUi,
    onClick: () -> Unit
) {
    ExpennyCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BudgetLimitProgressIndicator(
                color = data.category.icon.color,
                progress = data.progressValue,
                value = "${data.progressPercentage}%"
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = data.category.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                Text(
                    text = stringResource(R.string.spent_label, data.spentValue.toMonetaryString()),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = data.limit.displayValue,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                Text(
                    text = stringResource(R.string.left_label, data.leftValue.toMonetaryString()),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }
        }
    }
}