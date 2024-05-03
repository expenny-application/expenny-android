@file:OptIn(OrbitExperimental::class)

package org.expenny.feature.accountdetails

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.expenny.core.common.extensions.setScaleNoRounding
import org.expenny.core.common.models.ErrorMessage
import org.expenny.core.common.models.StringResource.Companion.fromRes
import org.expenny.core.ui.base.*
import org.expenny.core.domain.usecase.ValidateInputUseCase
import org.expenny.core.domain.usecase.account.*
import org.expenny.core.domain.usecase.currency.GetCurrencyUseCase
import org.expenny.core.domain.usecase.currency.GetMainCurrencyUseCase
import org.expenny.core.domain.validators.*
import org.expenny.core.model.account.Account
import org.expenny.core.model.currency.Currency
import org.expenny.core.resources.R
import org.expenny.core.ui.mapper.CurrencyMapper
import org.expenny.core.ui.base.ExpennyViewModel
import org.expenny.feature.accountdetails.navigation.AccountDetailsNavArgs
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.simple.*
import org.orbitmvi.orbit.viewmodel.container
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class AccountDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMainCurrency: GetMainCurrencyUseCase,
    private val validateInput: ValidateInputUseCase,
    private val getAccount: GetAccountUseCase,
    private val createAccount: CreateAccountUseCase,
    private val updateAccount: UpdateAccountUseCase,
    private val deleteAccount: DeleteAccountUseCase,
    private val getCurrency: GetCurrencyUseCase,
    private val currencyMapper: CurrencyMapper
) : ExpennyViewModel<Action>(), ContainerHost<State, Event> {

    private val currentAccount = MutableStateFlow<Account?>(null)
    private val selectedCurrency = MutableStateFlow<Currency?>(null)

    override val container = container<State, Event>(
        initialState = State(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            launch { subscribeToSelectedCurrency() }
            launch { setupInitialState() }
        }
    }

    override fun onAction(action: Action) {
        when (action) {
            is Action.OnNameChange -> handleOnNameChange(action)
            is Action.OnTypeChange -> handleOnTypeChange(action)
            is Action.OnCurrencySelect -> handleOnCurrencyChange(action)
            is Action.OnStartBalanceChange -> handleOnStartBalanceChange(action)
            is Action.OnDescriptionChange -> handleOnDescriptionChange(action)
            is Action.OnAdditionsSectionVisibilityChange -> handleOnAdditionsSectionVisibilityChange(action)
            is Action.OnDeleteAccountDialogConfirm -> handleOnDeleteAccountDialogConfirm()
            is Action.OnDeleteAccountDialogDismiss -> handleOnDeleteAccountDialogDismiss()
            is Action.OnSelectCurrencyClick -> handleOnSelectCurrencyClick()
            is Action.OnSaveClick -> handleOnSaveClick()
            is Action.OnDeleteClick -> handleOnDeleteClick()
            is Action.OnBackClick -> handleOnBackClick()
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

    private fun handleOnTypeChange(action: Action.OnTypeChange) = intent {
        reduce {
            state.copy(selectedType = action.type)
        }
    }

    private fun handleOnCurrencyChange(action: Action.OnCurrencySelect) = intent {
        selectedCurrency.value = getCurrency(GetCurrencyUseCase.Params(action.id)).first()
    }

    private fun handleOnStartBalanceChange(action: Action.OnStartBalanceChange) = blockingIntent {
        reduce {
            state.copy(
                startBalanceInput = state.startBalanceInput.copy(
                    value = action.balance,
                    error = validateStartBalance(action.balance).errorRes
                )
            )
        }
    }

    private fun handleOnDescriptionChange(action: Action.OnDescriptionChange) = blockingIntent {
        reduce {
            state.copy(
                descriptionInput = state.descriptionInput.copy(
                    value = action.description,
                    error = validateDescription(action.description).errorRes
                )
            )
        }
    }

    private fun handleOnAdditionsSectionVisibilityChange(action: Action.OnAdditionsSectionVisibilityChange) = blockingIntent {
        reduce {
            state.copy(showAdditionsSection = action.isVisible)
        }
    }

    private fun handleOnSelectCurrencyClick() = intent {
        if (currentAccount.value == null) {
            postSideEffect(Event.NavigateToCurrenciesSelectionList(selectedCurrency.value?.id))
        }
    }

    private fun handleOnSaveClick() = intent {
        if (validateFields()) {
            if (currentAccount.value == null) {
                createAccount(
                    CreateAccountUseCase.Params(
                        currencyId = selectedCurrency.value!!.id,
                        name = state.nameInput.value,
                        type = state.selectedType,
                        description = state.descriptionInput.value,
                        startBalance = state.startBalanceInput.value
                    )
                )
            } else {
                updateAccount(
                    UpdateAccountUseCase.Params(
                        id = currentAccount.value!!.id,
                        currencyId = selectedCurrency.value!!.id,
                        name = state.nameInput.value,
                        type = state.selectedType,
                        description = state.descriptionInput.value,
                        startBalance = state.startBalanceInput.value
                    )
                )
            }
            postSideEffect(Event.ShowMessage(fromRes(R.string.saved_message)))
            postSideEffect(Event.NavigateBackToAccountsList)
        }
    }

    private fun handleOnDeleteClick() = blockingIntent {
        reduce { state.copy(showDeleteDialog = true) }
    }

    private fun handleOnDeleteAccountDialogConfirm() {
        intent {
            reduce { state.copy(showDeleteDialog = false) }

            deleteAccount(currentAccount.value!!.id)

            postSideEffect(Event.ShowMessage(fromRes(R.string.deleted_message)))
            postSideEffect(Event.NavigateBackToAccountsList)
        }
    }

    private fun handleOnDeleteAccountDialogDismiss() = blockingIntent {
        reduce { state.copy(showDeleteDialog = false) }
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(Event.NavigateBack)
    }

    private fun setupInitialState() = intent {
        savedStateHandle.navArgs<AccountDetailsNavArgs>().also { args ->
            if (args.accountId != null) {
                val account = getAccount(GetAccountUseCase.Params(args.accountId)).first()!!

                currentAccount.value = account
                selectedCurrency.value = account.currency

                reduce {
                    state.copy(
                        showDeleteButton = true,
                        toolbarTitle = fromRes(R.string.edit_account_label),
                        selectedType = account.type,
                        nameInput = state.nameInput.copy(
                            value = account.name
                        ),
                        currencyInput = state.currencyInput.copy(
                            value = currencyMapper(account.currency).preview,
                            isEnabled = false
                        ),
                        descriptionInput = state.descriptionInput.copy(
                            value = account.description
                        ),
                        startBalanceInput = state.startBalanceInput.copy(
                            value = account.startBalance.value
                        )
                    )
                }
            } else {
                reduce {
                    state.copy(
                        showDeleteButton = false,
                        toolbarTitle = fromRes(R.string.add_account_label),
                    )
                }
                selectedCurrency.value = getMainCurrency().first()
                postSideEffect(Event.RequestNameInputFocus)
            }
        }
    }

    private fun subscribeToSelectedCurrency() = intent {
        repeatOnSubscription {
            selectedCurrency.filterNotNull().collect {
                reduce {
                    state.copy(
                        selectedCurrency = it.unit.code,
                        currencyInput = state.currencyInput.copy(
                            value = currencyMapper(it).preview
                        ),
                        startBalanceInput = state.startBalanceInput.copy(
                            value = state.startBalanceInput.value.setScaleNoRounding(it.unit.scale)
                        )
                    )
                }
            }
        }
    }

    override fun onCoroutineException(message: ErrorMessage) {
        intent {
            postSideEffect(Event.ShowMessage(message.text))
        }
    }

    private fun validateFields(): Boolean {
        return with(container.stateFlow.value) {
            val nameValidationResult = validateName(nameInput.value)
            val currencyValidationResult = validateCurrency(currencyInput.value)
            val startBalanceValidationResult = validateStartBalance(startBalanceInput.value)
            val descriptionValidationResult = validateDescription(descriptionInput.value)

            intent {
                reduce {
                    state.copy(
                        nameInput = state.nameInput.copy(error = nameValidationResult.errorRes),
                        currencyInput = state.currencyInput.copy(error = currencyValidationResult.errorRes),
                        startBalanceInput = state.startBalanceInput.copy(error = startBalanceValidationResult.errorRes),
                        descriptionInput = state.descriptionInput.copy(error = descriptionValidationResult.errorRes),
                    )
                }
            }
            return@with listOf(
                nameValidationResult,
                currencyValidationResult,
                startBalanceValidationResult,
                descriptionValidationResult
            ).all { it.isValid }
        }
    }

    private fun validateName(value: String) = validateInput(
        value, listOf(RequiredStringValidator(), MinimumLengthValidator())
    )

    private fun validateCurrency(value: String) = validateInput(
        value, listOf(RequiredStringValidator())
    )

    private fun validateStartBalance(value: BigDecimal) = validateInput(
        value.toPlainString(), listOf(BigDecimalConstraintsValidator(min = BigDecimal.ZERO))
    )

    private fun validateDescription(value: String) = validateInput(
        value, listOf(MinimumLengthValidator(1))
    )
}