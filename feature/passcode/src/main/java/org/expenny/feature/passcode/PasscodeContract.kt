package org.expenny.feature.passcode

import androidx.annotation.StringRes
import androidx.biometric.BiometricPrompt.CryptoObject
import org.expenny.core.common.utils.Constants
import org.expenny.core.common.utils.StringResource
import org.expenny.core.resources.R
import org.expenny.feature.passcode.model.PasscodeType
import org.expenny.feature.passcode.model.PasscodeValidationResult

data class State(
    val passcodeType: PasscodeType = PasscodeType.Unlock,
    val passcodeValidationResult: PasscodeValidationResult? = null,
    val passcodeMaxLength: Int = Constants.DEFAULT_PASSCODE_LENGTH,
    val passcode: String = "",
    val isBiometricEnabled: Boolean = false,
    val showInfoDialog: Boolean = false
) {
    val isBackspaceEnabled: Boolean = passcode.isNotEmpty()
    val showToolbar: Boolean = passcodeType != PasscodeType.Unlock
    val showLogo: Boolean = passcodeType == PasscodeType.Unlock

    val passcodeParagraphResId: Int @StringRes get() = when (passcodeType) {
        PasscodeType.Unlock -> R.string.enter_passcode_label
        PasscodeType.Confirm -> R.string.confirm_passcode_label
        PasscodeType.Create -> R.string.create_passcode_label
    }
}

sealed interface Action {
    class OnDigitClick(val digit: Int) : Action
    class OnBiometricAuthenticationError(val error: String) : Action
    data object OnBiometricAuthenticationSuccess : Action
    data object OnBiometricClick : Action
    data object OnBackspaceClick : Action
    data object OnInfoDialogDismiss : Action
    data object OnInfoClick : Action
    data object OnBackClick : Action
}

sealed interface Event {
    class ShowBiometricPrompt(val cryptoObject: CryptoObject) : Event
    class ShowMessage(val message: StringResource) : Event
    data object NavigateToDashboard : Event
    data object NavigateBack : Event
}