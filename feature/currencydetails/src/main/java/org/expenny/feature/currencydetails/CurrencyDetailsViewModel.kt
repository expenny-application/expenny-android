package org.expenny.feature.currencydetails

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.expenny.core.common.extensions.invert
import org.expenny.core.common.utils.StringResource.Companion.fromRes
import org.expenny.core.common.viewmodel.*
import org.expenny.core.domain.usecase.ValidateInputUseCase
import org.expenny.core.domain.usecase.currency.CreateCurrencyUseCase
import org.expenny.core.domain.usecase.currency.DeleteCurrencyUseCase
import org.expenny.core.domain.usecase.currency.GetCurrencyUseCase
import org.expenny.core.domain.usecase.currency.GetMainCurrencyUseCase
import org.expenny.core.domain.usecase.currency.UpdateCurrencyUseCase
import org.expenny.core.domain.usecase.currencyunit.GetCurrencyUnitUseCase
import org.expenny.core.domain.usecase.rate.GetLatestCurrencyRateUseCase
import org.expenny.core.domain.validators.RequiredBigDecimalValidator
import org.expenny.core.domain.validators.RequiredStringValidator
import org.expenny.core.model.currency.Currency
import org.expenny.core.model.currency.CurrencyUnit
import org.expenny.core.model.resource.ResourceResult
import org.expenny.core.resources.R
import org.expenny.core.ui.mapper.CurrencyUnitMapper
import org.expenny.feature.currencydetails.navigation.CurrencyDetailsNavArgs
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class CurrencyDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMainCurrency: GetMainCurrencyUseCase,
    private val getCurrency: GetCurrencyUseCase,
    private val getCurrencyUnit: GetCurrencyUnitUseCase,
    private val validateInput: ValidateInputUseCase,
    private val currencyUnitMapper: CurrencyUnitMapper,
    private val createCurrency: CreateCurrencyUseCase,
    private val updateCurrency: UpdateCurrencyUseCase,
    private val deleteCurrency: DeleteCurrencyUseCase,
    private val getLatestCurrencyRate: GetLatestCurrencyRateUseCase,
) : ExpennyActionViewModel<Action>(), ContainerHost<State, Event> {

    private val currentCurrency =  MutableStateFlow<Currency?>(null)
    private val mainCurrency = MutableStateFlow<Currency?>(null)
    private val selectedCurrencyUnit = MutableStateFlow<CurrencyUnit?>(null)

    override val container = container<State, Event>(
        initialState = State(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            setNavArgs()
            launch { subscribeOnSelectedCurrencyUnit() }
        }
    }

    private val state get() = container.stateFlow.value

    private fun setNavArgs() {
        savedStateHandle.navArgs<CurrencyDetailsNavArgs>().let { navArgs ->
            if (navArgs.currencyId != null) {
                intent {
                    currentCurrency.value = getCurrency(GetCurrencyUseCase.Params(navArgs.currencyId)).first()!!
                    mainCurrency.value = getMainCurrency().first()!!
                    selectedCurrencyUnit.value = currentCurrency.value!!.unit
                    val isSubscribedToRateUpdates = currentCurrency.value!!.isSubscribedToRateUpdates

                    reduce {
                        state.copy(
                            currencyUnitInput = state.currencyUnitInput.copy(
                                value = currencyUnitMapper(selectedCurrencyUnit.value!!).preview,
                                enabled = false
                            ),
                            baseCurrency = mainCurrency.value!!.unit.code,
                            quoteCurrency = selectedCurrencyUnit.value!!.code,
                            toolbarTitle = fromRes(R.string.edit_currency_label),
                            subscribeToRatesUpdates = isSubscribedToRateUpdates,
                            showSubscribeToRatesUpdatesCheckbox = true,
                            showRatesDisclaimerMessage = false,
                            showRatesInputFields = true,
                            showDeleteButton = true,
                        )
                    }
                    reduceBaseToQuoteRateInput(currentCurrency.value!!.baseToQuoteRate)
                    reduceQuoteToBaseRateInput(currentCurrency.value!!.quoteToBaseRate)
                }
            } else {
                intent {
                    reduce {
                        state.copy(
                            toolbarTitle = fromRes(R.string.add_currency_label),
                            showRatesDisclaimerMessage = true,
                            showRatesInputFields = false,
                            showDeleteButton = false,
                        )
                    }
                }
            }
        }
    }

    override fun onAction(action: Action) {
        when (action) {
            is Action.OnCurrencyUnitSelect -> handleOnCurrencyUnitSelect(action)
            is Action.OnSelectCurrencyUnitClick -> handleOnSelectCurrencyUnitClick()
            is Action.OnDeleteClick -> handleOnDeleteClick()
            is Action.OnDeleteCurrencyDialogConfirm -> handleOnDeleteCurrencyDialogConfirm()
            is Action.OnDeleteCurrencyDialogDismiss -> handleOnDeleteCurrencyDialogDismiss()
            is Action.OnSaveClick -> handleOnSaveClick()
            is Action.OnBaseToQuoteRateChange -> handleOnBaseToQuoteRateChange(action)
            is Action.OnQuoteToBaseRateChange -> handleOnQuoteToBaseRateChange(action)
            is Action.OnSubscribeToRatesUpdateCheckboxChange -> handleOnEnableRatesUpdateCheckboxChange(action)
            is Action.OnBackClick -> handleOnBackClick()
        }
    }

    private fun handleOnEnableRatesUpdateCheckboxChange(action: Action.OnSubscribeToRatesUpdateCheckboxChange) = blockingIntent {
        reduce {
            state.copy(subscribeToRatesUpdates = action.value)
        }
    }

    private fun handleOnBaseToQuoteRateChange(action: Action.OnBaseToQuoteRateChange) = blockingIntent {
        reduce {
            state.copy(
                baseToQuoteRateInput = state.baseToQuoteRateInput.copy(
                    value = action.rate,
                    error = validateRate(action.rate).errorRes
                )
            )
        }
        reduceQuoteToBaseRateInput(state.baseToQuoteRateInput.value.invert())
    }

    private fun handleOnQuoteToBaseRateChange(action: Action.OnQuoteToBaseRateChange) = blockingIntent {
        reduce {
            state.copy(
                quoteToBaseRateInputField = state.quoteToBaseRateInputField.copy(
                    value = action.rate,
                    error = validateRate(action.rate).errorRes
                )
            )
        }
        reduceBaseToQuoteRateInput(state.quoteToBaseRateInputField.value.invert())
    }

    private fun handleOnCurrencyUnitSelect(action: Action.OnCurrencyUnitSelect) {
        intent {
            selectedCurrencyUnit.value = getCurrencyUnit(GetCurrencyUnitUseCase.Params(action.id))!!

            if (mainCurrency.value == null) {
                mainCurrency.value = getMainCurrency().first()!!
            }

            reduce {
                state.copy(
                    baseCurrency = mainCurrency.value!!.unit.code,
                    quoteCurrency = selectedCurrencyUnit.value!!.code
                )
            }

            getLatestCurrencyRate(
                base = mainCurrency.value!!.unit.code,
                quote = selectedCurrencyUnit.value!!.code
            ).collect { result ->
                when (result) {
                    is ResourceResult.Loading -> {
                        reduce { state.copy(isLoading = true) }
                    }
                    is ResourceResult.Error -> {
                        reduce {
                            state.copy(
                                isLoading = false,
                                showRatesInputFields = true,
                                showSubscribeToRatesUpdatesCheckbox = false,
                                subscribeToRatesUpdates = false,
                            )
                        }

                        // Setting main currency base/quote rate which is 1
                        val defaultRate = mainCurrency.value!!.baseToQuoteRate
                        reduceBaseToQuoteRateInput(defaultRate)
                        reduceQuoteToBaseRateInput(defaultRate)

                        postSideEffect(Event.ShowMessage(fromRes(R.string.remote_rate_error)))
                    }
                    is ResourceResult.Success -> {
                        reduce {
                            state.copy(
                                isLoading = false,
                                showRatesInputFields = true,
                                showSubscribeToRatesUpdatesCheckbox = true,
                            )
                        }

                        val baseToQuoteRate = result.data!!.rate
                        val quoteToBaseRate = baseToQuoteRate.invert()
                        reduceBaseToQuoteRateInput(baseToQuoteRate)
                        reduceQuoteToBaseRateInput(quoteToBaseRate)
                    }
                }
            }
        }
    }

    private fun handleOnSaveClick() = intent {
        if (validateFields()) {
            if (currentCurrency.value == null) {
                createCurrency(
                    CreateCurrencyUseCase.Params(
                        currencyUnitId = selectedCurrencyUnit.value!!.id,
                        quoteToBaseRate = state.quoteToBaseRateInputField.value,
                        isSubscribedToRateUpdates = state.subscribeToRatesUpdates
                    )
                )
            } else {
                updateCurrency(
                    UpdateCurrencyUseCase.Params(
                        id = currentCurrency.value!!.id,
                        quoteToBaseRate = state.quoteToBaseRateInputField.value,
                        isSubscribedToRateUpdates = state.subscribeToRatesUpdates
                    )
                )
            }
            postSideEffect(Event.ShowMessage(fromRes(R.string.saved_message)))
            postSideEffect(Event.NavigateBack)
        }
    }

    private fun handleOnDeleteCurrencyDialogConfirm() = intent {
        reduce {
            state.copy(showDeleteDialog = false)
        }
        deleteCurrency(currentCurrency.value!!.id)
        postSideEffect(Event.ShowMessage(fromRes(R.string.deleted_message)))
        postSideEffect(Event.NavigateBack)
    }

    private fun handleOnDeleteCurrencyDialogDismiss() = intent {
        reduce { state.copy(showDeleteDialog = false) }
    }

    private fun handleOnDeleteClick() = intent {
        reduce { state.copy(showDeleteDialog = true) }
    }

    private fun handleOnSelectCurrencyUnitClick() = intent {
        if (currentCurrency.value == null) {
            postSideEffect(Event.NavigateToCurrencyUnitsSelectionList(selectedCurrencyUnit.value?.id))
        }
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(Event.NavigateBack)
    }

    private suspend fun SimpleSyntax<State, Event>.reduceBaseToQuoteRateInput(rate: BigDecimal) {
        reduce {
            state.copy(
                baseToQuoteRateInput = state.baseToQuoteRateInput.copy(
                    value = rate,
                    error = validateRate(rate).errorRes
                )
            )
        }
    }

    private suspend fun SimpleSyntax<State, Event>.reduceQuoteToBaseRateInput(rate: BigDecimal) {
        reduce {
            state.copy(
                quoteToBaseRateInputField = state.quoteToBaseRateInputField.copy(
                    value = rate,
                    error = validateRate(rate).errorRes
                )
            )
        }
    }

    private fun subscribeOnSelectedCurrencyUnit() = intent {
        repeatOnSubscription {
            selectedCurrencyUnit
                .filterNotNull()
                .onEach {
                    reduce {
                        val value = currencyUnitMapper(it).preview
                        state.copy(
                            currencyUnitInput = state.currencyUnitInput.copy(
                                value = value,
                                error = validateCurrencyUnit(value).errorRes
                            )
                        )
                    }
                }
                .collect()
        }
    }

    private fun validateFields(): Boolean {
        val currencyUnitValidationResult = validateCurrencyUnit(state.currencyUnitInput.value)
        val baseToQuoteValidationResult = validateRate(state.baseToQuoteRateInput.value)
        val quoteToBaseValidationResult = validateRate(state.quoteToBaseRateInputField.value)

        intent {
            reduce {
                state.copy(
                    currencyUnitInput = state.currencyUnitInput.copy(error = currencyUnitValidationResult.errorRes),
                    baseToQuoteRateInput = state.baseToQuoteRateInput.copy(error = baseToQuoteValidationResult.errorRes),
                    quoteToBaseRateInputField = state.quoteToBaseRateInputField.copy(error = quoteToBaseValidationResult.errorRes),
                )
            }
        }
        return listOf(
            currencyUnitValidationResult,
            baseToQuoteValidationResult,
            quoteToBaseValidationResult
        ).all { it.isValid }
    }

    private fun validateCurrencyUnit(value: String) = validateInput(
        value, listOf(RequiredStringValidator())
    )

    private fun validateRate(value: BigDecimal) = validateInput(
        value.toPlainString(), listOf(RequiredBigDecimalValidator())
    )
}