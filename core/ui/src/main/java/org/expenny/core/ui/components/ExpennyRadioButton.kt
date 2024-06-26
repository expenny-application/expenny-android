package org.expenny.core.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import org.expenny.core.ui.base.BooleanPreviewParameterProvider
import org.expenny.core.ui.base.ExpennyPreview
import org.expenny.core.ui.foundation.ExpennyThemePreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpennyRadioButton(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        RadioButton(
            modifier = modifier,
            enabled = isEnabled,
            selected = isSelected,
            onClick = onClick
        )
    }
}

@ExpennyPreview
@Composable
private fun ExpennyRadioButtonPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) isEnabled: Boolean
) {
    ExpennyThemePreview {
        ExpennyRadioButton(
            isSelected = true,
            isEnabled = isEnabled,
            onClick = {}
        )
        ExpennyRadioButton(
            isSelected = false,
            isEnabled = isEnabled,
            onClick = {}
        )
    }
}