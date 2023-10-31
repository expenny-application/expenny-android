package org.expenny.feature.currencies.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import org.expenny.core.ui.extensions.asRawString
import org.expenny.feature.currencies.Action
import org.expenny.feature.currencies.State

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CurrenciesListContent(
    state: State,
    onAction: (Action) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .imePadding(),
        topBar = {
            CurrenciesListToolbar(
                title = state.toolbarTitle.asRawString(),
                scrollBehavior = scrollBehavior,
                onAddClick = { onAction(Action.OnCurrencyAddClick) },
                onBackClick = { onAction(Action.OnBackClick) }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        CurrenciesList(
            modifier = Modifier.padding(paddingValues),
            currencies = state.currencies,
            selection = state.selection,
            onCurrencyClick = { id, isMain ->
                onAction(Action.OnCurrencyClick(id, isMain))
            }
        )
    }
}