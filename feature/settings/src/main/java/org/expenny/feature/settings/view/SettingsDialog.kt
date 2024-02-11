package org.expenny.feature.settings.view

import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyActionsBottomSheet
import org.expenny.core.ui.components.ExpennyActionsBottomSheetItem
import org.expenny.core.ui.components.ExpennyTimePicker
import org.expenny.core.ui.data.selection.SelectionType
import org.expenny.core.ui.data.ui.ProfileUi
import org.expenny.core.ui.extensions.label
import org.expenny.core.ui.foundation.ExpennyDialog
import org.expenny.core.ui.foundation.ExpennyListDialog
import org.expenny.core.ui.foundation.ExpennyListDialogItem
import org.expenny.core.ui.foundation.ExpennySingleSelectionDialog
import org.expenny.core.ui.foundation.ExpennyTextButton
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
        is State.Dialog.ThemeDialog -> {
            if (state.selectedTheme != null) {
                ExpennySingleSelectionDialog(
                    label = stringResource(R.string.select_theme_label),
                    data = state.themes.map { Pair(it, it.label) },
                    selection = state.selectedTheme,
                    onSelect = { onDialogAction(Action.Dialog.OnThemeSelect(it)) },
                    onDismiss = { onDialogAction(Action.Dialog.OnDialogDismiss) }
                )
            }
        }
        is State.Dialog.LanguageDialog -> {
            ExpennySingleSelectionDialog(
                label = stringResource(R.string.select_language_label),
                data = state.languages.map { Pair(it, it.label) },
                selection = state.selectedLanguage,
                onSelect = { onDialogAction(Action.Dialog.OnLanguageSelect(it)) },
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
                profiles = state.profiles,
                selectedProfile = state.currentProfile,
                onSwitch = { onDialogAction(Action.Dialog.OnSwitchProfileClick(it)) },
                onDismiss = { onDialogAction(Action.Dialog.OnDialogDismiss) }
            )
        }
        is State.Dialog.DeleteApplicationDataDialog -> {
            DeleteApplicationDataDialog(
                onConfirm = { onDialogAction(Action.Dialog.OnDeleteApplicationDataDialogConfirm) },
                onDismiss = { onDialogAction(Action.Dialog.OnDialogDismiss) }
            )
        }
        is State.Dialog.DeleteProfileDataDialog -> {
            DeleteProfileDataDialog(
                onConfirm = { onDialogAction(Action.Dialog.OnDeleteProfileDataDialogConfirm) },
                onDismiss = { onDialogAction(Action.Dialog.OnDialogDismiss) }
            )
        }
        is State.Dialog.DeleteProfileDialog -> {
            DeleteProfileDialog(
                onConfirm = { onDialogAction(Action.Dialog.OnDeleteProfileDialogConfirm) },
                onDismiss = { onDialogAction(Action.Dialog.OnDialogDismiss) }
            )
        }
        else -> {}
    }
}

@Composable
private fun DeleteDialog(
    title: (@Composable () -> Unit)?,
    content: @Composable (() -> Unit)? = null,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    ExpennyDialog(
        onDismissRequest = onDismiss,
        title = title,
        content = content,
        confirmButton = {
            ExpennyTextButton(
                onClick = onConfirm,
                content = {
                    Text(text = stringResource(R.string.delete_button))
                }
            )
        },
        dismissButton = {
            ExpennyTextButton(
                onClick = onDismiss,
                content = {
                    Text(text = stringResource(R.string.cancel_button))
                }
            )
        }
    )
}

@Composable
private fun DeleteApplicationDataDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    DeleteDialog(
        onDismiss = onDismiss,
        onConfirm = onConfirm,
        title = {
            Text(text = stringResource(R.string.delete_application_data_question_label))
        },
        content = {
            Text(text = stringResource(R.string.delete_application_data_paragraph))
        }
    )
}

@Composable
private fun DeleteProfileDataDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    DeleteDialog(
        onDismiss = onDismiss,
        onConfirm = onConfirm,
        title = {
            Text(text = stringResource(R.string.delete_profile_data_question_label))
        },
        content = {
            Text(text = stringResource(R.string.delete_paragraph))
        }
    )
}

@Composable
private fun DeleteProfileDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    DeleteDialog(
        onDismiss = onDismiss,
        onConfirm = onConfirm,
        title = {
            Text(text = stringResource(R.string.delete_profile_question_label))
        },
        content = {
            Text(text = stringResource(R.string.delete_associated_data_paragraph))
        }
    )
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
private fun ProfileSelectionDialog(
    profiles: List<ProfileUi>,
    selectedProfile: ProfileUi?,
    onSwitch: (Long) -> Unit,
    onDismiss: () -> Unit,
) {
    ExpennyListDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.switch_profile_label))
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
            items(profiles) { profile ->
                ExpennyListDialogItem(
                    selectionType = SelectionType.Single,
                    isSelected = selectedProfile?.id == profile.id,
                    onClick = {
                        onSwitch(profile.id)
                    }
                ) {
                    Text(
                        text = profile.displayName,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }
        }
    )
}