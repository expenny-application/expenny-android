package org.expenny.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.reducers.AccountsFilterStateReducer

@Composable
fun ExpennyAccountsFilter(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    state: AccountsFilterStateReducer.State,
    onSelectAll: () -> Unit,
    onSelect: (Long) -> Unit,
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
                    isSelected = state.selectAll,
                    onClick = onSelectAll,
                    content = {
                        ChipText(text = stringResource(R.string.all_accounts_label))
                    }
                )
            }
            items(
                items = state.accounts,
                key = { it.key }
            ) { account ->
                ExpennyChip(
                    isSelected = state.selection.contains(account.key) && !state.selectAll,
                    onClick = {
                        onSelect(account.key)
                    },
                    content = {
                        ChipText(text = account.label)
                    }
                )
            }
        }
    }
}