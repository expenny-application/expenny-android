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
import org.expenny.feature.profilesetup.contract.ProfileSetupAction
import org.expenny.feature.profilesetup.contract.ProfileSetupEvent
import org.expenny.feature.profilesetup.contract.ProfileSetupState
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
) : ExpennyViewModel<ProfileSetupAction>(), ContainerHost<ProfileSetupState, ProfileSetupEvent> {

    private val selectedCurrencyUnit: MutableStateFlow<CurrencyUnit?> = MutableStateFlow(null)

    override val container = container<ProfileSetupState, ProfileSetupEvent>(
        initialState = ProfileSetupState(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            setDefaultInputValues()
            launch { subscribeToSelectedCurrencyUnit() }
            launch { subscribeToIsFormValid() }
        }
    }

    private val state get() = container.stateFlow.value

    override fun onAction(action: ProfileSetupAction) {
        when (action) {
            is ProfileSetupAction.OnSetupCashBalanceCheckboxChange -> handleOnSetupCashBalanceCheckboxChange(action)
            is ProfileSetupAction.OnNameChange -> handleOnNameChange(action)
            is ProfileSetupAction.OnCurrencyUnitSelect -> handleOnCurrencyUnitSelect(action)
            is ProfileSetupAction.OnAccountNameChange -> handleOnAccountNameChange(action)
            is ProfileSetupAction.OnAccountBalanceChange -> handleOnAccountBalanceChange(action)
            is ProfileSetupAction.OnSelectCurrencyUnitClick -> handleOnSelectCurrencyUnitClick()
            is ProfileSetupAction.OnAbortDialogDismiss -> handleOnAbortDialogDismiss()
            is ProfileSetupAction.OnAbortDialogConfirm -> handleOnAbortDialogConfirm()
            is ProfileSetupAction.OnConfirmationDialogConfirm -> handleOnConfirmationDialogConfirm()
            is ProfileSetupAction.OnConfirmationDialogDismiss -> handleOnConfirmationDialogDismiss()
            is ProfileSetupAction.OnBackClick -> handleOnBackClick()
            is ProfileSetupAction.OnCtaClick -> handleOnCtaClick()
        }
    }

    override fun onCoroutineException(message: ErrorMessage) {
        intent {
            postSideEffect(ProfileSetupEvent.ShowMessage(message.text))
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
        postSideEffect(ProfileSetupEvent.NavigateToHome)
    }

    private fun handleOnAbortDialogConfirm() = intent {
        reduce { state.copy(showAbortDialog = false) }
        postSideEffect(ProfileSetupEvent.NavigateBack)
    }

    private fun handleOnSelectCurrencyUnitClick() = intent {
        postSideEffect(ProfileSetupEvent.NavigateToCurrencyUnitsSelectionList(selectedCurrencyUnit.value?.id))
    }

    private fun handleOnCurrencyUnitSelect(action: ProfileSetupAction.OnCurrencyUnitSelect) = intent {
        selectedCurrencyUnit.value = getCurrencyUnit(GetCurrencyUnitUseCase.Params(action.id))
    }

    private fun handleOnSetupCashBalanceCheckboxChange(action: ProfileSetupAction.OnSetupCashBalanceCheckboxChange) = intent {
        reduce {
            state.copy(
                setupCashBalanceCheckbox = state.setupCashBalanceCheckbox.copy(value = action.isChecked),
                showSetupCashBalanceInputs = action.isChecked
            )
        }
    }

    private fun handleOnNameChange(action: ProfileSetupAction.OnNameChange) = blockingIntent {
        reduce {
            state.copy(
                nameInput = state.nameInput.copy(
                    value = action.name,
                    error = validateName(action.name).errorRes
                )
            )
        }
    }

    private fun handleOnAccountNameChange(action: ProfileSetupAction.OnAccountNameChange) = blockingIntent {
        reduce {
            state.copy(
                accountNameInput = state.accountNameInput.copy(
                    value = action.accountName,
                    error = validateAccountName(action.accountName).errorRes
                )
            )
        }
    }

    private fun handleOnAccountBalanceChange(action: ProfileSetupAction.OnAccountBalanceChange) = blockingIntent {
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
            postSideEffect(ProfileSetupEvent.NavigateToHome)
        } else {
            reduce {
                state.copy(showConfirmationDialog = true)
            }
        }
    }

    private fun handleOnBackClick() = intent {
        if (state.selectCurrencyInput.value.isEmpty()) {
            postSideEffect(ProfileSetupEvent.NavigateBack)
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