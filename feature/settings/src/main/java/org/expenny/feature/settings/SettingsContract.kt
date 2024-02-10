package org.expenny.feature.settings

import org.expenny.core.common.types.ApplicationLanguage
import org.expenny.core.common.types.ApplicationTheme
import org.expenny.core.common.utils.Constants.DEFAULT_REMINDER_TIME_FORMAT
import org.expenny.core.common.utils.StringResource
import org.expenny.core.ui.data.ui.ProfileUi
import org.expenny.feature.settings.model.ProfileActionType
import org.expenny.feature.settings.model.SettingsItemType
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class State(
    val currentProfile: ProfileUi? = null,
    val profiles: List<ProfileUi> = emptyList(),
    val languages: List<ApplicationLanguage> = ApplicationLanguage.values().toList(),
    val selectedLanguage: ApplicationLanguage = ApplicationLanguage.SystemDefault,
    val themes: List<ApplicationTheme> = ApplicationTheme.values().toList(),
    val selectedTheme: ApplicationTheme? = null,
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
        data object ProfileDialog : Dialog
        data object ProfileActionsDialog : Dialog
        data object ReminderTimeDialog : Dialog
        data object ThemeDialog : Dialog
        data object LanguageDialog : Dialog
    }
}

sealed interface Action {
    sealed interface Dialog : Action {
        class OnProfileActionTypeSelect(val type: ProfileActionType) : Dialog
        class OnSelectProfileClick(val profileId: Long) : Dialog
        class OnThemeSelect(val theme: ApplicationTheme) : Dialog
        class OnLanguageSelect(val language: ApplicationLanguage) : Dialog
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
