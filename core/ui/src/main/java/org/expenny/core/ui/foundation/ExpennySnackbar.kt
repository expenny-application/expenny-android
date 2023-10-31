package org.expenny.core.ui.foundation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun ExpennySnackbar(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    onAction: () -> Unit = {}
) {
    SnackbarHost(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Bottom),
        hostState = snackbarHostState,
        snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(10.dp),
                containerColor = MaterialTheme.colorScheme.onSurface,
                contentColor = MaterialTheme.colorScheme.surface,
                actionContentColor = MaterialTheme.colorScheme.surface,
                dismissActionContentColor = MaterialTheme.colorScheme.surface,
                content = {
                    Text(
                        text = data.visuals.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.surface
                    )
                },
                action = {
                    data.visuals.actionLabel?.let {
                        TextButton(onClick = onAction) {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.surface
                            )
                        }
                    }
                }
            )
        }
    )
}