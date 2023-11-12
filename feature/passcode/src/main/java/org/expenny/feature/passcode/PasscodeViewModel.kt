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
import org.expenny.feature.passcode.model.PasscodeType.Verify
import org.expenny.feature.passcode.navigation.PasscodeNavArgs
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject
import org.expenny.core.resources.R
import org.expenny.feature.passcode.model.PasscodeType.Confirm
import org.expenny.feature.passcode.model.PasscodeStatus
import org.expenny.feature.passcode.model.PasscodeStatus.None
import org.expenny.feature.passcode.model.PasscodeStatus.Valid
import org.expenny.feature.passcode.model.PasscodeStatus.Invalid

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
            is Action.OnBiometricAuthenticationSuccess -> {
                intent {
                    reduce {
                        state.copy(
                            passcode = validPasscode,
                            passcodeStatus = Valid
                        )
                    }
                    delay(200)
                    postSideEffect(Event.NavigateToDashboard)
                }
            }
            is Action.OnBiometricAuthenticationError -> {
                intent {
                    postSideEffect(Event.ShowMessage(fromStr(action.error)))
                }
            }
            is Action.OnBiometricClick -> handleOnBiometricClick()
            else -> {}
        }
    }

    private fun handleOnBiometricClick() {
        if (state.passcodeStatus != Valid) {
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
            if (state.passcodeType == Verify) {
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
            reduce { state.copy(passcodeType = args.type) }

            if (args.type == Verify) {
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
        if (state.passcode.isNotEmpty() && state.passcodeStatus != Valid) {
            reduce {
                state.copy(
                    passcode = state.passcode.dropLast(1),
                    passcodeStatus = None
                )
            }
        }
    }

    private fun handleOnDigitClick(action: Action.OnDigitClick) = intent {
        if (state.passcode.length < state.passcodeMaxLength && state.passcodeStatus != Valid) {
            val newPasscode = state.passcode + action.digit
            val newPasscodeStatus = getPasscodeStatus(newPasscode)
            reduce {
                state.copy(
                    passcode = newPasscode,
                    passcodeStatus = newPasscodeStatus
                )
            }
            when (newPasscodeStatus) {
                Valid -> when (state.passcodeType) {
                    Create -> {
                        delay(200)
                        validPasscode = state.passcode
                        reduce {
                            state.copy(
                                passcodeType = Confirm,
                                passcodeStatus = None,
                                passcode = ""
                            )
                        }
                    }
                    Confirm -> {
                        localRepository.setPasscode(state.passcode)
                        delay(200)
                        postSideEffect(Event.NavigateBack)
                    }
                    Verify -> {
                        delay(200)
                        postSideEffect(Event.NavigateToDashboard)
                    }
                }
                Invalid -> {
                    delay(200)
                    reduce {
                        state.copy(
                            passcodeStatus = None,
                            passcode = ""
                        )
                    }
                }
                else -> {}
            }
        }
    }

    private fun getPasscodeStatus(passcode: String): PasscodeStatus {
        return if (state.passcodeType == Create) {
            if (passcode.length == state.passcodeMaxLength) Valid else None
        } else {
            if (passcode.length == validPasscode.length) {
                if (passcode == validPasscode) Valid else Invalid
            } else {
                None
            }
        }
    }
}