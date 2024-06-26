package org.expenny.feature.records.list.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
            if (filterSelectionsState.selectionSize > 0) {
                ExpennyChip(
                    isSelected = false,
                    onClick = onClearClick,
                    content = {
                        ChipIcon(painter = painterResource(R.drawable.ic_delete))
                    }
                )
            }
            LazyRow(
                modifier = Modifier.weight(1f),
                state = listState,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filterTypes) { filterType ->
                    when (filterType) {
                        RecordsFilterType.WithoutCategory -> {
                            ExpennyChip(
                                isSelected = filterSelectionsState.withoutCategory,
                                onClick = { onClick(filterType) },
                                content = {
                                    ChipText(text = filterType.label)
                                }
                            )
                        }
                        else -> {
                            val selectionSize = filterSelectionsState.selectionSizeByFilterType(filterType)
                            ExpennyChip(
                                isSelected = selectionSize > 0,
                                count = selectionSize,
                                onClick = {
                                    onClick(filterType)
                                },
                                trailingContent = {
                                    ChipIcon(painter = painterResource(R.drawable.ic_expand))
                                },
                                content = {
                                    ChipText(text = filterType.label)
                                }
                            )
                        }
                    }
                }
            }
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
