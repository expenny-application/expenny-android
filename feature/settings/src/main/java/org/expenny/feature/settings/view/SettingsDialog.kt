package org.expenny.feature.settings.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.expenny.core.common.types.ProfileActionType
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyBottomSheet
import org.expenny.core.ui.components.ExpennyTimePicker
import org.expenny.core.ui.extensions.icon
import org.expenny.core.ui.extensions.label
import org.expenny.core.ui.components.ExpennyDeleteDialog
import org.expenny.core.ui.components.ExpennySingleSelectionDialog
import org.expenny.feature.settings.Action
import org.expenny.feature.settings.State

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsDialog(
    state: State,
    scope: CoroutineScope,
    profileActionsSheetState: SheetState,
    onDialogAction: (Action.Dialog) -> Unit
) {
    when (state.dialog) {
        is State.Dialog.ThemesSelectionDialog -> {
            ExpennySingleSelectionDialog(
                title = stringResource(R.string.select_theme_label),
                data = state.dialog.data,
                selection = state.dialog.selection,
                onSelectionChange = { onDialogAction(Action.Dialog.OnThemeSelect(it)) },
                onDismiss = { onDialogAction(Action.Dialog.OnDialogDismiss) }
            )
        }
        is State.Dialog.LanguagesSelectionDialog -> {
            ExpennySingleSelectionDialog(
                title = stringResource(R.string.select_language_label),
                data = state.dialog.data,
                selection = state.dialog.selection,
                onSelectionChange = { onDialogAction(Action.Dialog.OnLanguageSelect(it)) },
                onDismiss = { onDialogAction(Action.Dialog.OnDialogDismiss) }
            )
        }
        is State.Dialog.ReminderTimeDialog -> {
            ExpennyTimePicker(
                currentTime = state.reminderTime,
                onSelect = { onDialogAction(Action.Dialog.OnReminderTimeChange(it)) },
                onDismiss = { onDialogAction(Action.Dialog.OnDialogDismiss) }
            )
        }
        is State.Dialog.ProfileActionsDialog -> {
            ProfileActionsDialog(
                scope = scope,
                sheetState = profileActionsSheetState,
                actions = state.profileActions,
                onActionTypeSelect = { onDialogAction(Action.Dialog.OnProfileActionTypeSelect(it)) },
                onDismiss = { onDialogAction(Action.Dialog.OnDialogDismiss) }
            )
        }
        is State.Dialog.ProfileSelectionDialog -> {
            ExpennySingleSelectionDialog(
                title = stringResource(R.string.switch_profile_label),
                data = state.dialog.data,
                selection = state.dialog.selection,
                onSelectionChange = { onDialogAction(Action.Dialog.OnSwitchProfileClick(it)) },
                onDismiss = { onDialogAction(Action.Dialog.OnDialogDismiss) }
            )
        }
        is State.Dialog.DeleteApplicationDataDialog -> {
            ExpennyDeleteDialog(
                title = stringResource(R.string.delete_application_data_question_label),
                body = stringResource(R.string.delete_application_data_paragraph),
                onConfirm = { onDialogAction(Action.Dialog.OnDeleteApplicationDataDialogConfirm) },
                onDismiss = { onDialogAction(Action.Dialog.OnDialogDismiss) }
            )
        }
        is State.Dialog.DeleteProfileDataDialog -> {
            ExpennyDeleteDialog(
                title = stringResource(R.string.delete_profile_data_question_label),
                body = stringResource(R.string.delete_paragraph),
                onConfirm = { onDialogAction(Action.Dialog.OnDeleteProfileDataDialogConfirm) },
                onDismiss = { onDialogAction(Action.Dialog.OnDialogDismiss) }
            )
        }
        is State.Dialog.DeleteProfileDialog -> {
            ExpennyDeleteDialog(
                title = stringResource(R.string.delete_profile_question_label),
                body = stringResource(R.string.delete_associated_data_paragraph),
                onConfirm = { onDialogAction(Action.Dialog.OnDeleteProfileDialogConfirm) },
                onDismiss = { onDialogAction(Action.Dialog.OnDialogDismiss) }
            )
        }
        else -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileActionsDialog(
    scope: CoroutineScope,
    sheetState: SheetState,
    actions: List<ProfileActionType>,
    onActionTypeSelect: (ProfileActionType) -> Unit,
    onDismiss: () -> Unit
) {
    ExpennyBottomSheet(
        sheetState = sheetState,
        onDismiss = onDismiss,
        actions = {
            actions.forEach { actionType ->
                Action(
                    icon = actionType.icon,
                    text = actionType.label,
                    isSensitive = when (actionType) {
                        ProfileActionType.DeleteProfile,
                        ProfileActionType.DeleteProfileData -> true
                        else -> false
                    },
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            onDismiss()
                            onActionTypeSelect(actionType)
                        }
                    }
                )
            }
        }
    )
}
