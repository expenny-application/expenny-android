package org.expenny.feature.settings.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyActionsBottomSheet
import org.expenny.core.ui.components.ExpennyActionsBottomSheetItem
import org.expenny.core.ui.components.ExpennyTimePicker
import org.expenny.core.ui.data.ui.ItemUi
import org.expenny.core.ui.data.ui.SingleSelectionUi
import org.expenny.core.ui.foundation.ExpennyDeleteDialog
import org.expenny.core.ui.foundation.ExpennyDialog
import org.expenny.feature.settings.Action
import org.expenny.feature.settings.State
import org.expenny.feature.settings.model.ProfileActionType

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
            ThemeSelectionDialog(
                themes = state.dialog.data,
                selectedTheme = state.dialog.selection,
                onSelectionChange = { onDialogAction(Action.Dialog.OnThemeSelect(it)) },
                onDismiss = { onDialogAction(Action.Dialog.OnDialogDismiss) }
            )
        }
        is State.Dialog.LanguagesSelectionDialog -> {
            LanguageSelectionDialog(
                languages = state.dialog.data,
                selectedLanguage = state.dialog.selection,
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
            ProfileSelectionDialog(
                profiles = state.dialog.data,
                selectedProfile = state.dialog.selection,
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
    ExpennyActionsBottomSheet(
        sheetState = sheetState,
        onDismiss = onDismiss,
    ) {
        actions.forEach { actionType ->
            ExpennyActionsBottomSheetItem(
                onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        onDismiss()
                        onActionTypeSelect(actionType)
                    }
                }
            ) {
                CompositionLocalProvider(LocalContentColor provides actionType.color) {
                    Icon(
                        painter = actionType.icon,
                        contentDescription = null
                    )
                    Text(text = actionType.label)
                }
            }
        }
    }
}

@Composable
private fun ThemeSelectionDialog(
    themes: List<ItemUi>,
    selectedTheme: SingleSelectionUi<Long>,
    onSelectionChange: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    ExpennyDialog(
        onDismissRequest = onDismiss,
        title = {
            DialogTitle(text = stringResource(R.string.select_theme_label))
        },
        rightButton = {
            DialogButton(
                label = stringResource(R.string.cancel_button),
                onClick = onDismiss
            )
        },
        list = {
            DialogList(
                data = themes,
                selection = selectedTheme,
                onSelectionChange = {
                    onSelectionChange(it.value)
                }
            )
        }
    )
}

@Composable
private fun LanguageSelectionDialog(
    languages: List<ItemUi>,
    selectedLanguage: SingleSelectionUi<Long>,
    onSelectionChange: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    ExpennyDialog(
        onDismissRequest = onDismiss,
        title = {
            DialogTitle(text = stringResource(R.string.select_language_label))
        },
        rightButton = {
            DialogButton(
                label = stringResource(R.string.cancel_button),
                onClick = onDismiss
            )
        },
        list = {
            DialogList(
                data = languages,
                selection = selectedLanguage,
                onSelectionChange = {
                    onSelectionChange(it.value)
                }
            )
        }
    )
}

@Composable
private fun ProfileSelectionDialog(
    profiles: List<ItemUi>,
    selectedProfile: SingleSelectionUi<Long>,
    onSelectionChange: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    ExpennyDialog(
        onDismissRequest = onDismiss,
        title = {
            DialogTitle(text = stringResource(R.string.switch_profile_label))
        },
        rightButton = {
            DialogButton(
                label = stringResource(R.string.cancel_button),
                onClick = onDismiss
            )
        },
        list = {
            DialogList(
                data = profiles,
                selection = selectedProfile,
                onSelectionChange = {
                    onSelectionChange(it.value)
                }
            )
        }
    )
}