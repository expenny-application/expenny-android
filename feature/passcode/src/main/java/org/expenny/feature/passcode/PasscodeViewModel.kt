package org.expenny.feature.passcode

import androidx.biometric.BiometricPrompt.CryptoObject
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.expenny.core.common.utils.StringResource.Companion.fromRes
import org.expenny.core.common.utils.StringResource.Companion.fromStr
import org.expenny.core.common.viewmodel.ExpennyActionViewModel
import org.expenny.core.domain.repository.BiometricRepository
import org.expenny.core.domain.repository.LocalRepository
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
import org.expenny.feature.passcode.model.PasscodeType
import org.expenny.feature.passcode.model.PasscodeType.Confirm
import org.expenny.feature.passcode.model.PasscodeValidationResult
import org.expenny.feature.passcode.model.PasscodeValidationResult.Valid
import org.expenny.feature.passcode.model.PasscodeValidationResult.Invalid
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax

@HiltViewModel
class PasscodeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val localRepository: LocalRepository,
    private val biometricRepository: BiometricRepository,
) : ExpennyActionViewModel<Action>(), ContainerHost<State, Event> {

    private var validPasscode: String = ""
    private var cryptoObject: CryptoObject? = null

    override val container = container<State, Event>(
        initialState = State(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            launch { setupInitialState() }
            launch { subscribeToBiometricPreference() }
        }
    }

    private val state get() = container.stateFlow.value

    override fun onAction(action: Action) {
        when (action) {
            is Action.OnDigitClick -> handleOnDigitClick(action)
            is Action.OnBackspaceClick -> handleOnBackspaceClick()
            is Action.OnBackClick -> handleOnBackClick()
            is Action.OnBiometricAuthenticationSuccess -> handleOnBiometricAuthenticationSuccess()
            is Action.OnBiometricAuthenticationError -> handleOnBiometricAuthenticationError(action)
            is Action.OnBiometricClick -> handleOnBiometricClick()
        }
    }

    private fun handleOnBiometricAuthenticationSuccess() = intent {
        setPasscode(validPasscode)
        setPasscodeValidationResultWithPostDelay(Valid)
        postSideEffect(Event.NavigateToDashboard)
    }

    private fun handleOnBiometricAuthenticationError(action: Action.OnBiometricAuthenticationError) = intent {
        postSideEffect(Event.ShowMessage(fromStr(action.error)))
    }

    private fun handleOnBiometricClick() {
        if (state.passcodeValidationResult != Valid) {
            cryptoObject?.let {
                intent {
                    postSideEffect(Event.ShowBiometricPrompt(it))
                }
            }
        }
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(Event.NavigateBack)
    }

    private fun subscribeToBiometricPreference() = intent {
        localRepository.isBiometricEnrolled().collect { isBiometricEnrolled ->
            if (state.passcodeType == Unlock) {
                if (isBiometricEnrolled) {
                    cryptoObject = biometricRepository.createCryptoObject(CryptoPurpose.Encrypt)
                    reduce { state.copy(isBiometricEnabled = true) }
                } else {
                    cryptoObject = null
                    reduce { state.copy(isBiometricEnabled = false) }
                }
            }
        }
    }

    private fun setupInitialState() = intent {
        savedStateHandle.navArgs<PasscodeNavArgs>().also { args ->
            setPasscodeType(args.type)

            if (args.type == Unlock) {
                localRepository.getPasscode().first().also { storedPasscode ->
                    if (storedPasscode == null) { // this must never happen
                        postSideEffect(Event.ShowMessage(fromRes(R.string.internal_error)))
                        postSideEffect(Event.NavigateBack)
                    } else {
                        validPasscode = storedPasscode
                    }
                }
            }
        }
    }

    private fun handleOnBackspaceClick() = intent {
        if (state.passcode.isNotEmpty() && state.passcodeValidationResult == null) {
            setPasscode(state.passcode.dropLast(1))
        }
    }

    private fun handleOnDigitClick(action: Action.OnDigitClick) = intent {
        if (state.passcode.length < state.passcodeMaxLength && state.passcodeValidationResult != Valid) {
            val newPasscode = state.passcode + action.digit
            val newPasscodeStatus = getPasscodeStatus(newPasscode, state.passcodeType)

            setPasscode(newPasscode)
            setPasscodeValidationResultWithPostDelay(newPasscodeStatus)

            when (newPasscodeStatus) {
                Valid -> when (state.passcodeType) {
                    Create -> {
                        validPasscode = newPasscode
                        resetPasscode()
                        resetPasscodeValidationResult()
                        setPasscodeType(Confirm)
                    }
                    Confirm -> {
                        localRepository.setPasscode(state.passcode)
                        postSideEffect(Event.NavigateBack)
                    }
                    Unlock -> {
                        postSideEffect(Event.NavigateToDashboard)
                    }
                }
                Invalid -> {
                    resetPasscodeValidationResult()
                    resetPasscode()
                }
                else -> {
                    // ignore
                }
            }
        }
    }

    private suspend fun SimpleSyntax<State, Event>.setPasscodeValidationResultWithPostDelay(
        result: PasscodeValidationResult?,
    ) {
        reduce { state.copy(passcodeValidationResult = result) }
        // delay before either erasing invalid passcode or navigating to next screen if valid
        delay(300)
    }

    private suspend fun SimpleSyntax<State, Event>.resetPasscodeValidationResult() {
        reduce { state.copy(passcodeValidationResult = null) }
    }

    private suspend fun SimpleSyntax<State, Event>.resetPasscode() {
        reduce { state.copy(passcode = "") }
    }

    private suspend fun SimpleSyntax<State, Event>.setPasscode(passcode: String) {
        reduce { state.copy(passcode = passcode) }
    }

    private suspend fun SimpleSyntax<State, Event>.setPasscodeType(type: PasscodeType) {
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