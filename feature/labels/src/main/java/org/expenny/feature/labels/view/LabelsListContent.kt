package org.expenny.feature.labels.view

import androidx.compose.foundation.layout.*
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
import org.expenny.feature.labels.Action
import org.expenny.feature.labels.State

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LabelsListContent(
    state: State,
    lazyListState: LazyListState,
    onAction: (Action) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LabelsListToolbar(
                scrollBehavior = scrollBehavior,
                title = state.toolbarTitle.asRawString(),
                onAddClick = { onAction(Action.OnAddLabelClick) },
                onBackClick = { onAction(Action.OnBackClick) }
            )
        },
        floatingActionButton = {
            if (state.showConfirmButton) {
                LabelsListActionButton(
                    modifier = Modifier.navigationBarsPadding(),
                    isExpanded = lazyListState.isScrollingUp(),
                    onClick = { onAction(Action.OnConfirmClick) }
                )
            }
        },
        contentWindowInsets = WindowInsets.statusBars,
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        LabelsList(
            modifier = Modifier.padding(paddingValues),
            labels = state.labels,
            selection = state.selection,
            onLabelClick = { onAction(Action.OnLabelClick(it)) },
        )
    }
}