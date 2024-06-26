package org.expenny.feature.records.list.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.expenny.core.common.types.RecordActionType
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyBottomSheet
import org.expenny.core.ui.extensions.icon
import org.expenny.core.ui.extensions.label
import org.expenny.core.ui.components.ExpennyMultiSelectionDialog
import org.expenny.core.ui.components.ExpennySingleSelectionDialog
import org.expenny.core.ui.components.ExpennyDialog
import org.expenny.feature.records.list.contract.RecordsListAction
import org.expenny.feature.records.list.contract.RecordsListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecordsDialog(
    dialog: RecordsListState.Dialog?,
    scope: CoroutineScope,
    recordActionsDialogState: SheetState,
    onDialogAction: (RecordsListAction.Dialog) -> Unit
) {
    when (dialog) {
        is RecordsListState.Dialog.RecordActionsDialog -> {
            RecordActionsSheet(
                scope = scope,
                actionsSheetState = recordActionsDialogState,
                onActionSelect = { onDialogAction(RecordsListAction.Dialog.OnRecordActionSelect(it)) },
                onDismiss = { onDialogAction(RecordsListAction.Dialog.OnDialogDismiss) }
            )
        }
        is RecordsListState.Dialog.IntervalTypesDialog -> {
            ExpennySingleSelectionDialog(
                title = stringResource(R.string.interval_type_label),
                data = dialog.data,
                selection = dialog.selection,
                onSelectionChange = { onDialogAction(RecordsListAction.Dialog.OnIntervalTypeSelect(it)) },
                onDismiss = { onDialogAction(RecordsListAction.Dialog.OnDialogDismiss) }
            )
        }
        is RecordsListState.Dialog.DeleteRecordDialog -> {
            DeleteRecordDialog(
                onConfirm = { onDialogAction(RecordsListAction.Dialog.OnDeleteRecordDialogConfirm) },
                onDismiss = { onDialogAction(RecordsListAction.Dialog.OnDialogDismiss) },
            )
        }
        is RecordsListState.Dialog.RecordTypesDialog -> {
            ExpennyMultiSelectionDialog(
                title = stringResource(R.string.record_type_label),
                data = dialog.data,
                selection = dialog.selection,
                onSelectionChange = { onDialogAction(RecordsListAction.Dialog.OnRecordTypesSelect(it)) },
                onDismiss = { onDialogAction(RecordsListAction.Dialog.OnDialogDismiss) },
            )
        }
        is RecordsListState.Dialog.AccountsDialog -> {
            ExpennyMultiSelectionDialog(
                title = stringResource(R.string.accounts_label),
                data = dialog.data,
                selection = dialog.selection,
                onSelectionChange = { onDialogAction(RecordsListAction.Dialog.OnAccountsSelect(it)) },
                onDismiss = { onDialogAction(RecordsListAction.Dialog.OnDialogDismiss) },
            )
        }
        is RecordsListState.Dialog.CategoriesDialog -> {
            ExpennyMultiSelectionDialog(
                title = stringResource(R.string.categories_label),
                data = dialog.data,
                selection = dialog.selection,
                onSelectionChange = { onDialogAction(RecordsListAction.Dialog.OnCategoriesSelect(it)) },
                onDismiss = { onDialogAction(RecordsListAction.Dialog.OnDialogDismiss) },
            )
        }
        is RecordsListState.Dialog.LabelsDialog -> {
            ExpennyMultiSelectionDialog(
                title = stringResource(R.string.labels_label),
                data = dialog.data,
                selection = dialog.selection,
                onSelectionChange = { onDialogAction(RecordsListAction.Dialog.OnLabelsSelect(it)) },
                onDismiss = { onDialogAction(RecordsListAction.Dialog.OnDialogDismiss) },
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
    ExpennyDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            DialogTitle(text = stringResource(R.string.delete_selected_record_question_label))
        },
        body = {
            DialogBody(text = stringResource(R.string.delete_record_paragraph))
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecordActionsSheet(
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    actionsSheetState: SheetState,
    onActionSelect: (RecordActionType) -> Unit,
    onDismiss: () -> Unit
) {
    ExpennyBottomSheet(
        modifier = modifier,
        onDismiss = onDismiss,
        sheetState = actionsSheetState,
        actions = {
            RecordActionType.entries.forEach { action ->
                BottomSheetAction(
                    icon = action.icon,
                    text = action.label,
                    onClick = {
                        scope.launch { actionsSheetState.hide() }.invokeOnCompletion {
                            if (!actionsSheetState.isVisible) onActionSelect(action)
                        }
                    }
                )
            }
        }
    )
}

