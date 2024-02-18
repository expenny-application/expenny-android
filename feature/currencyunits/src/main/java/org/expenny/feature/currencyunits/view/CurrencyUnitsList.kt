package org.expenny.feature.currencyunits.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.expenny.core.ui.data.selection.SingleSelection
import org.expenny.core.ui.data.ui.CurrencyUnitUi
import org.expenny.core.ui.foundation.ExpennyCard
import org.expenny.core.ui.foundation.ExpennyGroupedVerticalList
import java.util.SortedMap

@OptIn(ExperimentalFoundationApi::class)
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
            CurrencyItemHeader(
                text = header,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .animateItemPlacement()
            )
        },
        listItem = { item ->
            CurrencyUnitItem(
                isSelected = selection.contains(item.id),
                code = item.code,
                name = item.name,
                onClick = { onCurrencyUnitClick(item.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItemPlacement(),
            )
        }
    )
}


@Composable
private fun CurrencyItemHeader(
    text: String,
    modifier: Modifier = Modifier,
    boxShape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
    backGroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge
) {
    Box(
        modifier = modifier.padding(vertical = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .background(color = backGroundColor, shape = boxShape)
                .border(border = border, shape = boxShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                color = contentColor,
                style = textStyle,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
    }
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