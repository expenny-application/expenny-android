package org.expenny.feature.labels.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.expenny.core.ui.components.ExpennySelectionButton
import org.expenny.core.ui.data.selection.MultiSelection
import org.expenny.core.ui.data.selection.SelectionType
import org.expenny.core.ui.data.ui.LabelUi
import org.expenny.core.ui.foundation.ExpennyText
import org.expenny.core.ui.foundation.ExpennyVerticalList
import org.expenny.core.ui.foundation.ExpennyCard
import org.expenny.core.resources.R
import org.expenny.core.ui.extensions.type

@Composable
internal fun LabelsList(
    modifier: Modifier = Modifier,
    selection: MultiSelection<Long>?,
    labels: List<LabelUi>,
    onLabelClick: (Long) -> Unit,
) {
    ExpennyVerticalList(
        modifier = modifier,
        list = labels,
        listItemKey = LabelUi::id,
        listItem = { item ->
            LabelItem(
                modifier = Modifier.fillMaxWidth(),
                selectionType = selection?.type,
                selected = selection?.contains(item.id) ?: false,
                label = item,
                onClick = {
                    onLabelClick(item.id)
                }
            )
        }
    )
}

@Composable
private fun LabelItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    selectionType: SelectionType?,
    label: LabelUi,
    onClick: () -> Unit
) {
    ExpennyCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_label),
                tint = label.color,
                contentDescription = null
            )
            ExpennyText(
                modifier = Modifier.weight(1f),
                text = label.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (selectionType != null) {
                ExpennySelectionButton(
                    isSelected = selected,
                    type = selectionType,
                    onClick = {
                        onClick()
                    }
                )
            }
        }
    }
}