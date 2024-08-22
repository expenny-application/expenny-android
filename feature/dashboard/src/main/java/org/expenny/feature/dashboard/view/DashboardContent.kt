package org.expenny.feature.dashboard.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import org.expenny.core.ui.base.ExpennyDrawerManager
import org.expenny.feature.dashboard.contract.DashboardAction
import org.expenny.feature.dashboard.contract.DashboardState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DashboardContent(
    state: DashboardState,
    drawerState: ExpennyDrawerManager,
    scope: CoroutineScope,
    scrollState: ScrollState,
    addRecordSheetState: SheetState,
    accountsListState: LazyListState,
    widgetsListState: LazyListState,
    onAction: (DashboardAction) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    if (state.showAddRecordDialog) {
        DashboardAddRecordDialog(
            scope = scope,
            sheetState = addRecordSheetState,
            onRecordTypeSelect = { onAction(DashboardAction.OnAddRecord(it)) },
            onDismiss = { onAction(DashboardAction.OnAddRecordDialogDismiss) }
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
                onDisplayCurrencyClick = { onAction(DashboardAction.OnDisplayCurrencyClick) }
            )
        },
        floatingActionButton = {
            DashboardFloatingActionButton(
                modifier = Modifier.navigationBarsPadding(),
                onClick = { onAction(DashboardAction.OnAddRecordClick) }
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
                    top = 8.dp,
                    bottom = 72.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DashboardAccountsSection(
                modifier = Modifier.fillMaxWidth(),
                accountsListState = accountsListState,
                accountsFilterState = state.accountsFilterState,
                balance = state.balanceData,
                onSelectAll = { onAction(DashboardAction.OnAllAccountsSelect) },
                onSelect = { onAction(DashboardAction.OnAccountSelect(it)) },
                onShowMoreRecordsClick = { onAction(DashboardAction.OnShowMoreRecordsClick) }
            )
            DashboardWidgetsSection(
                modifier = Modifier.fillMaxWidth(),
                listState = widgetsListState,
                widgets = state.widgets,
                onWidgetClick = { onAction(DashboardAction.OnWidgetClick(it)) }
            )
            DashboardExpensesSection(
                modifier = Modifier.fillMaxWidth(),
                periodTypes = state.periodTypes,
                expensesData = state.expensesData,
                currentPeriodType = state.currentPeriodType,
                onCategorySelect = { onAction(DashboardAction.OnCategoryExpensesSelect(it)) },
                onCategoryDeselect = { onAction(DashboardAction.OnCategoryExpensesDeselect) },
                onTimePeriodChange = { onAction(DashboardAction.OnExpensesPeriodTypeChange(it)) }
            )
        }
    }
}
