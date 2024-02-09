package org.expenny.core.ui.foundation

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties

@Composable
fun ExpennyDialog(
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)?,
    content: @Composable (() -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    dismissButton: @Composable (() -> Unit)? = null,
    confirmButton: @Composable () -> Unit,
    dialogProperties: DialogProperties = DialogProperties(),
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        icon = icon,
        title = title,
        text = content,
        properties = dialogProperties,
        onDismissRequest = onDismissRequest,
        dismissButton = dismissButton,
        confirmButton = confirmButton,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        iconContentColor = MaterialTheme.colorScheme.primary,
        textContentColor = MaterialTheme.colorScheme.onSurface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        shape = MaterialTheme.shapes.medium,
    )
}