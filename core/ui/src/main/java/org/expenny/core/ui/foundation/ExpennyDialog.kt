package org.expenny.core.ui.foundation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
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
    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        properties = dialogProperties
    ) {
        DialogContent(
            buttons = {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    dismissButton?.invoke()
                    confirmButton()
                }
            },
            modifier = modifier,
            icon = icon,
            title = title,
            text = content,
        )
    }
}

@Composable
private fun DialogContent(
    modifier: Modifier = Modifier,
    buttons: @Composable () -> Unit,
    icon: (@Composable () -> Unit)?,
    title: (@Composable () -> Unit)?,
    text: @Composable (() -> Unit)?,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Column(
            modifier = Modifier.padding(PaddingValues(all = 24.dp))
        ) {
            icon?.let {
                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
                    Box(
                        modifier = Modifier
                            .padding(PaddingValues(bottom = 16.dp))
                            .align(Alignment.CenterHorizontally)
                    ) {
                        icon()
                    }
                }
            }
            title?.let {
                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
                    ProvideTextStyle(MaterialTheme.typography.titleLarge) {
                        Box(
                            modifier = Modifier
                                .padding(PaddingValues(bottom = 16.dp))
                                .align(
                                    if (icon == null) Alignment.Start
                                    else Alignment.CenterHorizontally
                                ),
                        ) {
                            title()
                        }
                    }
                }
            }
            text?.let {
                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
                    ProvideTextStyle(MaterialTheme.typography.bodyLarge) {
                        Box(
                            modifier = Modifier
                                .weight(weight = 1f, fill = false)
                                .padding(PaddingValues(bottom = 16.dp))
                                .align(Alignment.Start)
                        ) {
                            text()
                        }
                    }
                }
            }
            Box(modifier = Modifier.align(Alignment.End)) {
                buttons()
            }
        }
    }
}