package org.expenny.feature.settings

import org.expenny.core.common.models.StringResource
import org.expenny.core.common.types.ApplicationLanguage
import org.expenny.core.common.types.ApplicationTheme
import org.expenny.core.common.types.ProfileActionType
import org.expenny.core.common.types.SettingsItemType
import org.expenny.core.common.utils.Constants.DEFAULT_REMINDER_TIME_FORMAT
import org.expenny.core.ui.data.ItemUi
import org.expenny.core.ui.data.ProfileUi
import org.expenny.core.ui.data.SingleSelectionUi
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class State(
    val profile: ProfileUi? = null,
    val theme: ApplicationTheme? = null,
    val language: ApplicationLanguage = ApplicationLanguage.SystemDefault,
    val profileActions: List<ProfileActionType> = ProfileActionType.values().toList(),
    val isUsePasscodeSelected: Boolean = false,
    val isUseBiometricSelected: Boolean = false,
    val isBiometricAvailable: Boolean = false,
    val isReminderSelected: Boolean = false,
    val isReminderTimeEnabled: Boolean = false,
    val reminderTime: LocalTime? = null,
    val dialog: Dialog? = null,
) {
    val reminderTimeString: String
        get() = reminderTime?.format(DateTimeFormatter.ofPattern(DEFAULT_REMINDER_TIME_FORMAT)).orEmpty()

    val isBiometricEnabled: Boolean
        get() = isUsePasscodeSelected && isBiometricAvailable

    sealed interface Dialog {
        data object DeleteApplicationDataDialog : Dialog
        data object DeleteProfileDataDialog : Dialog
        data object DeleteProfileDialog : Dialog
        data object ProfileActionsDialog : Dialog
        data object ReminderTimeDialog : Dialog

        data class ProfileSelectionDialog(
            val data: List<ItemUi<Long>>,
            val selection: SingleSelectionUi<Long>
        ) : Dialog

        data class ThemesSelectionDialog(
            val data: List<ItemUi<ApplicationTheme>>,
            val selection: SingleSelectionUi<ApplicationTheme>
        ) : Dialog

        data class LanguagesSelectionDialog(
            val data: List<ItemUi<ApplicationLanguage>>,
            val selection: SingleSelectionUi<ApplicationLanguage>
        ) : Dialog
    }
}

sealed interface Action {
    sealed interface Dialog : Action {
        class OnProfileActionTypeSelect(val type: ProfileActionType) : Dialog
        class OnSwitchProfileClick(val selection: SingleSelectionUi<Long>) : Dialog
        class OnThemeSelect(val selection: SingleSelectionUi<ApplicationTheme>) : Dialog
        class OnLanguageSelect(val selection: SingleSelectionUi<ApplicationLanguage>) : Dialog
        class OnReminderTimeChange(val time: LocalTime) : Dialog
        data object OnCreateProfileClick : Dialog
        data object OnDeleteApplicationDataDialogConfirm : Dialog
        data object OnDeleteProfileDataDialogConfirm : Dialog
        data object OnDeleteProfileDialogConfirm : Dialog
        data object OnDialogDismiss : Dialog
    }
    class OnSettingsItemTypeClick(val type: SettingsItemType) : Action
    data object OnBackClick : Action
}

sealed interface Event {
    class ShowMessage(val message: StringResource) : Event
    class RestartApplication(val isDataCleanupRequested: Boolean = false) : Event
    data object NavigateToCreateProfile : Event
    data object NavigateToCategoriesList : Event
    data object NavigateToCreatePasscode : Event
    data object NavigateToSystemSecuritySettings : Event
    data object NavigateToSystemAlarmSettings : Event
    data object NavigateToCurrencies : Event
    data object NavigateBack : Event
}
