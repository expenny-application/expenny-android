package org.expenny.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ExpennyLoadingContainer(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    color: Color = MaterialTheme.colorScheme.primary,
    content: (@Composable () -> Unit)? = null
) {
    if (isLoading) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = color)
        }
    } else {
        content?.invoke()
    }
}