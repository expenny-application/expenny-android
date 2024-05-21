package org.expenny.feature.currencies.list.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import org.expenny.core.ui.extensions.asRawString
import org.expenny.feature.currencies.list.contract.CurrenciesListAction
import org.expenny.feature.currencies.list.contract.CurrenciesListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CurrenciesListContent(
    state: CurrenciesListState,
    onAction: (CurrenciesListAction) -> Unit
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
                onAddClick = { onAction(CurrenciesListAction.OnCurrencyAddClick) },
                onBackClick = { onAction(CurrenciesListAction.OnBackClick) }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        CurrenciesList(
            modifier = Modifier.padding(paddingValues),
            currencies = state.currencies,
            selection = state.selection,
            onCurrencyClick = { id, isMain ->
                onAction(CurrenciesListAction.OnCurrencyClick(id, isMain))
            }
        )
    }
}