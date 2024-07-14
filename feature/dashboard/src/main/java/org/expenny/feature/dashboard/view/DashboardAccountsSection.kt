package org.expenny.feature.dashboard.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.expenny.core.ui.components.ExpennyAccountsFilter
import org.expenny.core.ui.data.AccountNameUi
import org.expenny.core.ui.reducers.AccountsFilterStateReducer
import org.expenny.feature.dashboard.contract.DashboardAction
import org.expenny.feature.dashboard.model.DashboardBalanceUi

@Composable
internal fun DashboardAccountsSection(
    modifier: Modifier = Modifier,
    accountsListState: LazyListState,
    accountsFilterState: AccountsFilterStateReducer.State,
    balance: DashboardBalanceUi,
    onSelect: (Long) -> Unit,
    onSelectAll: () -> Unit,
    onShowMoreRecordsClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ExpennyAccountsFilter(
            modifier = Modifier.fillMaxWidth(),
            listState = accountsListState,
            state = accountsFilterState,
            onSelect = onSelect,
            onSelectAll = onSelectAll
        )
        DashboardBalanceSection(
            modifier = Modifier.fillMaxWidth(),
            balanceData = balance,
            onShowMoreRecordsClick = onShowMoreRecordsClick
        )
    }
}