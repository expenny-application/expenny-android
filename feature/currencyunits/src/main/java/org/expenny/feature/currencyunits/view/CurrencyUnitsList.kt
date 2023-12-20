package org.expenny.feature.currencyunits.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.expenny.core.ui.data.selection.SingleSelection
import org.expenny.core.ui.foundation.ExpennyGroupedVerticalList
import org.expenny.core.ui.foundation.*
import org.expenny.core.ui.data.ui.CurrencyUnitUi
import java.util.*

@Composable
internal fun CurrencyUnitsList(
    modifier: Modifier = Modifier,
    selection: SingleSelection<Long>,
    currencyUnits: SortedMap<String, List<CurrencyUnitUi>>,
    onCurrencyUnitClick: (Long) -> Unit,
) {
    ExpennyGroupedVerticalList(
        modifier = modifier,
        groupedList = currencyUnits,
        listItemKey = CurrencyUnitUi::code,
        listItemHeader = { header ->
            ExpennyText(
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
                selected = selection.contains(item.id),
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
    selected: Boolean,
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
                ExpennyText(
                    text = code,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                ExpennyText(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            ExpennyRadioButton(
                isSelected = selected,
                onClick = onClick
            )
        }
    }
}