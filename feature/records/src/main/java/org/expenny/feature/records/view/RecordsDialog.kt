package org.expenny.feature.records.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.expenny.core.common.types.DateRecurrence
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyActionsBottomSheet
import org.expenny.core.ui.components.ExpennyActionsBottomSheetItem
import org.expenny.core.ui.extensions.label
import org.expenny.core.ui.foundation.ExpennyAlertDialog
import org.expenny.core.ui.foundation.ExpennyAlertDialogButton
import org.expenny.core.ui.foundation.ExpennyMultiSelectionDialog
import org.expenny.core.ui.foundation.ExpennySingleSelectionDialog
import org.expenny.core.ui.foundation.ExpennyText
import org.expenny.feature.records.Action
import org.expenny.feature.records.model.RecordActionType
import org.expenny.feature.records.model.SelectionFilterDataUi
import org.expenny.feature.records.State
import org.expenny.feature.records.reducer.FilterSelectionsStateReducer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecordsDialog(
    dialog: State.Dialog?,
    scope: CoroutineScope,
    recordActionsDialogState: SheetState,
    dateRecurrence: DateRecurrence,
    selectionFilterData: SelectionFilterDataUi,
    filterSelection: FilterSelectionsStateReducer.State,
    onDialogAction: (Action.Dialog) -> Unit
) {
    when (dialog) {
        is State.Dialog.RecordActionsDialog -> {
            RecordActionsSheet(
                scope = scope,
                actionsSheetState = recordActionsDialogState,
                onActionSelect = { onDialogAction(Action.Dialog.OnRecordActionSelect(it)) },
                onDismiss = { onDialogAction(Action.Dialog.OnDialogDismiss) }
            )
        }
        is State.Dialog.DateRecurrenceDialog -> {
            ExpennySingleSelectionDialog(
                label = stringResource(R.string.date_recurrence_label),
                data = DateRecurrence.values().map { Pair(it, it.label) },
                selection = dateRecurrence,
                onSelect = { onDialogAction(Action.Dialog.OnDateRecurrenceSelect(it)) },
                onDismiss = { onDialogAction(Action.Dialog.OnDialogDismiss) }
            )
        }
        is State.Dialog.DeleteRecordDialog -> {
            DeleteRecordDialog(
                onConfirm = { onDialogAction(Action.Dialog.OnDeleteRecordDialogConfirm) },
                onDismiss = { onDialogAction(Action.Dialog.OnDialogDismiss) },
            )
        }
        is State.Dialog.RecordTypesDialog -> {
            ExpennyMultiSelectionDialog(
                label = stringResource(R.string.record_type_label),
                data = selectionFilterData.recordTypes.map { Pair(it, it.label) },
                selection = filterSelection.recordTypesSelection,
                onSelect = { onDialogAction(Action.Dialog.OnRecordTypesSelect(it)) },
                onDismiss = { onDialogAction(Action.Dialog.OnDialogDismiss) },
            )
        }
        is State.Dialog.AccountsDialog -> {
            ExpennyMultiSelectionDialog(
                label = stringResource(R.string.accounts_label),
                data = selectionFilterData.accounts,
                selection = filterSelection.accountsSelection,
                onSelect = { onDialogAction(Action.Dialog.OnAccountsSelect(it)) },
                onDismiss = { onDialogAction(Action.Dialog.OnDialogDismiss) },
            )
        }
        is State.Dialog.CategoriesDialog -> {
            ExpennyMultiSelectionDialog(
                label = stringResource(R.string.categories_label),
                data = selectionFilterData.categories,
                selection = filterSelection.categoriesSelection,
                onSelect = { onDialogAction(Action.Dialog.OnCategoriesSelect(it)) },
                onDismiss = { onDialogAction(Action.Dialog.OnDialogDismiss) },
            )
        }
        is State.Dialog.LabelsDialog -> {
            ExpennyMultiSelectionDialog(
                label = stringResource(R.string.labels_label),
                data = selectionFilterData.labels,
                selection = filterSelection.labelsSelection,
                onSelect = { onDialogAction(Action.Dialog.OnLabelsSelect(it)) },
                onDismiss = { onDialogAction(Action.Dialog.OnDialogDismiss) },
            )
        }
        else -> {}
    }
}

@Composable
internal fun DeleteRecordDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    ExpennyAlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            ExpennyText(text = stringResource(R.string.delete_selected_record_question_label))
        },
        content = {
            ExpennyText(
                text = stringResource(R.string.delete_record_paragraph),
                maxLines = Int.MAX_VALUE
            )
        },
        confirmButton = {
            ExpennyAlertDialogButton(
                label = stringResource(R.string.delete_button),
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            )
        },
        dismissButton = {
            ExpennyAlertDialogButton(
                label = stringResource(R.string.cancel_button),
                onClick = onDismiss
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecordActionsSheet(
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    actionsSheetState: SheetState,
    onActionSelect: (RecordActionType) -> Unit,
    onDismiss: () -> Unit
) {
    ExpennyActionsBottomSheet(
        modifier = modifier,
        sheetState = actionsSheetState,
        onDismiss = onDismiss
    ) {
        RecordActionType.values().forEach { action ->
            ExpennyActionsBottomSheetItem(
                label = action.label,
                icon = action.icon,
                onClick = {
                    scope.launch { actionsSheetState.hide() }.invokeOnCompletion {
                        if (!actionsSheetState.isVisible) onActionSelect(action)
                    }
                }
            )
        }
    }
}

private val RecordActionType.label: String
    @Composable
    get() = when (this) {
        RecordActionType.Select -> stringResource(R.string.select_label)
        RecordActionType.Clone -> stringResource(R.string.clone_label)
        RecordActionType.Edit -> stringResource(R.string.edit_label)
        RecordActionType.Delete -> stringResource(R.string.delete_label)
    }

private val RecordActionType.icon: Painter
    @Composable
    get() = when (this) {
        RecordActionType.Select -> painterResource(R.drawable.ic_check)
        RecordActionType.Clone -> painterResource(R.drawable.ic_copy)
        RecordActionType.Edit -> painterResource(R.drawable.ic_edit)
        RecordActionType.Delete -> painterResource(R.drawable.ic_delete)
    }