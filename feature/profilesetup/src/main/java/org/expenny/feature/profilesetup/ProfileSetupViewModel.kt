package org.expenny.feature.profilesetup

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.expenny.core.common.models.ErrorMessage
import org.expenny.core.common.models.StringResource.Companion.fromRes
import org.expenny.core.domain.usecase.profile.CreateProfileUseCase
import org.expenny.core.domain.usecase.ValidateInputUseCase
import org.expenny.core.domain.usecase.account.CreateInitialAccountUseCase
import org.expenny.core.domain.usecase.currencyunit.GetCurrencyUnitUseCase
import org.expenny.core.domain.validators.*
import org.expenny.core.model.currency.CurrencyUnit
import org.expenny.core.resources.R
import org.expenny.core.ui.mapper.CurrencyUnitMapper
import org.expenny.core.ui.base.ExpennyViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
internal class ProfileSetupViewModel @Inject constructor(
    private val validateInput: ValidateInputUseCase,
    private val createProfile: CreateProfileUseCase,
    private val createInitialAccount: CreateInitialAccountUseCase,
    private val getCurrencyUnit: GetCurrencyUnitUseCase,
    private val currencyUnitMapper: CurrencyUnitMapper,
) : ExpennyViewModel<Action>(), ContainerHost<State, Event> {

    private val selectedCurrencyUnit: MutableStateFlow<CurrencyUnit?> = MutableStateFlow(null)

    override val container = container<State, Event>(
        initialState = State(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            setDefaultInputValues()
            launch { subscribeToSelectedCurrencyUnit() }
            launch { subscribeToIsFormValid() }
        }
    }

    private val state get() = container.stateFlow.value

    override fun onAction(action: Action) {
        when (action) {
            is Action.OnSetupCashBalanceCheckboxChange -> handleOnSetupCashBalanceCheckboxChange(action)
            is Action.OnNameChange -> handleOnNameChange(action)
            is Action.OnCurrencyUnitSelect -> handleOnCurrencyUnitSelect(action)
            is Action.OnAccountNameChange -> handleOnAccountNameChange(action)
            is Action.OnAccountBalanceChange -> handleOnAccountBalanceChange(action)
            is Action.OnSelectCurrencyUnitClick -> handleOnSelectCurrencyUnitClick()
            is Action.OnAbortDialogDismiss -> handleOnAbortDialogDismiss()
            is Action.OnAbortDialogConfirm -> handleOnAbortDialogConfirm()
            is Action.OnConfirmationDialogConfirm -> handleOnConfirmationDialogConfirm()
            is Action.OnConfirmationDialogDismiss -> handleOnConfirmationDialogDismiss()
            is Action.OnBackClick -> handleOnBackClick()
            is Action.OnCtaClick -> handleOnCtaClick()
        }
    }

    override fun onCoroutineException(message: ErrorMessage) {
        intent {
            postSideEffect(Event.ShowMessage(message.text))
        }
    }

    private suspend fun setup() {
        createProfile(
            CreateProfileUseCase.Params(
                name = state.nameInput.value,
                currencyUnitId = selectedCurrencyUnit.value!!.id
            )
        )
        if (state.setupCashBalanceCheckbox.value) {
            createInitialAccount(
                CreateInitialAccountUseCase.Params(
                    name = state.accountNameInput.value,
                    startBalance = state.accountBalanceInput.value
                )
            )
        }
    }

    private fun setDefaultInputValues() = intent {
        reduce {
            state.copy(
                nameInput = state.nameInput.copy(value = provideString(fromRes(R.string.anonymous_label))),
                accountNameInput = state.accountNameInput.copy(value = provideString(fromRes(R.string.personal_wallet_label)))
            )
        }
    }

    private fun handleOnConfirmationDialogConfirm() = intent {
        reduce { state.copy(showConfirmationDialog = false) }
        setup()
        postSideEffect(Event.NavigateToHome)
    }

    private fun handleOnAbortDialogConfirm() = intent {
        reduce { state.copy(showAbortDialog = false) }
        postSideEffect(Event.NavigateBack)
    }

    private fun handleOnSelectCurrencyUnitClick() = intent {
        postSideEffect(Event.NavigateToCurrencyUnitsSelectionList(selectedCurrencyUnit.value?.id))
    }

    private fun handleOnCurrencyUnitSelect(action: Action.OnCurrencyUnitSelect) = intent {
        selectedCurrencyUnit.value = getCurrencyUnit(GetCurrencyUnitUseCase.Params(action.id))
    }

    private fun handleOnSetupCashBalanceCheckboxChange(action: Action.OnSetupCashBalanceCheckboxChange) = intent {
        reduce {
            state.copy(
                setupCashBalanceCheckbox = state.setupCashBalanceCheckbox.copy(value = action.isChecked),
                showSetupCashBalanceInputs = action.isChecked
            )
        }
    }

    private fun handleOnNameChange(action: Action.OnNameChange) = blockingIntent {
        reduce {
            state.copy(
                nameInput = state.nameInput.copy(
                    value = action.name,
                    error = validateName(action.name).errorRes
                )
            )
        }
    }

    private fun handleOnAccountNameChange(action: Action.OnAccountNameChange) = blockingIntent {
        reduce {
            state.copy(
                accountNameInput = state.accountNameInput.copy(
                    value = action.accountName,
                    error = validateAccountName(action.accountName).errorRes
                )
            )
        }
    }

    private fun handleOnAccountBalanceChange(action: Action.OnAccountBalanceChange) = blockingIntent {
        reduce {
            state.copy(
                accountBalanceInput = state.accountBalanceInput.copy(
                    value = action.accountBalance,
                    error = validateAccountBalance(action.accountBalance).errorRes
                )
            )
        }
    }

    private fun handleOnCtaClick() = intent {
        if (state.setupCashBalanceCheckbox.value) {
            setup()
            postSideEffect(Event.NavigateToHome)
        } else {
            reduce {
                state.copy(showConfirmationDialog = true)
            }
        }
    }

    private fun handleOnBackClick() = intent {
        if (state.selectCurrencyInput.value.isEmpty()) {
            postSideEffect(Event.NavigateBack)
        } else {
            reduce {
                state.copy(showAbortDialog = true)
            }
        }
    }

    private fun handleOnConfirmationDialogDismiss() = intent {
        reduce {
            state.copy(
                showConfirmationDialog = false,
                showSetupCashBalanceInputs = true,
                setupCashBalanceCheckbox = state.setupCashBalanceCheckbox.copy(value = true),
            )
        }
    }

    private fun handleOnAbortDialogDismiss() = intent {
        reduce { state.copy(showAbortDialog = false) }
    }

    private fun subscribeToSelectedCurrencyUnit() = intent {
        repeatOnSubscription {
            selectedCurrencyUnit
                .filterNotNull()
                .onEach {
                    reduce {
                        state.copy(
                            showSetupCashBalanceCheckbox = true,
                            selectedCurrency = it.code,
                            selectCurrencyInput = state.selectCurrencyInput.copy(
                                value = currencyUnitMapper(it).preview
                            ),
                            accountBalanceInput = state.accountBalanceInput.copy(
                                value = state.accountBalanceInput.value.setScale(it.scale)
                            )
                        )
                    }
                }.collect()
        }
    }

    private fun subscribeToIsFormValid() {
        fun isFormValid() = buildList {
            add(validateName(state.nameInput.value))
            add(validateCurrency(state.selectCurrencyInput.value))
            if (state.showSetupCashBalanceInputs) {
                add(validateAccountName(state.accountNameInput.value))
                add(validateAccountBalance(state.accountBalanceInput.value))
            }
        }.all { it.isValid }

        intent {
            repeatOnSubscription {
                container.stateFlow.filterNotNull()
                    .map { isFormValid() }
                    .distinctUntilChanged()
                    .onEach { isFormValid ->
                        reduce {
                            state.copy(isCtaEnabled = isFormValid)
                        }
                    }.launchIn(viewModelScope)
            }
        }
    }

    private fun validateCurrency(value: String): ValidationResult {
        return validateInput(value, listOf(RequiredStringValidator()))
    }

    private fun validateAccountBalance(value: BigDecimal): ValidationResult {
        return validateInput(value.toPlainString(), listOf(RequiredBigDecimalValidator()))
    }

    private fun validateAccountName(value: String): ValidationResult {
        return validateInput(value, listOf(RequiredStringValidator(), MinimumLengthValidator()))
    }

    private fun validateName(value: String): ValidationResult {
        return validateInput(value, listOf(RequiredStringValidator(), MinimumLengthValidator()))
    }
}