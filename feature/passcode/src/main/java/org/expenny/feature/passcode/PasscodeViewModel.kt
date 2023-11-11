package org.expenny.feature.passcode

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.expenny.core.common.utils.StringResource.Companion.fromRes
import org.expenny.core.common.viewmodel.ExpennyActionViewModel
import org.expenny.core.domain.repository.LocalRepository
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
) : ExpennyActionViewModel<Action>(), ContainerHost<State, Event> {

    private var currentPasscode: String = ""
    private var validPasscode: String = ""

    override val container = container<State, Event>(
        initialState = State(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            launch { setupInitialState() }
        }
    }

    private val state get() = container.stateFlow.value

    override fun onAction(action: Action) {
        when (action) {
            is Action.OnDigitClick -> handleOnDigitClick(action)
            is Action.OnBackspaceClick -> handleOnBackspaceClick()
            is Action.OnBackClick -> handleOnBackClick()
            else -> {}
        }
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(Event.NavigateBack)
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
        if (state.passcodeLength > 0) {
            currentPasscode = currentPasscode.dropLast(1)
            reduce {
                state.copy(
                    passcodeLength = currentPasscode.length,
                    passcodeStatus = None
                )
            }
        }
    }

    private fun handleOnDigitClick(action: Action.OnDigitClick) = intent {
        if (state.passcodeLength < state.passcodeMaxLength) {
            currentPasscode += action.digit

            val passcodeStatus = getPasscodeStatus()

            reduce {
                state.copy(
                    passcodeLength = currentPasscode.length,
                    passcodeStatus = passcodeStatus
                )
            }

            if (passcodeStatus == Valid) {
                when (state.passcodeType) {
                    Create -> {
                        delay(200)
                        validPasscode = currentPasscode
                        currentPasscode = ""
                        reduce {
                            state.copy(
                                passcodeType = Confirm,
                                passcodeStatus = None,
                                passcodeLength = 0
                            )
                        }
                    }
                    Confirm -> {
                        localRepository.setPasscode(currentPasscode)
                        delay(200)
                        postSideEffect(Event.NavigateBack)
                    }
                    Verify -> {
                        delay(200)
                        postSideEffect(Event.NavigateToDashboard)
                    }
                }
            }
        }
    }

    private fun getPasscodeStatus(): PasscodeStatus {
        return if (state.passcodeType == Create) {
            if (currentPasscode.length == state.passcodeMaxLength) Valid else None
        } else {
            if (currentPasscode.length == validPasscode.length) {
                if (currentPasscode == validPasscode) Valid else Invalid
            } else {
                None
            }
        }
    }
}