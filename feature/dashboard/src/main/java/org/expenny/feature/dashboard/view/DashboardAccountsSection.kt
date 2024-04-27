package org.expenny.feature.dashboard.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.expenny.core.ui.data.AccountNameUi
import org.expenny.feature.dashboard.model.DashboardBalanceUi

@Composable
internal fun DashboardAccountsSection(
    modifier: Modifier = Modifier,
    accountsListState: LazyListState,
    selectAll: Boolean,
    selectedAccounts: List<AccountNameUi>,
    accounts: List<AccountNameUi>,
    balance: DashboardBalanceUi,
    onSelect: (AccountNameUi) -> Unit,
    onSelectAll: () -> Unit,
    onCreate: () -> Unit,
    onShowMoreRecordsClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DashboardAccountsFilter(
            modifier = Modifier.fillMaxWidth(),
            listState = accountsListState,
            selectAll = selectAll,
            accounts = accounts,
            selectedAccounts = selectedAccounts,
            onSelectAll = onSelectAll,
            onSelect = onSelect,
            onCreateClick = onCreate
        )
        DashboardBalanceSection(
            modifier = Modifier.fillMaxWidth(),
            balanceData = balance,
            onShowMoreRecordsClick = onShowMoreRecordsClick
        )
    }
}