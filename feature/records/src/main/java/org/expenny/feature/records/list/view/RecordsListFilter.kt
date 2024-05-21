package org.expenny.feature.records.list.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.expenny.core.common.types.RecordsFilterType
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyChip
import org.expenny.core.ui.extensions.label
import org.expenny.feature.records.list.reducer.FilterSelectionsStateReducer

@Composable
internal fun RecordsListFilter(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    filterTypes: List<RecordsFilterType>,
    filterSelectionsState: FilterSelectionsStateReducer.State,
    onClearClick: () -> Unit,
    onClick: (RecordsFilterType) -> Unit,
) {
    if (filterTypes.isNotEmpty()) {
        Row(
            modifier = modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterIcon(
                selectionSize = filterSelectionsState.selectionSize,
                onClearClick = onClearClick
            )
            LazyRow(
                modifier = Modifier.weight(1f),
                state = listState,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filterTypes) { filterType ->
                    when (filterType) {
                        RecordsFilterType.WithoutCategory -> {
                            ExpennyChip(
                                modifier = modifier,
                                isSelected = filterSelectionsState.withoutCategory,
                                onClick = { onClick(filterType) },
                                label = {
                                    ChipLabel(text = filterType.label)
                                }
                            )
                        }
                        else -> {
                            val selectionSize = filterSelectionsState.selectionSizeByFilterType(filterType)
                            ExpennyChip(
                                modifier = modifier,
                                isSelected = selectionSize > 0,
                                count = selectionSize,
                                onClick = {
                                    onClick(filterType)
                                },
                                trailingIcon = {
                                    ChipIcon(painter = painterResource(R.drawable.ic_expand))
                                },
                                label = {
                                    ChipLabel(text = filterType.label)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterIcon(
    modifier: Modifier = Modifier,
    selectionSize: Int,
    onClearClick: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        ExpennyChip(
            isSelected = false,
            onClick = { showMenu = true },
            count = selectionSize,
            label = {
                ChipIcon(painter = painterResource(R.drawable.ic_filter))
            },
        )
        DropdownMenu(
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerHigh),
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(R.string.clear_filter_label),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                onClick = {
                    showMenu = false
                    onClearClick()
                }
            )
        }
    }
}

private fun FilterSelectionsStateReducer.State.selectionSizeByFilterType(
    filterType: RecordsFilterType
): Int {
    return when (filterType) {
        RecordsFilterType.Types -> recordTypesSelection.size
        RecordsFilterType.Accounts -> accountsSelection.size
        RecordsFilterType.Categories -> categoriesSelection.size
        RecordsFilterType.Labels -> labelsSelection.size
        RecordsFilterType.WithoutCategory -> 0
    }
}
