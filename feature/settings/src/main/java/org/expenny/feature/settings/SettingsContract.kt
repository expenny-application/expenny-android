package org.expenny.feature.settings

import org.expenny.core.common.types.ApplicationLanguage
import org.expenny.core.common.types.ApplicationTheme
import org.expenny.core.common.utils.Constants
import org.expenny.core.common.utils.Constants.DEFAULT_REMINDER_TIME_FORMAT
import org.expenny.core.common.utils.StringResource
import org.expenny.core.ui.data.ui.ProfileUi
import org.expenny.feature.settings.model.SettingsItemType
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class State(
    val currentProfile: ProfileUi? = null,
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
        object ReminderTimeDialog : Dialog
        object ThemeDialog : Dialog
        object LanguageDialog : Dialog
    }
}

sealed interface Action {
    class OnThemeSelect(val theme: ApplicationTheme) : Action
    class OnLanguageSelect(val language: ApplicationLanguage) : Action
    class OnSettingsItemTypeClick(val type: SettingsItemType) : Action
    class OnReminderTimeChange(val time: LocalTime) : Action
    object OnDialogDismiss : Action
    object OnBackClick : Action
}

sealed interface Event {
    class ShowMessage(val message: StringResource) : Event
    object NavigateToCreatePasscode : Event
    object NavigateToSystemSecuritySettings : Event
    object NavigateToSystemAlarmSettings : Event
    object NavigateToCurrencies : Event
    object NavigateToLabels : Event
    object NavigateBack : Event
}
