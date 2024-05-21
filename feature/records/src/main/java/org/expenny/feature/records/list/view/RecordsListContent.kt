package org.expenny.feature.records.list.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import org.expenny.core.ui.components.ExpennyDateRangeFilterButton
import org.expenny.core.ui.extensions.isScrollingUp
import org.expenny.core.ui.base.ExpennyDrawerManager
import org.expenny.core.ui.components.ExpennyCard
import org.expenny.core.ui.components.ExpennyRecord
import org.expenny.core.ui.components.ExpennySelectionButton
import org.expenny.core.ui.data.MultiSelectionUi
import org.expenny.core.ui.data.RecordUi
import org.expenny.core.ui.data.SelectionType
import org.expenny.feature.records.list.contract.RecordsListAction
import org.expenny.feature.records.list.contract.RecordsListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecordsListContent(
    state: RecordsListState,
    scope: CoroutineScope,
    recordActionsSheetState: SheetState,
    drawerState: ExpennyDrawerManager,
    lazyListState: LazyListState,
    onAction: (RecordsListAction) -> Unit,
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
                onBackClick = { onAction(RecordsListAction.OnBackClick) },
                onCloseSelectionMode = { onAction(RecordsListAction.OnExitSelectionModeClick) },
                onSelectAllClick = { onAction(RecordsListAction.OnSelectAllClick) },
                onDeleteClick = { onAction(RecordsListAction.OnDeleteSelectedRecordsClick) },
                onAddClick = { onAction(RecordsListAction.OnAddRecordClick) }
            )
        },
        floatingActionButton = {
            ExpennyDateRangeFilterButton(
                modifier = Modifier.navigationBarsPadding(),
                isVisible = lazyListState.isScrollingUp() && !state.isSelectionMode,
                currentDateRange = state.intervalState.dateRangeString,
                onSelectDateRecurrenceClick = { onAction(RecordsListAction.OnSelectIntervalTypeClick) },
                onPreviousDateRangeClick = { onAction(RecordsListAction.OnPreviousIntervalClick) },
                onNextDateRangeClick = { onAction(RecordsListAction.OnNextIntervalClick) }
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
            onRecordLongClick = { onAction(RecordsListAction.OnRecordLongClick(it)) },
            onRecordClick = { onAction(RecordsListAction.OnRecordClick(it)) },
        ) {
            RecordsListFilter(
                listState = rememberLazyListState(),
                filterTypes = state.filterTypes,
                filterSelectionsState = state.filterSelectionsState,
                onClearClick = { onAction(RecordsListAction.OnClearFilterClick) },
                onClick = { onAction(RecordsListAction.OnFilterClick(it)) },
            )
        }
    }
}

@Composable
private fun RecordsList(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    isSelectionMode: Boolean,
    selection: MultiSelectionUi<Long>,
    records: List<RecordUi>,
    onRecordClick: (Long) -> Unit,
    onRecordLongClick: (Long) -> Unit,
    filter: @Composable LazyItemScope.() -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(
            top = 8.dp,
            start = 16.dp,
            end = 16.dp,
            bottom = 64.dp
        )
    ) {
        item {
            filter()
        }
        itemsIndexed(
            items = records,
            key = { _, record -> record.key }
        ) { index, record ->
            when (record) {
                is RecordUi.Header -> {
                    if (index > 0) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    RecordsListHeader(header = record)
                }
                is RecordUi.Item -> {
                    RecordsListItem(
                        isSelectionMode = isSelectionMode,
                        isSelected = selection.contains(record.id),
                        record = record,
                        onClick = onRecordClick,
                        onLongClick = onRecordLongClick
                    )
                }
            }
        }
    }
}

@Composable
private fun RecordsListHeader(
    modifier: Modifier = Modifier,
    header: RecordUi.Header
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = header.date,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelLarge,
        )
        Text(
            text = header.amountSum.displayValue,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Composable
private fun RecordsListItem(
    modifier: Modifier = Modifier,
    record: RecordUi.Item,
    isSelectionMode: Boolean,
    isSelected: Boolean,
    onClick: (Long) -> Unit,
    onLongClick: (Long) -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AnimatedVisibility(isSelectionMode) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ExpennySelectionButton(
                    isSelected = isSelected,
                    type = SelectionType.Multi,
                    onClick = {
                        onClick(record.id)
                    }
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
        ExpennyCard(
            modifier = modifier.fillMaxWidth(),
            onClick = { onClick(record.id) },
            onLongClick = {
                if (!isSelectionMode) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onLongClick(record.id)
                }
            }
        ) {
            ExpennyRecord(
                modifier = Modifier.padding(16.dp),
                record = record
            )
        }
    }
}
