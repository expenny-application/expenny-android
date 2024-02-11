package org.expenny.core.ui.foundation

import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
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
            Text(text = label)
        },
        dismissButton = {
            ExpennyTextButton(
                onClick = onDismiss,
                content = {
                    Text(text = stringResource(R.string.cancel_button))
                }
            )
        },
        confirmButton = {
            ExpennyTextButton(
                onClick = { onSelect(currentSelection) },
                content = {
                    Text(text = stringResource(R.string.apply_button))
                }
            )
        },
        listContent = {
            item {
                ExpennyListDialogItem(
                    selectionType = SelectionType.Multi,
                    isSelected = currentSelection.isEmpty(),
                    onClick = {
                        currentSelection = emptyList()
                    }
                ) {
                    Text(text = stringResource(R.string.none_button))
                }
            }
            items(data) { item ->
                ExpennyListDialogItem(
                    selectionType = SelectionType.Multi,
                    isSelected = currentSelection.contains(item.first),
                    onClick = {
                        currentSelection = currentSelection.toggleItem(item.first)
                    }
                ) {
                    Text(text = item.second)
                }
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
            Text(text = label)
        },
        confirmButton = {
            ExpennyTextButton(
                onClick = onDismiss,
                content = {
                    Text(text = stringResource(R.string.cancel_button))
                }
            )
        },
        listContent = {
            items(data) { item ->
                ExpennyListDialogItem(
                    selectionType = SelectionType.Single,
                    isSelected = selection == item.first,
                    onClick = {
                        onSelect(item.first)
                    }
                ) {
                    Text(text = item.second)
                }
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
            Text(text = label)
        },
        dismissButton = {
            ExpennyTextButton(
                onClick = onDismiss,
                content = {
                    Text(text = stringResource(R.string.cancel_button))
                }
            )
        },
        confirmButton = {
            ExpennyTextButton(
                onClick = { onSelect(currentSelection) },
                content = {
                    Text(text = stringResource(R.string.apply_button))
                }
            )
        },
        listContent = {
            item {
                ExpennyListDialogItem(
                    selectionType = SelectionType.Single,
                    isSelected = currentSelection == null,
                    onClick = {
                        currentSelection = null
                    }
                ) {
                    Text(text = stringResource(R.string.none_button))
                }
            }
            items(data) { item ->
                ExpennyListDialogItem(
                    selectionType = SelectionType.Single,
                    isSelected = currentSelection == item.first,
                    onClick = {
                        currentSelection = item.first
                    }
                ) {
                    Text(text = item.second)
                }
            }
        }
    )
}