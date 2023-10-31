package org.expenny.core.ui.foundation

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ExpennyRadioButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onClick: (() -> Unit)?,
    isEnabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    RadioButton(
        modifier = modifier.size(24.dp),
        selected = isSelected,
        enabled = isEnabled,
        interactionSource = interactionSource,
        onClick = onClick,
    )
}

