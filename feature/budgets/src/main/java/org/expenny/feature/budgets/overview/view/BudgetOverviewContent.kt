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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.expenny.core.common.extensions.toMonetaryString
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyCard
import org.expenny.core.ui.components.ExpennyChip
import org.expenny.core.ui.components.ExpennyDateRangeFilter
import org.expenny.core.ui.data.AccountNameUi
import org.expenny.core.ui.data.BudgetUi
import org.expenny.core.ui.extensions.asRawString
import org.expenny.core.ui.extensions.isScrollingUp
import org.expenny.feature.budgets.overview.contract.BudgetOverviewAction
import org.expenny.feature.budgets.overview.contract.BudgetOverviewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BudgetOverviewContent(
    state: BudgetOverviewState,
    lazyListState: LazyListState,
    onAction: (BudgetOverviewAction) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            BudgetOverviewToolbar(
                scrollBehavior = scrollBehavior,
                title  = state.toolbarTitle.asRawString(),
                displayCurrency = state.displayCurrency,
                onDisplayCurrencyClick = { onAction(BudgetOverviewAction.OnDisplayCurrencyClick) },
                onBackClick = { onAction(BudgetOverviewAction.OnBackClick) }
            )
        },
        floatingActionButton = {
            ExpennyDateRangeFilter(
                modifier = Modifier.navigationBarsPadding(),
                isVisible = lazyListState.isScrollingUp(),
                bounds = state.dateRangeFilterBounds,
                dateRange = state.intervalState.dateRange,
                onPreviousDateRangeClick = { onAction(BudgetOverviewAction.OnPreviousIntervalClick) },
                onNextDateRangeClick = { onAction(BudgetOverviewAction.OnNextIntervalClick) }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        contentWindowInsets = WindowInsets.statusBars,
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AccountsFilter(
                modifier = Modifier.fillMaxWidth(),
                listState = lazyListState,
                selectAll = state.selectAllAccounts,
                accounts = state.accounts,
                selectedAccounts = state.selectedAccounts,
                onSelect = { onAction(BudgetOverviewAction.OnAccountSelect(it)) },
                onSelectAll = { onAction(BudgetOverviewAction.OnAllAccountsSelect) }
            )
            BudgetOverviewProgressIndicator(
                progress = state.overview.progressValue,
                lowerBound = state.overview.currentValue,
                upperBound = state.overview.limitValue,
                value = state.overview.leftAmount?.displayValue ?: stringResource(R.string.not_assigned_label)
            )
            BudgetLimitsList(
                limits = state.overview.budgets,
                onClick = { onAction(BudgetOverviewAction.OnBudgetLimitClick(it)) },
                onAddNewClick = { onAction(BudgetOverviewAction.OnAddBudgetLimitClick) },
            )
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
            modifier = modifier
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

@Composable
private fun AccountsFilter(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    selectAll: Boolean,
    accounts: List<AccountNameUi>,
    selectedAccounts: List<AccountNameUi>,
    onSelectAll: () -> Unit,
    onSelect: (AccountNameUi) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LazyRow(
            modifier = Modifier.weight(1f),
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                ExpennyChip(
                    isSelected = selectAll,
                    onClick = onSelectAll,
                    content = {
                        ChipText(text = stringResource(R.string.all_accounts_label))
                    }
                )
            }
            items(
                items = accounts,
                key = { it.id }
            ) { account ->
                ExpennyChip(
                    isSelected = selectedAccounts.contains(account) && !selectAll,
                    onClick = {
                        onSelect(account)
                    },
                    content = {
                        ChipText(text = account.displayName)
                    }
                )
            }
        }
    }
}