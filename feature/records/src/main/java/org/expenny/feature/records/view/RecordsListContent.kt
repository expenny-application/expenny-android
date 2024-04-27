package org.expenny.feature.records.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import kotlinx.coroutines.CoroutineScope
import org.expenny.core.ui.components.ExpennyDateRangeFilterButton
import org.expenny.core.ui.extensions.isScrollingUp
import org.expenny.core.ui.base.ExpennyDrawerManager
import org.expenny.feature.records.Action
import org.expenny.feature.records.State

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecordsListContent(
    state: State,
    scope: CoroutineScope,
    recordActionsSheetState: SheetState,
    drawerState: ExpennyDrawerManager,
    lazyListState: LazyListState,
    onAction: (Action) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    RecordsDialog(
        dialog = state.dialog,
        scope = scope,
        recordActionsDialogState = recordActionsSheetState,
        onDialogAction = { onAction(it) }
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            RecordsListToolbar(
                isSelectionMode = state.isSelectionMode,
                selectionCount = state.recordsSelection.value.size,
                drawerState = drawerState,
                scrollBehavior = scrollBehavior,
                onBackClick = { onAction(Action.OnBackClick) },
                onCloseSelectionMode = { onAction(Action.OnExitSelectionModeClick) },
                onSelectAllClick = { onAction(Action.OnSelectAllClick) },
                onDeleteClick = { onAction(Action.OnDeleteSelectedRecordsClick) },
                onAddClick = { onAction(Action.OnAddRecordClick) }
            )
        },
        floatingActionButton = {
            ExpennyDateRangeFilterButton(
                modifier = Modifier.navigationBarsPadding(),
                isVisible = lazyListState.isScrollingUp() && !state.isSelectionMode,
                currentDateRange = state.intervalState.dateRangeString,
                onSelectDateRecurrenceClick = { onAction(Action.OnSelectIntervalTypeClick) },
                onPreviousDateRangeClick = { onAction(Action.OnPreviousIntervalClick) },
                onNextDateRangeClick = { onAction(Action.OnNextIntervalClick) }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        contentWindowInsets = WindowInsets.statusBars,
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        RecordsList(
            modifier = Modifier.padding(paddingValues),
            lazyListState = lazyListState,
            isSelectionMode = state.isSelectionMode,
            selection = state.recordsSelection,
            records = state.records,
            onRecordLongClick = { onAction(Action.OnRecordLongClick(it)) },
            onRecordClick = { onAction(Action.OnRecordClick(it)) },
        ) {
            RecordsListFilter(
                listState = rememberLazyListState(),
                filterTypes = state.filterTypes,
                filterSelectionsState = state.filterSelectionsState,
                onClearClick = { onAction(Action.OnClearFilterClick) },
                onClick = { onAction(Action.OnFilterClick(it)) },
            )
        }
    }
}
