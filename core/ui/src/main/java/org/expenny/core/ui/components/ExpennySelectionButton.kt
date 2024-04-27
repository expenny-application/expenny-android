package org.expenny.core.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.expenny.core.ui.data.SelectionType

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
                isChecked = isSelected,
                onClick = {
                    onClick(true)
                }
            )
        }
        SelectionType.Multi -> {
            ExpennyCheckbox(
                modifier = modifier,
                isChecked = isSelected,
                onClick = onClick
            )
        }
    }
}