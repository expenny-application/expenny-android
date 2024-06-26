package org.expenny.feature.records.labels.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyChip
import org.expenny.core.ui.components.ExpennyFab
import org.expenny.core.ui.components.ExpennyVerticalListPaddingValues
import org.expenny.core.ui.extensions.noRippleClickable
import org.expenny.feature.records.labels.contract.RecordLabelsListAction
import org.expenny.feature.records.labels.contract.RecordLabelsListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecordLabelsListContent(
    state: RecordLabelsListState,
    onAction: (RecordLabelsListAction) -> Unit
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
                showSearch = state.showSearch,
                searchQuery = state.searchQuery,
                onSearchQueryChange = { onAction(RecordLabelsListAction.OnSearchQueryChange(it)) },
                onCloseClick = { onAction(RecordLabelsListAction.OnCloseClick) }
            )
        },
        floatingActionButton = {
            ExpennyFab(
                onClick = { onAction(RecordLabelsListAction.OnConfirmClick) },
                icon = {
                    FabIcon(painter = painterResource(R.drawable.ic_check))
                },
                label = {
                    FabText(text = stringResource(R.string.confirm_button))
                }
            )
        },
        contentWindowInsets = WindowInsets.statusBars,
        containerColor = MaterialTheme.colorScheme.surface,
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        RecordLabelsList(
            modifier = Modifier.padding(paddingValues),
            currentLabel = state.searchQuery,
            labels = state.filteredLabels,
            selectedLabels = state.selectedLabels,
            onAddLabelClick = { onAction(RecordLabelsListAction.OnLabelAdd(it)) },
            onRemoveLabelClick = { onAction(RecordLabelsListAction.OnLabelRemove(it)) },
        )
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun RecordLabelsList(
    modifier: Modifier = Modifier,
    currentLabel: String,
    labels: List<String>,
    selectedLabels: List<String>,
    onAddLabelClick: (String) -> Unit,
    onRemoveLabelClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        state = rememberLazyListState(),
        contentPadding = ExpennyVerticalListPaddingValues
    ) {
        if (selectedLabels.isNotEmpty()) {
            item {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                ) {
                    selectedLabels.forEach { selectedLabel ->
                        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                            ExpennyChip(
                                onClick = {},
                                content = {
                                    ChipText(text = selectedLabel)
                                },
                                trailingIcon = {
                                    ChipIcon(
                                        painter = painterResource(R.drawable.ic_close),
                                        onClick = {
                                            onRemoveLabelClick(selectedLabel)
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
        if (currentLabel.isNotBlank()) {
            item {
                RecordLabelItem(
                    modifier = Modifier.fillMaxWidth(),
                    label = currentLabel,
                    onAddClick = {
                        onAddLabelClick(currentLabel)
                    }
                )
            }
        }
        items(
            items = labels,
            key = { it }
        ) { label ->
            RecordLabelItem(
                modifier = Modifier.fillMaxWidth(),
                label = label,
                onAddClick = {
                    onAddLabelClick(label)
                }
            )
        }
    }
}

@Composable
private fun RecordLabelItem(
    modifier: Modifier = Modifier,
    label: String,
    onAddClick: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            modifier = Modifier.noRippleClickable { onAddClick() },
            text = stringResource(R.string.add_button),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}