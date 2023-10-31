package org.expenny.core.ui.foundation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun ExpennyCheckBox(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    isEnabled: Boolean = true,
    onClick: (Boolean) -> Unit,
) {
    Checkbox(
        modifier = modifier.size(24.dp),
        enabled = isEnabled,
        checked = isChecked,
        onCheckedChange = onClick,
    )
}