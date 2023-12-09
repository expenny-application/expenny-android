package org.expenny.feature.categories.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import org.expenny.core.ui.extensions.asRawString
import org.expenny.feature.categories.Action
import org.expenny.feature.categories.State

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CategoriesListContent(
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
            CategoriesListToolbar(
                scrollBehavior = scrollBehavior,
                title = state.toolbarTitle.asRawString(),
                onAddClick = { onAction(Action.OnAddCategoryClick) },
                onBackClick = { onAction(Action.OnBackClick) }
            )
        },
        contentWindowInsets = WindowInsets.statusBars,
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        CategoriesList(
            modifier = Modifier.padding(paddingValues),
            categories = state.categories,
            selection = state.selection,
            onCategoryClick = { onAction(Action.OnCategoryClick(it)) },
        )
    }
}