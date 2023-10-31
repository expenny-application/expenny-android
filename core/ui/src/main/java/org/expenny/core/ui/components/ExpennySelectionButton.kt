package org.expenny.core.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.expenny.core.ui.data.selection.SelectionType
import org.expenny.core.ui.foundation.ExpennyCheckBox
import org.expenny.core.ui.foundation.ExpennyRadioButton


@Composable
fun ExpennySelectionButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    type: SelectionType,
    onClick: (Boolean) -> Unit
) {
    when (type) {
        SelectionType.Single -> {
            ExpennyRadioButton(
                modifier = modifier,
                isSelected = isSelected,
                onClick = {
                    if (!isSelected) {
                        onClick(true)
                    }
                }
            )
        }
        SelectionType.Multi -> {
            ExpennyCheckBox(
                modifier = modifier,
                isChecked = isSelected,
                onClick = {
                    onClick(it)
                }
            )
        }
    }
}