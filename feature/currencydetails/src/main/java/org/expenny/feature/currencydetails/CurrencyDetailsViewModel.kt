package org.expenny.feature.currencydetails

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import org.expenny.core.common.extensions.invert
import org.expenny.core.common.extensions.toDateString
import org.expenny.core.common.utils.Constants.CURRENCY_RATE_SCALE
import org.expenny.core.common.utils.StringResource.Companion.fromRes
import org.expenny.core.common.viewmodel.ExpennyActionViewModel
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
import org.expenny.core.model.currency.CurrencyRate
import org.expenny.core.model.currency.CurrencyUnit
import org.expenny.core.model.resource.RemoteResult
import org.expenny.core.resources.R
import org.expenny.core.ui.mapper.CurrencyUnitMapper
import org.expenny.feature.currencydetails.navigation.CurrencyDetailsNavArgs
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.time.LocalDateTime
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

    private val currencyId: Long? = savedStateHandle.navArgs<CurrencyDetailsNavArgs>().currencyId
    private var currencyUnit: CurrencyUnit? = null
    private lateinit var mainCurrency: Currency

    override val container = container<State, Event>(
        initialState = State(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            initState()
        }
    }

    private val stateValue
        get() = container.stateFlow.value

    private fun initState() {
        intent {
            runCatching {
                getMainCurrency().first()!!.also {
                    mainCurrency = it
                    setBaseCurrency(it.unit.code)
                }

                val currency = currencyId?.let { id ->
                    getCurrency(GetCurrencyUseCase.Params(id)).first()!!.also {
                        currencyUnit = it.unit
                    }
                }

                if (currency != null) {
                    setEditCurrency()
                    setCurrencyData(currency)
                    setBaseToQuoteRate(currency.baseToQuoteRate)
                    setQuoteToBaseRate(currency.quoteToBaseRate)
                    setIsSubscribableToUpdates(mainCurrency.unit, currency.unit)
                } else {
                    setAddCurrency()
                }
            }.onFailure {
                if (it !is CancellationException) {
                    postSideEffect(Event.ShowMessage(fromRes(R.string.internal_error)))
                    postSideEffect(Event.NavigateBack)
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
            is Action.OnDialogDismiss -> handleOnDialogDismiss()
            is Action.OnSaveClick -> handleOnSaveClick()
            is Action.OnBaseToQuoteRateChange -> handleOnBaseToQuoteRateChange(action)
            is Action.OnQuoteToBaseRateChange -> handleOnQuoteToBaseRateChange(action)
            is Action.OnSubscribeToUpdatesCheckboxChange -> handleOnSubscribeToUpdatesCheckboxChange(action)
            is Action.OnUpdateClick -> handleOnUpdateClick()
            is Action.OnInfoClick -> handleOnInfoClick()
            is Action.OnBackClick -> handleOnBackClick()
        }
    }

    private fun handleOnUpdateClick() = intent {
        currencyUnit?.let {
            handleOnCurrencyUnitSelect(Action.OnCurrencyUnitSelect(it.id))
        }
    }

    private fun handleOnSubscribeToUpdatesCheckboxChange(action: Action.OnSubscribeToUpdatesCheckboxChange) {
        blockingIntent {
            reduce {
                state.copy(isSubscribedToUpdates = action.value)
            }
        }
    }

    private fun handleOnBaseToQuoteRateChange(action: Action.OnBaseToQuoteRateChange) {
        blockingIntent {
            setBaseToQuoteRate(action.rate)
            setQuoteToBaseRate(state.baseToQuoteRateInput.value.invert())
        }
    }

    private fun handleOnQuoteToBaseRateChange(action: Action.OnQuoteToBaseRateChange) {
        blockingIntent {
            setQuoteToBaseRate(action.rate)
            setBaseToQuoteRate(state.quoteToBaseRateInput.value.invert())
        }
    }

    private fun handleOnCurrencyUnitSelect(action: Action.OnCurrencyUnitSelect) {
        getCurrencyUnit(GetCurrencyUnitUseCase.Params(action.id))!!.also {
            handleOnCurrencyUnitSelect(it)
        }
    }

    private fun handleOnCurrencyUnitSelect(newCurrencyUnit: CurrencyUnit) {
        intent {
            reduce {
                state.copy(
                    baseCurrency = mainCurrency.unit.code,
                    quoteCurrency = newCurrencyUnit.code,
                    currencyUnitInput = state.currencyUnitInput.copy(
                        value = currencyUnitMapper(newCurrencyUnit).preview
                    )
                )
            }

            fetchLatestCurrencyRate(
                base = mainCurrency.unit,
                quote = newCurrencyUnit,
                onComplete = {
                    currencyUnit = newCurrencyUnit
                },
                onSuccess = {
                    reduce {
                        state.copy(
                            showCurrencyRatesSection = true,
                            isSubscribableToUpdates = true,
                            isUpdatable = false,
                            lastUpdateInput = state.lastUpdateInput.copy(
                                value = LocalDateTime.now().toDateString()
                            )
                        )
                    }
                    setBaseToQuoteRate(it.rate)
                    setQuoteToBaseRate(it.rate.invert())
                },
                onError = {
                    if (newCurrencyUnit != currencyUnit) {
                        reduce {
                            state.copy(
                                showCurrencyRatesSection = true,
                                isSubscribableToUpdates = false,
                            )
                        }
                        setBaseToQuoteRate(ONE.setScale(CURRENCY_RATE_SCALE))
                        setQuoteToBaseRate(ONE.setScale(CURRENCY_RATE_SCALE))
                    }
                }
            )
        }
    }

    private fun handleOnSaveClick() = intent {
        if (validateFields()) {
            if (currencyId == null) {
                createCurrency(
                    CreateCurrencyUseCase.Params(
                        currencyUnitId = currencyUnit!!.id,
                        quoteToBaseRate = state.quoteToBaseRateInput.value,
                        isSubscribedToRateUpdates = state.isSubscribedToUpdates
                    )
                )
            } else {
                updateCurrency(
                    UpdateCurrencyUseCase.Params(
                        id = currencyId,
                        quoteToBaseRate = state.quoteToBaseRateInput.value,
                        isSubscribedToRateUpdates = state.isSubscribedToUpdates
                    )
                )
            }
            postSideEffect(Event.ShowMessage(fromRes(R.string.saved_message)))
            postSideEffect(Event.NavigateBack)
        }
    }

    private fun handleOnDeleteCurrencyDialogConfirm() = intent {
        reduce {
            state.copy(dialog = null)
        }
        deleteCurrency(currencyId!!)
        postSideEffect(Event.ShowMessage(fromRes(R.string.deleted_message)))
        postSideEffect(Event.NavigateBack)
    }

    private fun handleOnDialogDismiss() = intent {
        reduce { state.copy(dialog = null) }
    }

    private fun handleOnDeleteClick() = intent {
        reduce { state.copy(dialog = State.Dialog.DeleteDialog) }
    }

    private fun handleOnSelectCurrencyUnitClick() = intent {
        if (currencyId == null) {
            postSideEffect(Event.NavigateToCurrencyUnitsSelectionList(currencyUnit?.id))
        }
    }

    private fun handleOnInfoClick() = intent {
        reduce { state.copy(dialog = State.Dialog.InfoDialog) }
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(Event.NavigateBack)
    }

    private suspend fun SimpleSyntax<State, *>.setBaseToQuoteRate(rate: BigDecimal) {
        reduce {
            state.copy(
                baseToQuoteRateInput = state.baseToQuoteRateInput.copy(
                    value = rate,
                    error = validateRate(rate).errorRes
                )
            )
        }
    }

    private suspend fun SimpleSyntax<State, *>.setQuoteToBaseRate(rate: BigDecimal) {
        reduce {
            state.copy(
                quoteToBaseRateInput = state.quoteToBaseRateInput.copy(
                    value = rate,
                    error = validateRate(rate).errorRes
                )
            )
        }
    }

    private suspend fun SimpleSyntax<State, *>.setIsSubscribableToUpdates(base: CurrencyUnit, quote: CurrencyUnit) {
        fetchLatestCurrencyRate(
            base = base,
            quote = quote,
            onComplete = {
                reduce {
                    state.copy(isSubscribableToUpdates = it != null)
                }
            }
        )
    }

    private suspend fun SimpleSyntax<State, *>.setCurrencyData(currency: Currency) {
        reduce {
            val currencyUnitInput = state.currencyUnitInput.copy(
                value = currencyUnitMapper(currency.unit).preview,
                isEnabled = false
            )
            val lastUpdateInput = state.lastUpdateInput.copy(
                value = currency.updatedAt.toDateString()
            )
            state.copy(
                isSubscribedToUpdates = currency.isSubscribedToUpdates,
                isUpdatable = currency.isStale,
                quoteCurrency = currency.unit.code,
                currencyUnitInput = currencyUnitInput,
                lastUpdateInput = lastUpdateInput
            )
        }
    }

    private suspend fun SimpleSyntax<State, *>.setAddCurrency() {
        reduce {
            state.copy(
                toolbarTitle = fromRes(R.string.add_currency_label),
                showInfoButton = true,
            )
        }
    }

    private suspend fun SimpleSyntax<State, *>.setEditCurrency() {
        reduce {
            state.copy(
                toolbarTitle = fromRes(R.string.edit_currency_label),
                showCurrencyRatesSection = true,
                showDeleteButton = true
            )
        }
    }

    private suspend fun SimpleSyntax<State, *>.setBaseCurrency(currencyCode: String) {
        reduce {
            state.copy(baseCurrency = currencyCode)
        }
    }


    private fun validateFields(): Boolean {
        val currencyUnitValidationResult = validateCurrencyUnit(stateValue.currencyUnitInput.value)
        val baseToQuoteValidationResult = validateRate(stateValue.baseToQuoteRateInput.value)
        val quoteToBaseValidationResult = validateRate(stateValue.quoteToBaseRateInput.value)

        intent {
            reduce {
                state.copy(
                    currencyUnitInput = state.currencyUnitInput.copy(error = currencyUnitValidationResult.errorRes),
                    baseToQuoteRateInput = state.baseToQuoteRateInput.copy(error = baseToQuoteValidationResult.errorRes),
                    quoteToBaseRateInput = state.quoteToBaseRateInput.copy(error = quoteToBaseValidationResult.errorRes),
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

    private suspend fun fetchLatestCurrencyRate(
        base: CurrencyUnit,
        quote: CurrencyUnit,
        onSuccess: suspend (CurrencyRate) -> Unit = {},
        onError: suspend () -> Unit = {},
        onLoading: suspend () -> Unit = {},
        onComplete: suspend (CurrencyRate?) -> Unit = {},
    ) {
        intent {
            getLatestCurrencyRate(
                base = base.code,
                quote = quote.code
            ).collect { result ->
                when (result) {
                    is RemoteResult.Loading -> {
                        reduce { state.copy(dialog = State.Dialog.LoadingDialog) }
                        onLoading()
                    }
                    is RemoteResult.Error -> {
                        reduce { state.copy(dialog = null) }
                        onError()
                        onComplete(null)
                    }
                    is RemoteResult.Success -> {
                        reduce { state.copy(dialog = null) }
                        result.data!!.also {
                            onSuccess(it)
                            onComplete(it)
                        }
                    }
                }
            }
        }
    }
}