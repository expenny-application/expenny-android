package org.expenny.feature.passcode

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.expenny.core.common.utils.Constants
import org.expenny.core.common.models.StringResource.Companion.fromRes
import org.expenny.core.common.models.StringResource.Companion.fromStr
import org.expenny.core.ui.base.ExpennyViewModel
import org.expenny.core.domain.usecase.preferences.GetBiometricCryptoObjectUseCase
import org.expenny.core.domain.usecase.preferences.GetBiometricPreferenceUseCase
import org.expenny.core.domain.usecase.preferences.GetPasscodePreferenceUseCase
import org.expenny.core.domain.usecase.preferences.SetPasscodePreferenceUseCase
import org.expenny.core.model.biometric.CryptoPurpose
import org.expenny.feature.passcode.model.PasscodeType.Create
import org.expenny.feature.passcode.model.PasscodeType.Unlock
import org.expenny.feature.passcode.navigation.PasscodeNavArgs
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject
import org.expenny.core.resources.R
import org.expenny.feature.passcode.contract.PasscodeAction
import org.expenny.feature.passcode.contract.PasscodeEvent
import org.expenny.feature.passcode.contract.PasscodeState
import org.expenny.feature.passcode.model.PasscodeType
import org.expenny.feature.passcode.model.PasscodeType.Confirm
import org.expenny.feature.passcode.model.PasscodeValidationResult
import org.expenny.feature.passcode.model.PasscodeValidationResult.Valid
import org.expenny.feature.passcode.model.PasscodeValidationResult.Invalid
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax

