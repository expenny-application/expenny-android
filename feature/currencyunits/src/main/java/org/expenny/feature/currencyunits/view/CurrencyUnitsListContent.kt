package org.expenny.feature.currencyunits.view

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import org.expenny.feature.currencyunits.Action
import org.expenny.feature.currencyunits.State

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CurrencyUnitsListContent(
    state: State,
    onAction: (Action) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CurrencyUnitsListToolbar(
                scrollBehavior = scrollBehavior,
                searchQuery = state.searchQuery,
                onSearchQueryChange = { onAction(Action.OnSearchQueryChange(it)) },
                onCloseClick = { onAction(Action.OnCloseClick) }
            )
        },
        contentWindowInsets = WindowInsets.statusBars,
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        CurrencyUnitsList(
            modifier = Modifier.padding(paddingValues),
            currencyUnits = state.currencyUnits,
            selection = state.selection,
            onCurrencyUnitClick = { onAction(Action.OnCurrencyUnitSelect(it)) },
        )
    }
}