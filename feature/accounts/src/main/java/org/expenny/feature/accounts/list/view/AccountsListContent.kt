package org.expenny.feature.accounts.list.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import org.expenny.core.resources.R
import org.expenny.core.ui.extensions.asRawString
import org.expenny.core.ui.extensions.isScrollingUp
import org.expenny.core.ui.base.ExpennyDrawerManager
import org.expenny.core.ui.components.ExpennyCard
import org.expenny.core.ui.components.ExpennyFab
import org.expenny.core.ui.components.ExpennySelectionButton
import org.expenny.core.ui.components.ExpennyVerticalList
import org.expenny.core.ui.data.AccountUi
import org.expenny.core.ui.data.AmountUi
import org.expenny.core.ui.data.SelectionType
import org.expenny.core.ui.data.SelectionUi
import org.expenny.core.ui.extensions.type
import org.expenny.feature.accounts.list.contract.AccountsListAction
import org.expenny.feature.accounts.list.contract.AccountsListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AccountsListContent(
    state: AccountsListState,
    lazyListState: LazyListState,
    drawerState: ExpennyDrawerManager,
    onAction: (AccountsListAction) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AccountsListToolbar(
                drawerState = drawerState,
                title = state.toolbarTitle.asRawString(),
                scrollBehavior = scrollBehavior,
                onAddClick = { onAction(AccountsListAction.OnAccountCreateClick) },
                onBackClick = { onAction(AccountsListAction.OnBackClick) }
            )
        },
        floatingActionButton = {
            if (state.showConfirmButton) {
                AccountsListConfirmSelectionButton(
                    modifier = Modifier.navigationBarsPadding(),
                    isExpanded = lazyListState.isScrollingUp(),
                    onClick = { onAction(AccountsListAction.OnConfirmSelectionClick) }
                )
            }
        },
        contentWindowInsets = WindowInsets.statusBars,
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        AccountsList(
            modifier = Modifier.padding(paddingValues),
            lazyListState = lazyListState,
            selection = state.selection,
            accounts = state.accounts,
            onAccountClick = { onAction(AccountsListAction.OnAccountClick(it)) },
        )
    }
}

@Composable
private fun AccountsListConfirmSelectionButton(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    ExpennyFab(
        modifier = modifier,
        onClick = onClick,
        isExpanded = isExpanded,
        icon = {
            FabIcon(painter = painterResource(R.drawable.ic_check))
        },
        label = {
            FabLabel(text = stringResource(R.string.confirm_button))
        }
    )
}

@Composable
private fun AccountsList(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    selection: SelectionUi<Long>?,
    accounts: List<AccountUi>,
    onAccountClick: (Long) -> Unit,
) {
    ExpennyVerticalList(
        modifier = modifier,
        state = lazyListState,
        list = accounts,
        listItemKey = AccountUi::id,
        listItem = { item ->
            AccountItem(
                modifier = Modifier.fillMaxWidth(),
                selectionType = selection?.type,
                selected = selection?.contains(item.id) ?: false,
                title = item.name,
                balance = item.balance,
                recordsCount = item.recordsCount,
                onClick = {
                    onAccountClick(item.id)
                }
            )
        }
    )
}

@Composable
private fun AccountItem(
    modifier: Modifier = Modifier,
    selectionType: SelectionType?,
    selected: Boolean,
    title: String,
    balance: AmountUi,
    recordsCount: Int,
    onClick: () -> Unit
) {
    ExpennyCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = balance.displayValue,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            if (selectionType != null) {
                ExpennySelectionButton(
                    type = selectionType,
                    isSelected = selected,
                    onClick = {
                        onClick()
                    }
                )
            } else {
                Row(
                    modifier = Modifier
                        .align(Alignment.Bottom)
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = recordsCount.toString(),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Icon(
                        modifier = Modifier.fillMaxHeight(),
                        painter = painterResource(R.drawable.ic_list),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null
                    )
                }
            }
        }
    }
}
