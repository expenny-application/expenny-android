package org.expenny.core.ui.foundation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonAttributes
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonSize
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonType


@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
@Composable
fun ExpennyAlertDialog(
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
        onDismissRequest = onDismissRequest,
        properties = dialogProperties
    ) {
        AlertDialogContent(
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
fun ExpennyAlertDialogButton(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit,
) {
    ExpennyButton(
        modifier = modifier,
        onClick = onClick,
        attributes = ExpennyFlatButtonAttributes(
            type = ExpennyFlatButtonType.Tertiary,
            size = ExpennyFlatButtonSize.Medium,
            label = label
        )
    )
}

@Composable
private fun AlertDialogContent(
    buttons: @Composable () -> Unit,
    modifier: Modifier = Modifier,
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
