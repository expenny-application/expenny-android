package org.expenny.feature.currencies.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennySelectionButton
import org.expenny.core.ui.data.selection.SelectionType
import org.expenny.core.ui.data.selection.SingleSelection
import org.expenny.core.ui.data.ui.CurrencyUi
import org.expenny.core.ui.extensions.type
import org.expenny.core.ui.foundation.ExpennyCard
import org.expenny.core.ui.foundation.ExpennyVerticalList

@Composable
internal fun CurrenciesList(
    modifier: Modifier = Modifier,
    selection: SingleSelection<Long>?,
    currencies: List<CurrencyUi>,
    onCurrencyClick: (Long, Boolean) -> Unit,
) {
    ExpennyVerticalList(
        modifier = modifier,
        list = currencies,
        listItemKey = CurrencyUi::id,
        listItem = { item ->
            CurrencyItem(
                modifier = Modifier.fillMaxWidth(),
                selectionType = selection?.type,
                isSelected = selection?.contains(item.id) ?: false,
                currency = item,
                onClick = {
                    onCurrencyClick(item.id, item.isMain)
                }
            )
        }
    )
}

@Composable
private fun CurrencyItem(
    modifier: Modifier = Modifier,
    selectionType: SelectionType?,
    isSelected: Boolean,
    currency: CurrencyUi,
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
                    text = currency.code,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = currency.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (currency.isMain) {
                Text(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(
                            horizontal = 8.dp,
                            vertical = 6.dp
                        ),
                    text = stringResource(R.string.main_label),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            } else {
                Text(
                    text = "~${currency.rate.displayValue}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            if (selectionType != null) {
                ExpennySelectionButton(
                    isSelected = isSelected,
                    type = selectionType,
                    onClick = { onClick() }
                )
            }
        }
    }
}
