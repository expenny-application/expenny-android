package org.expenny.feature.records.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Badge
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyChip
import org.expenny.core.ui.foundation.ExpennySelectableChip
import org.expenny.feature.records.model.RecordsFilterType
import org.expenny.feature.records.reducer.FilterSelectionsStateReducer

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
                            BooleanFilter(
                                label = filterType.label,
                                isSelected = filterSelectionsState.withoutCategory,
                                onClick = { onClick(filterType) }
                            )
                        }
                        else -> {
                            val selectionSize = filterSelectionsState.selectionSizeByFilterType(filterType)
                            SelectionFilter(
                                label = filterType.label,
                                selectionSize = selectionSize,
                                isSelected = selectionSize > 0,
                                onClick = { onClick(filterType) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BooleanFilter(
    modifier: Modifier = Modifier,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    ExpennySelectableChip(
        modifier = modifier,
        isSelected = isSelected,
        onClick = onClick,
        label = {
            Text(text = label)
        }
    )
}

@Composable
private fun SelectionFilter(
    modifier: Modifier = Modifier,
    label: String,
    selectionSize: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    ExpennySelectableChip(
        modifier = modifier,
        isSelected = isSelected,
        leadingIcon = filterItemBadge(selectionSize),
        onClick = onClick,
        trailingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_expand),
                contentDescription = null
            )
        },
        label = {
            Text(text = label)
        }
    )
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
            onClick = { showMenu = true },
            leadingIcon = filterItemBadge(selectionSize),
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_filter),
                    contentDescription = null
                )
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

@Composable
private fun filterItemBadge(selectionSize: Int): @Composable (() -> Unit)? {
    return if (selectionSize > defaultFilterSelectionSize) {
        {
            Badge(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(text = selectionSize.toString())
            }
        }
    } else null
}

private val RecordsFilterType.label: String
    @Composable
    get() = when (this) {
        RecordsFilterType.Accounts -> stringResource(R.string.accounts_label)
        RecordsFilterType.Categories -> stringResource(R.string.categories_label)
        RecordsFilterType.Labels -> stringResource(R.string.labels_label)
        RecordsFilterType.Types -> stringResource(R.string.types_label)
        RecordsFilterType.WithoutCategory -> stringResource(R.string.without_category_label)
    }

private fun FilterSelectionsStateReducer.State.selectionSizeByFilterType(
    filterType: RecordsFilterType
): Int {
    return when (filterType) {
        RecordsFilterType.Types -> recordTypesSelection.size
        RecordsFilterType.Accounts -> accountsSelection.size
        RecordsFilterType.Categories -> categoriesSelection.size
        RecordsFilterType.Labels -> labelsSelection.size
        RecordsFilterType.WithoutCategory -> defaultFilterSelectionSize
    }
}

private val defaultFilterSelectionSize = 0