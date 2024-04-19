package org.expenny.core.ui.components

import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import org.expenny.core.ui.data.ui.SelectionType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpennySelectionButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    type: SelectionType,
    onClick: (Boolean) -> Unit
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        when (type) {
            SelectionType.Single -> {
                RadioButton(
                    modifier = modifier,
                    selected = isSelected,
                    onClick = {
                        if (!isSelected) {
                            onClick(true)
                        }
                    }
                )
            }
            SelectionType.Multi -> {
                Checkbox(
                    modifier = modifier,
                    checked = isSelected,
                    onCheckedChange = {
                        onClick(it)
                    }
                )
            }
        }
    }
}