@HiltViewModel
class PasscodeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getBiometricPreference: GetBiometricPreferenceUseCase,
    private val getBiometricCryptoObject: GetBiometricCryptoObjectUseCase,
    private val getPasscodePreference: GetPasscodePreferenceUseCase,
    private val setPasscodePreference: SetPasscodePreferenceUseCase,
) : ExpennyViewModel<PasscodeAction>(), ContainerHost<PasscodeState, PasscodeEvent> {

    private var validPasscode: String = ""
    private val state get() = container.stateFlow.value

    override val container = container<PasscodeState, PasscodeEvent>(
        initialState = PasscodeState(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            launch { setupInitialState() }
            launch { subscribeToBiometricPreference() }
        }
    }

    override fun onAction(action: PasscodeAction) {
        when (action) {
            is PasscodeAction.OnDigitClick -> handleOnDigitClick(action)
            is PasscodeAction.OnBackspaceClick -> handleOnBackspaceClick()
            is PasscodeAction.OnBiometricAuthenticationSuccess -> handleOnBiometricAuthenticationSuccess()
            is PasscodeAction.OnBiometricAuthenticationError -> handleOnBiometricAuthenticationError(action)
            is PasscodeAction.OnBiometricClick -> handleOnBiometricClick()
            is PasscodeAction.OnInfoDialogDismiss -> handleOnInfoDialogDismiss()
            is PasscodeAction.OnInfoClick -> handleOnInfoClick()
            is PasscodeAction.OnBackClick -> handleOnBackClick()
        }
    }

    private fun handleOnBiometricAuthenticationSuccess() = intent {
        setPasscodeField(validPasscode)
        setPasscodeValidationResultWithPostDelay(Valid)
        postSideEffect(PasscodeEvent.NavigateToDashboard)
    }

    private fun handleOnBiometricAuthenticationError(action: PasscodeAction.OnBiometricAuthenticationError) = intent {
        postSideEffect(PasscodeEvent.ShowMessage(fromStr(action.error)))
    }

    private fun handleOnBiometricClick() {
        if (state.passcodeValidationResult != Valid) {
            val cryptoObject = getBiometricCryptoObject(CryptoPurpose.Encrypt)
            intent {
                postSideEffect(PasscodeEvent.ShowBiometricPrompt(cryptoObject))
            }
        }
    }

    private fun handleOnInfoClick() = intent {
        reduce {
            state.copy(showInfoDialog = true)
        }
    }

    private fun handleOnInfoDialogDismiss() = intent {
        reduce {
            state.copy(showInfoDialog = false)
        }
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(PasscodeEvent.NavigateBack)
    }

    private fun subscribeToBiometricPreference() = intent {
        getBiometricPreference().collect { isBiometricEnrolled ->
            if (state.passcodeType == Unlock) {
                reduce { state.copy(isBiometricEnabled = isBiometricEnrolled) }
                if (isBiometricEnrolled) {
                    delay(Constants.DEFAULT_COMPOSITION_DELAY_MS)
                    handleOnBiometricClick()
                }
            }
        }
    }

    private fun setupInitialState() = intent {
        savedStateHandle.navArgs<PasscodeNavArgs>().also { args ->
            setPasscodeType(args.type)

            if (args.type == Unlock) {
                getPasscodePreference().first().also { storedPasscode ->
                    if (storedPasscode == null) { // this must never happen
                        postSideEffect(PasscodeEvent.ShowMessage(fromRes(R.string.internal_error)))
                        postSideEffect(PasscodeEvent.NavigateBack)
                    } else {
                        validPasscode = storedPasscode
                    }
                }
            }
        }
    }

    private fun handleOnBackspaceClick() = intent {
        if (state.passcode.isNotEmpty() && state.passcodeValidationResult == null) {
            setPasscodeField(state.passcode.dropLast(1))
        }
    }

    private fun handleOnDigitClick(action: PasscodeAction.OnDigitClick) = intent {
        if (state.passcode.length < state.passcodeMaxLength && state.passcodeValidationResult != Valid) {
            val newPasscode = state.passcode + action.digit
            val newPasscodeStatus = getPasscodeStatus(newPasscode, state.passcodeType)

            setPasscodeField(newPasscode)
            setPasscodeValidationResultWithPostDelay(newPasscodeStatus)

            when (newPasscodeStatus) {
                Valid -> when (state.passcodeType) {
                    Create -> {
                        validPasscode = newPasscode
                        resetPasscodeField()
                        resetPasscodeValidationResult()
                        setPasscodeType(Confirm)
                    }
                    Confirm -> {
                        setPasscodePreference(state.passcode)
                        postSideEffect(PasscodeEvent.NavigateBack)
                    }
                    Unlock -> {
                        postSideEffect(PasscodeEvent.NavigateToDashboard)
                    }
                }
                Invalid -> {
                    resetPasscodeValidationResult()
                    resetPasscodeField()
                }
                else -> {
                    // ignore
                }
            }
        }
    }

    private suspend fun SimpleSyntax<PasscodeState, PasscodeEvent>.setPasscodeValidationResultWithPostDelay(
        result: PasscodeValidationResult?,
    ) {
        reduce { state.copy(passcodeValidationResult = result) }
        // delay before either erasing invalid passcode or navigating to next screen if valid
        delay(300)
    }

    private suspend fun SimpleSyntax<PasscodeState, PasscodeEvent>.resetPasscodeValidationResult() {
        reduce { state.copy(passcodeValidationResult = null) }
    }

    private suspend fun SimpleSyntax<PasscodeState, PasscodeEvent>.resetPasscodeField() {
        reduce { state.copy(passcode = "") }
    }

    private suspend fun SimpleSyntax<PasscodeState, PasscodeEvent>.setPasscodeField(passcode: String) {
        reduce { state.copy(passcode = passcode) }
    }

    private suspend fun SimpleSyntax<PasscodeState, PasscodeEvent>.setPasscodeType(type: PasscodeType) {
        reduce { state.copy(passcodeType = type) }
    }

    private fun getPasscodeStatus(passcode: String, passcodeType: PasscodeType): PasscodeValidationResult? {
        return if (passcodeType == Create) {
            if (passcode.length == state.passcodeMaxLength) Valid else null
        } else {
            if (passcode.length == validPasscode.length) {
                if (passcode == validPasscode) Valid else Invalid
            } else {
                null
            }
        }
    }
}