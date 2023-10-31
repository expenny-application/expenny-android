package org.expenny.feature.accounts.view

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import org.expenny.core.ui.extensions.asRawString
import org.expenny.core.ui.extensions.isScrollingUp
import org.expenny.core.ui.utils.ExpennyDrawerState
import org.expenny.feature.accounts.model.Action
import org.expenny.feature.accounts.model.State

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AccountsListContent(
    state: State,
    lazyListState: LazyListState,
    drawerState: ExpennyDrawerState,
    onAction: (Action) -> Unit,
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
                onAddClick = { onAction(Action.OnAccountAddClick) },
                onBackClick = { onAction(Action.OnBackClick) }
            )
        },
        floatingActionButton = {
            if (state.showConfirmButton) {
                AccountsListConfirmSelectionButton(
                    modifier = Modifier.navigationBarsPadding(),
                    isExpanded = lazyListState.isScrollingUp(),
                    onClick = { onAction(Action.OnConfirmSelectionClick) }
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
            onAccountClick = { onAction(Action.OnAccountClick(it)) },
        )
    }
}
