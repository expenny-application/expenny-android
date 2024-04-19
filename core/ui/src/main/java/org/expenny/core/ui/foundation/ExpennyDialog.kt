package org.expenny.core.ui.foundation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import org.expenny.core.common.extensions.addOrRemoveIfExist
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennySelectionButton
import org.expenny.core.ui.data.ui.ItemUi
import org.expenny.core.ui.data.ui.MultiSelectionUi
import org.expenny.core.ui.data.ui.SelectionUi
import org.expenny.core.ui.data.ui.SelectionType
import org.expenny.core.ui.data.ui.SingleSelectionUi
import org.expenny.core.ui.extensions.drawVerticalScrollbar
import org.expenny.core.ui.extensions.empty
import org.expenny.core.ui.extensions.isEmpty
import org.expenny.core.ui.extensions.type

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ExpennyDialog(
    modifier: Modifier = Modifier,
    icon: (@Composable ExpennyDialogScope.() -> Unit)? = null,
    title: @Composable ExpennyDialogScope.() -> Unit,
    body: (@Composable ExpennyDialogScope.() -> Unit)? = null,
    list: (@Composable ExpennyDialogScope.()-> Unit)? = null,
    leftButton: (@Composable ExpennyDialogScope.() -> Unit)? = null,
    rightButton: @Composable ExpennyDialogScope.() -> Unit,
    dialogProperties: DialogProperties = DialogProperties(),
    onDismissRequest: () -> Unit,
) {
    val scope = remember { ExpennyDialogScope() }

    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        properties = dialogProperties
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainer,
        ) {
            Column(
                modifier = Modifier.padding(PaddingValues(all = 24.dp)),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (icon != null) {
                    Box(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        icon(scope)
                    }
                }
                Box(
                    modifier = Modifier.align(
                        if (icon == null) Alignment.Start else Alignment.CenterHorizontally
                    )
                ) {
                    title(scope)
                }
                if (body != null) {
                    Box(
                        modifier = Modifier
                            .weight(weight = 1f, fill = false)
                            .align(Alignment.Start)
                    ) {
                        body(scope)
                    }
                }
                if (list != null) {
                    Box(
                        modifier = Modifier.weight(weight = 1f, fill = false)
                    ) {
                        list(scope)
                    }
                }
                Box(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        leftButton?.invoke(scope)
                        rightButton(scope)
                    }
                }
            }
        }
    }
}

@Composable
fun ExpennyDeleteDialog(
    title: String,
    body: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    ExpennyDialog(
        onDismissRequest = onDismiss,
        title = {
            DialogTitle(text = title)
        },
        body = {
            DialogBody(text = body)
        },
        rightButton = {
            DialogButton(
                onClick = onConfirm,
                label = stringResource(R.string.delete_button)
            )
        },
        leftButton = {
            DialogButton(
                onClick = onDismiss,
                label = stringResource(R.string.cancel_button)
            )
        }
    )
}

@Composable
fun ExpennySingleSelectionDialog(
    modifier: Modifier = Modifier,
    title: String,
    data: List<ItemUi>,
    selection: SingleSelectionUi<Long>,
    onSelectionChange: (SingleSelectionUi<Long>) -> Unit,
    onDismiss: () -> Unit
) {
    ExpennyDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            DialogTitle(text = title)
        },
        rightButton = {
            DialogButton(
                label = stringResource(R.string.cancel_button),
                onClick = onDismiss
            )
        },
        list = {
            DialogList(
                data = data,
                selection = selection,
                onSelectionChange = {
                    onSelectionChange(it)
                }
            )
        }
    )
}

