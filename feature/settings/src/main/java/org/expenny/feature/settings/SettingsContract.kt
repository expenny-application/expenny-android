package org.expenny.feature.settings

import androidx.biometric.BiometricPrompt.CryptoObject
import org.expenny.core.common.types.ApplicationLanguage
import org.expenny.core.common.types.ApplicationTheme
import org.expenny.core.common.utils.StringResource
import org.expenny.core.ui.data.ui.ProfileUi
import org.expenny.feature.settings.model.SettingsItemType

data class State(
    val currentProfile: ProfileUi? = null,
    val languages: List<ApplicationLanguage> = ApplicationLanguage.values().toList(),
    val selectedLanguage: ApplicationLanguage = ApplicationLanguage.SystemDefault,
    val themes: List<ApplicationTheme> = ApplicationTheme.values().toList(),
    val selectedTheme: ApplicationTheme? = null,
    val isUsePasscodeSelected: Boolean = false,
    val isUseBiometricSelected: Boolean = false,
    val isBiometricAvailable: Boolean = false,
    val dialog: Dialog? = null,
) {
    val isBiometricEnabled: Boolean get() = isUsePasscodeSelected && isBiometricAvailable

    sealed interface Dialog {
        object ThemeDialog : Dialog
        object LanguageDialog : Dialog
    }
}

sealed interface Action {
    class OnThemeSelect(val theme: ApplicationTheme) : Action
    class OnLanguageSelect(val language: ApplicationLanguage) : Action
    class OnSettingsItemTypeClick(val type: SettingsItemType) : Action
    class OnBiometricAuthenticationError(val error: String) : Action
    object OnBiometricAuthenticationSuccess : Action
    object OnThemeDialogDismiss : Action
    object OnLanguageDialogDismiss : Action
    object OnBackClick : Action
}

sealed interface Event {
    class ShowMessage(val message: StringResource) : Event
    class ShowBiometricPrompt(val cryptoObject: CryptoObject) : Event
    object NavigateToCreatePasscode : Event
    object NavigateToSystemSecuritySettings : Event
    object NavigateToCurrencies : Event
    object NavigateToLabels : Event
    object NavigateBack : Event
}
