package org.expenny.feature.currencyunits.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import org.expenny.core.ui.components.ExpennyCard
import org.expenny.core.ui.components.ExpennyGroupedVerticalList
import org.expenny.core.ui.components.ExpennyRadioButton
import org.expenny.core.ui.data.CurrencyUnitUi
import org.expenny.core.ui.data.SingleSelectionUi
import org.expenny.feature.currencyunits.contract.CurrencyUnitsListAction
import org.expenny.feature.currencyunits.contract.CurrencyUnitsListState
import java.util.SortedMap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CurrencyUnitsListContent(
    state: CurrencyUnitsListState,
    onAction: (CurrencyUnitsListAction) -> Unit
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
                onSearchQueryChange = { onAction(CurrencyUnitsListAction.OnSearchQueryChange(it)) },
                onCloseClick = { onAction(CurrencyUnitsListAction.OnCloseClick) }
            )
        },
        contentWindowInsets = WindowInsets.statusBars,
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        CurrencyUnitsList(
            modifier = Modifier.padding(paddingValues),
            currencyUnits = state.currencyUnits,
            selection = state.selection,
            onCurrencyUnitClick = { onAction(CurrencyUnitsListAction.OnCurrencyUnitSelect(it)) },
        )
    }
}

@Composable
private fun CurrencyUnitsList(
    modifier: Modifier = Modifier,
    selection: SingleSelectionUi<Long>,
    currencyUnits: SortedMap<String, List<CurrencyUnitUi>>,
    onCurrencyUnitClick: (Long) -> Unit,
) {
    ExpennyGroupedVerticalList(
        modifier = modifier,
        groupedList = currencyUnits,
        listItemKey = CurrencyUnitUi::code,
        listItemHeader = { header ->
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelLarge,
                text = header,
            )
        },
        listItem = { item ->
            CurrencyUnitItem(
                modifier = Modifier.fillMaxWidth(),
                isSelected = selection.contains(item.id),
                code = item.code,
                name = item.name,
                onClick = {
                    onCurrencyUnitClick(item.id)
                }
            )
        }
    )
}

@Composable
private fun CurrencyUnitItem(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    code: String,
    name: String,
    onClick: () -> Unit
) {
    ExpennyCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = code,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            ExpennyRadioButton(
                isSelected = isSelected,
                onClick = onClick
            )
        }
    }
}