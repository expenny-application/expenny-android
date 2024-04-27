package org.expenny.core.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpennyRadioButton(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isChecked: Boolean,
    onClick: () -> Unit
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        RadioButton(
            modifier = modifier,
            enabled = isEnabled,
            selected = isChecked,
            onClick = onClick
        )
    }
}