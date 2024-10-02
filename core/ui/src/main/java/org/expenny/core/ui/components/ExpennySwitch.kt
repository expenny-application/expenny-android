package org.expenny.core.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.expenny.core.ui.foundation.transparent
import org.expenny.core.resources.R

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
            colors = SwitchDefaults.colors().copy(
                disabledUncheckedTrackColor = MaterialTheme.colorScheme.outline
                    .copy(alpha = 0.24f).compositeOver(MaterialTheme.colorScheme.surface),
                disabledUncheckedIconColor = MaterialTheme.colorScheme.outline,
                disabledUncheckedThumbColor = MaterialTheme.colorScheme.outline
                    .copy(alpha = 0.38f).compositeOver(MaterialTheme.colorScheme.surface),
                disabledUncheckedBorderColor = MaterialTheme.colorScheme.transparent,
                uncheckedTrackColor = MaterialTheme.colorScheme.outline,
                uncheckedIconColor = MaterialTheme.colorScheme.outline,
                uncheckedThumbColor = MaterialTheme.colorScheme.surfaceContainerLow,
                uncheckedBorderColor = MaterialTheme.colorScheme.transparent,
            ),
            thumbContent = {
                Icon(
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                    painter = painterResource(
                        if (isSelected) R.drawable.ic_check else R.drawable.ic_close
                    ),
                    contentDescription = null
                )
            },
            onCheckedChange = onClick
        )
    }
}