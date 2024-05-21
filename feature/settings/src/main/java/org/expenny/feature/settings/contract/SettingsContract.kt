package org.expenny.feature.settings.contract

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

data class SettingsState(
    val profile: ProfileUi? = null,
    val theme: ApplicationTheme? = null,
    val language: ApplicationLanguage = ApplicationLanguage.SystemDefault,
    val profileActions: List<ProfileActionType> = ProfileActionType.entries,
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

sealed interface SettingsAction {
    sealed interface Dialog : SettingsAction {
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
    class OnSettingsItemTypeClick(val type: SettingsItemType) : SettingsAction
    data object OnBackClick : SettingsAction
}

sealed interface SettingsEvent {
    class ShowMessage(val message: StringResource) : SettingsEvent
    class RestartApplication(val isDataCleanupRequested: Boolean = false) : SettingsEvent
    data object NavigateToCreateProfile : SettingsEvent
    data object NavigateToCategoriesList : SettingsEvent
    data object NavigateToCreatePasscode : SettingsEvent
    data object NavigateToSystemSecuritySettings : SettingsEvent
    data object NavigateToSystemAlarmSettings : SettingsEvent
    data object NavigateToCurrencies : SettingsEvent
    data object NavigateBack : SettingsEvent
}
