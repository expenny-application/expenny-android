package org.expenny.feature.records.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import org.expenny.core.ui.components.ExpennyRecord
import org.expenny.core.ui.data.ui.RecordUi
import org.expenny.core.ui.foundation.ExpennyText
import org.expenny.core.ui.foundation.ExpennyCard
import org.expenny.core.ui.components.ExpennySelectionButton
import org.expenny.core.ui.data.selection.MultiSelection
import org.expenny.core.ui.data.selection.SelectionType
import org.expenny.feature.records.model.*
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun RecordsList(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    isSelectionMode: Boolean,
    selection: MultiSelection<Long>,
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
                        modifier = Modifier.animateItemPlacement(),
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
        ExpennyText(
            text = header.date,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelLarge,
        )
        ExpennyText(
            text = header.dateAmount.displayValue,
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
