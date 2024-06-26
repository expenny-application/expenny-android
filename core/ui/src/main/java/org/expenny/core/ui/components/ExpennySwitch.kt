package org.expenny.core.ui.components

import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpennySwitch(
    modifier: Modifier = Modifier,
    isEnabled: Boolean,
    isSelected: Boolean,
    onClick: (Boolean) -> Unit
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        Switch(
            modifier = modifier.sizeIn(
                maxHeight = 28.dp
            ),
            enabled = isEnabled,
            checked = isSelected,
            onCheckedChange = onClick
        )
    }
}