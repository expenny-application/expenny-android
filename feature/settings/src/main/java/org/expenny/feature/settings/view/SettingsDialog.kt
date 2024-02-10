package org.expenny.feature.settings.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.CoroutineScope
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyTimePicker
import org.expenny.core.ui.extensions.label
import org.expenny.core.ui.foundation.ExpennySingleSelectionDialog
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
            SettingsProfileActionsDialog(
                scope = scope,
                sheetState = profileActionsSheetState,
                onActionTypeSelect = { onDialogAction(Action.Dialog.OnProfileActionTypeSelect(it)) },
                onDismiss = { onDialogAction(Action.Dialog.OnDialogDismiss) }
            )
        }
        is State.Dialog.ProfileDialog -> {
            SettingsProfileSelectionDialog(
                profiles = state.profiles,
                selectedProfile = state.currentProfile,
                onSelect = { onDialogAction(Action.Dialog.OnSelectProfileClick(it)) },
                onCreate = { onDialogAction(Action.Dialog.OnCreateProfileClick) },
                onDismiss = { onDialogAction(Action.Dialog.OnDialogDismiss) }
            )
        }
        is State.Dialog.DeleteApplicationDataDialog -> {
            SettingsDeleteApplicationDataDialog(
                onConfirm = { onDialogAction(Action.Dialog.OnDeleteApplicationDataDialogConfirm) },
                onDismiss = { onDialogAction(Action.Dialog.OnDialogDismiss) }
            )
        }
        is State.Dialog.DeleteProfileDataDialog -> {
            SettingsDeleteProfileDataDialog(
                onConfirm = { onDialogAction(Action.Dialog.OnDeleteProfileDataDialogConfirm) },
                onDismiss = { onDialogAction(Action.Dialog.OnDialogDismiss) }
            )
        }
        is State.Dialog.DeleteProfileDialog -> {
            SettingsDeleteProfileDialog(
                onConfirm = { onDialogAction(Action.Dialog.OnDeleteProfileDialogConfirm) },
                onDismiss = { onDialogAction(Action.Dialog.OnDialogDismiss) }
            )
        }
        else -> {}
    }
}