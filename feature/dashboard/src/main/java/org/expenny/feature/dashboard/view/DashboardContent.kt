package org.expenny.feature.dashboard.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import org.expenny.core.ui.utils.ExpennyDrawerState
import org.expenny.feature.dashboard.model.Action
import org.expenny.feature.dashboard.model.State

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DashboardContent(
    state: State,
    drawerState: ExpennyDrawerState,
    scope: CoroutineScope,
    scrollState: ScrollState,
    addRecordSheetState: SheetState,
    accountsListState: LazyListState,
    widgetsListState: LazyListState,
    onAction: (Action) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    if (state.showAddRecordDialog) {
        DashboardAddRecordDialog(
            scope = scope,
            sheetState = addRecordSheetState,
            onRecordTypeSelect = { onAction(Action.OnAddRecord(it)) },
            onDismiss = { onAction(Action.OnAddRecordDialogDismiss) }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            DashboardToolbar(
                scrollBehavior = scrollBehavior,
                drawerState = drawerState,
                displayCurrency = state.displayCurrency,
                onDisplayCurrencyClick = { onAction(Action.OnDisplayCurrencyClick) }
            )
        },
        floatingActionButton = {
            DashboardFloatingActionButton(
                modifier = Modifier.navigationBarsPadding(),
                onClick = { onAction(Action.OnAddRecordClick) }
            )
        },
        contentWindowInsets = WindowInsets.statusBars,
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .navigationBarsPadding()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 72.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DashboardAccountsSection(
                modifier = Modifier.fillMaxWidth(),
                accountsListState = accountsListState,
                selectAll = state.selectAllAccounts,
                accounts = state.accounts,
                selectedAccounts = state.selectedAccounts,
                balance = state.balanceData,
                onSelectAll = { onAction(Action.OnAllAccountsSelect) },
                onSelect = { onAction(Action.OnAccountSelect(it)) },
                onCreate = { onAction(Action.OnCreateAccountClick) },
                onShowMoreRecordsClick = { onAction(Action.OnShowMoreRecordsClick) }
            )
            DashboardWidgetsSection(
                modifier = Modifier.fillMaxWidth(),
                listState = widgetsListState,
                widgets = state.widgets,
                onWidgetClick = { onAction(Action.OnWidgetClick(it)) }
            )
            DashboardExpensesSection(
                modifier = Modifier.fillMaxWidth(),
                timePeriods = state.timePeriods,
                expensesData = state.expensesData,
                currentTimePeriod = state.currentTimePeriod,
                onCategorySelect = { onAction(Action.OnCategoryExpensesSelect(it)) },
                onCategoryDeselect = { onAction(Action.OnCategoryExpensesDeselect) },
                onTimePeriodChange = { onAction(Action.OnExpensesTimeSpanChange(it)) }
            )
        }
    }
}
