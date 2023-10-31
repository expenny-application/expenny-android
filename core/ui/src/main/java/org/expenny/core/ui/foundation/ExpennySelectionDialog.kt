package org.expenny.core.ui.foundation

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.expenny.core.common.extensions.toggleItem
import org.expenny.core.resources.R
import org.expenny.core.ui.data.selection.SelectionType

@Composable
fun <T> ExpennyMultiSelectionDialog(
    modifier: Modifier = Modifier,
    label: String,
    data: List<Pair<T, String>>,
    selection: List<T>,
    onSelect: (List<T>) -> Unit,
    onDismiss: () -> Unit,
) {
    var currentSelection by rememberSaveable { mutableStateOf(selection) }

    ExpennyListDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            ExpennyText(text = label)
        },
        dismissButton = {
            ExpennyAlertDialogButton(
                label = stringResource(R.string.cancel_button),
                onClick = onDismiss
            )
        },
        confirmButton = {
            ExpennyAlertDialogButton(
                label = stringResource(R.string.apply_button),
                onClick = {
                    onSelect(currentSelection)
                }
            )
        },
        listContent = {
            item {
                ExpennyListDialogItem(
                    selectionType = SelectionType.Multi,
                    label = "None",
                    isSelected = currentSelection.isEmpty(),
                    onClick = {
                        currentSelection = emptyList()
                    }
                )
            }
            items(data) { item ->
                ExpennyListDialogItem(
                    selectionType = SelectionType.Multi,
                    label = item.second,
                    isSelected = currentSelection.contains(item.first),
                    onClick = {
                        currentSelection = currentSelection.toggleItem(item.first)
                    }
                )
            }
        }
    )
}

@Composable
fun <T> ExpennySingleSelectionDialog(
    modifier: Modifier = Modifier,
    label: String,
    data: List<Pair<T, String>>,
    selection: T,
    onSelect: (T) -> Unit,
    onDismiss: () -> Unit,
) {
    ExpennyListDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            ExpennyText(text = label)
        },
        confirmButton = {
            ExpennyAlertDialogButton(
                label = stringResource(R.string.cancel_button),
                onClick = onDismiss,
            )
        },
        listContent = {
            items(data) { item ->
                ExpennyListDialogItem(
                    selectionType = SelectionType.Single,
                    label = item.second,
                    isSelected = selection == item.first,
                    onClick = {
                        onSelect(item.first)
                    }
                )
            }
        }
    )
}

@Composable
fun <T> ExpennyOptionalSingleSelectionDialog(
    modifier: Modifier = Modifier,
    label: String,
    data: List<Pair<T, String>>,
    selection: T?,
    onSelect: (T?) -> Unit,
    onDismiss: () -> Unit,
) {
    var currentSelection by rememberSaveable { mutableStateOf(selection) }

    ExpennyListDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            ExpennyText(text = label)
        },
        dismissButton = {
            ExpennyAlertDialogButton(
                label = stringResource(R.string.cancel_button),
                onClick = onDismiss
            )
        },
        confirmButton = {
            ExpennyAlertDialogButton(
                label = stringResource(R.string.apply_button),
                onClick = {
                    onSelect(currentSelection)
                }
            )
        },
        listContent = {
            item {
                ExpennyListDialogItem(
                    selectionType = SelectionType.Single,
                    label = stringResource(R.string.none_button),
                    isSelected = currentSelection == null,
                    onClick = {
                        currentSelection = null
                    }
                )
            }
            items(data) { item ->
                ExpennyListDialogItem(
                    selectionType = SelectionType.Single,
                    label = item.second,
                    isSelected = currentSelection == item.first,
                    onClick = {
                        currentSelection = item.first
                    }
                )
            }
        }
    )
}