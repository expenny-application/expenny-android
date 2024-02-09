package org.expenny.feature.dashboard.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.data.ui.AccountNameUi
import org.expenny.core.ui.foundation.ExpennyChip
import org.expenny.core.ui.foundation.ExpennySelectableChip

@Composable
internal fun DashboardAccountsFilter(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    selectAll: Boolean,
    accounts: List<AccountNameUi>,
    selectedAccounts: List<AccountNameUi>,
    onSelectAll: () -> Unit,
    onSelect: (AccountNameUi) -> Unit,
    onCreateClick: () -> Unit
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
                ExpennySelectableChip(
                    isSelected = selectAll,
                    onClick = onSelectAll,
                    label = {
                        Text(text = stringResource(R.string.all_accounts_label))
                    }
                )
            }
            items(
                items = accounts,
                key = { it.id }
            ) { account ->
                ExpennySelectableChip(
                    isSelected = selectedAccounts.contains(account) && !selectAll,
                    onClick = {
                        onSelect(account)
                    },
                    label = {
                        Text(text = account.displayName)
                    }
                )
            }
        }
        ExpennyChip(
            onClick = onCreateClick,
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = null
                )
            }
        )
    }
}
