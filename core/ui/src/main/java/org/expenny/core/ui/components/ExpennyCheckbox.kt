package org.expenny.core.ui.components

import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import org.expenny.core.ui.base.BooleanPreviewParameterProvider
import org.expenny.core.ui.base.ExpennyPreview
import org.expenny.core.ui.foundation.ExpennyThemePreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpennyCheckbox(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    isEnabled: Boolean = true,
    onClick: (Boolean) -> Unit
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        Checkbox(
            modifier = modifier,
            checked = isChecked,
            enabled = isEnabled,
            onCheckedChange = {
                onClick(it)
            }
        )
    }
}

@ExpennyPreview
@Composable
private fun ExpennyCheckboxPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) isEnabled: Boolean
) {
    ExpennyThemePreview {
        ExpennyCheckbox(
            isChecked = true,
            isEnabled = isEnabled,
            onClick = {}
        )
        ExpennyCheckbox(
            isChecked = false,
            isEnabled = isEnabled,
            onClick = {}
        )
    }
}