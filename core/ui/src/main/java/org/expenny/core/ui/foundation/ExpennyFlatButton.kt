package org.expenny.core.ui.foundation

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ExpennyFlatButton(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit,
    icon: (@Composable () -> Unit)? = null,
    label: @Composable () -> Unit
) {
    Button(
        modifier = modifier.sizeIn(
            minHeight = 56.dp,
            minWidth = 56.dp
        ),
        elevation = null,
        border = null,
        onClick = onClick,
        enabled = isEnabled,
        shape = MaterialTheme.shapes.small,
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(0.12f),
            disabledContentColor = MaterialTheme.colorScheme.primary.copy(0.38f),
        ),
        content = {
            ProvideTextStyle(value = MaterialTheme.typography.labelLarge) {
                if (icon != null) {
                    Box(modifier = Modifier.size(24.dp)) {
                        icon()
                    }
                }
                label()
            }
        }
    )
}