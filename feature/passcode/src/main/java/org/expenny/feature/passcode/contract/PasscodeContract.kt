package org.expenny.feature.passcode.contract

import androidx.annotation.StringRes
import androidx.biometric.BiometricPrompt.CryptoObject
import org.expenny.core.common.utils.Constants
import org.expenny.core.common.models.StringResource
import org.expenny.core.resources.R
import org.expenny.feature.passcode.model.PasscodeType
import org.expenny.feature.passcode.model.PasscodeValidationResult

data class PasscodeState(
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

sealed interface PasscodeAction {
    class OnDigitClick(val digit: Int) : PasscodeAction
    class OnBiometricAuthenticationError(val error: String) : PasscodeAction
    data object OnBiometricAuthenticationSuccess : PasscodeAction
    data object OnBiometricClick : PasscodeAction
    data object OnBackspaceClick : PasscodeAction
    data object OnInfoDialogDismiss : PasscodeAction
    data object OnInfoClick : PasscodeAction
    data object OnBackClick : PasscodeAction
}

sealed interface PasscodeEvent {
    class ShowBiometricPrompt(val cryptoObject: CryptoObject) : PasscodeEvent
    class ShowMessage(val message: StringResource) : PasscodeEvent
    data object NavigateToDashboard : PasscodeEvent
    data object NavigateBack : PasscodeEvent
}