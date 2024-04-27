package org.expenny.feature.currencyunits.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.expenny.core.ui.data.SingleSelectionUi
import org.expenny.core.ui.data.CurrencyUnitUi
import org.expenny.core.ui.components.ExpennyCard
import org.expenny.core.ui.components.ExpennyGroupedVerticalList
import java.util.SortedMap

@Composable
internal fun CurrencyUnitsList(
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

@OptIn(ExperimentalMaterial3Api::class)
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
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                RadioButton(
                    selected = isSelected,
                    onClick = onClick
                )
            }
        }
    }
}