@Composable
fun ExpennyMultiSelectionDialog(
    modifier: Modifier = Modifier,
    title: String,
    data: List<ItemUi>,
    selection: MultiSelectionUi<Long>,
    onSelectionChange: (MultiSelectionUi<Long>) -> Unit,
    onDismiss: () -> Unit
) {
    var localSelection by remember { mutableStateOf(selection) }

    ExpennyDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            DialogTitle(text = title)
        },
        rightButton = {
            DialogButton(
                label = stringResource(R.string.apply_button),
                onClick = { onSelectionChange(localSelection) },
            )
        },
        leftButton = {
            DialogButton(
                label = stringResource(R.string.cancel_button),
                onClick = onDismiss
            )
        },
        list = {
            DialogList(
                data = data,
                selection = localSelection,
                onSelectionChange = {
                    localSelection = it
                }
            )
        }
    )
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

@Stable
class ExpennyDialogScope {

    @Composable
    fun DialogIcon(
        modifier: Modifier = Modifier,
        painter: Painter,
    ) {
        Icon(
            modifier = modifier,
            painter = painter,
            contentDescription = stringResource(R.string.icon_a11y),
            tint = MaterialTheme.colorScheme.primary
        )
    }

    @Composable
    fun DialogButton(
        modifier: Modifier = Modifier,
        label: String,
        isEnabled: Boolean = true,
        onClick: () -> Unit,
    ) {
        TextButton(
            modifier = modifier,
            shape = MaterialTheme.shapes.small,
            enabled = isEnabled,
            onClick = onClick,
            colors = ButtonDefaults.textButtonColors().copy(
                contentColor = MaterialTheme.colorScheme.primary,
                disabledContentColor = MaterialTheme.colorScheme.primary.copy(0.38f)
            )
        ) {
            Text(text = label)
        }
    }

    @Composable
    fun DialogBody(
        modifier: Modifier = Modifier,
        text: String,
    ) {
        Text(
            modifier = modifier,
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }

    @Composable
    fun DialogTitle(
        modifier: Modifier = Modifier,
        text: String,
    ) {
        Text(
            modifier = modifier,
            text = text,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }

    @Composable
    inline fun <reified S : SelectionUi<Long>> DialogList(
        modifier: Modifier = Modifier,
        data: List<ItemUi>,
        selection: S,
        crossinline onSelectionChange: (S) -> Unit
    ) {
        val selectionType by rememberUpdatedState(selection.type)
        val handleOnSelectionChange: (Long) -> Unit = {
            val newSelection = when (selection) {
                is SingleSelectionUi<*> -> SingleSelectionUi(it)
                is MultiSelectionUi<*> -> MultiSelectionUi(selection.value.addOrRemoveIfExist(it))
                else -> throw IllegalArgumentException("Invalid selection type")
            }
            onSelectionChange(newSelection as S)
        }
        DialogList(modifier = modifier) {
            if (selection is MultiSelectionUi<*>) {
                item {
                    DialogListItem(
                        selectionType = selectionType,
                        isSelected = selection.isEmpty(),
                        label = stringResource(R.string.none_button),
                        onClick = {
                            onSelectionChange(selection.empty() as S)
                        }
                    )
                }
            }
            items(data) {
                DialogListItem(
                    selectionType = selectionType,
                    isSelected = selection.contains(it.id),
                    label = it.label,
                    onClick = {
                        handleOnSelectionChange(it.id)
                    }
                )
            }
        }
    }

    @Composable
    fun DialogList(
        modifier: Modifier = Modifier,
        content: LazyListScope.() -> Unit,
    ) {
        val lazyListState = rememberLazyListState()
        val configuration = LocalConfiguration.current
        val maxDialogHeightDp = LocalDensity.current.run {
            (configuration.screenHeightDp.dp.toPx() * 0.5).toInt().toDp()
        }
        LazyColumn(
            modifier = modifier
                .heightIn(max = maxDialogHeightDp)
                .drawVerticalScrollbar(lazyListState),
            state = lazyListState,
            content = content
        )
    }

    @Composable
    fun DialogListItem(
        modifier: Modifier = Modifier,
        selectionType: SelectionType,
        isSelected: Boolean,
        label: String,
        onClick: () -> Unit,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            ExpennySelectionButton(
                type = selectionType,
                isSelected = isSelected,
                onClick = { onClick() }
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
        }
    }
}