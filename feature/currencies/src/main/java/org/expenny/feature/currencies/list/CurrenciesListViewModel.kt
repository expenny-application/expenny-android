package org.expenny.feature.currencies.list

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.expenny.core.common.models.StringResource.Companion.fromRes
import org.expenny.core.domain.usecase.currency.GetCurrenciesUseCase
import org.expenny.core.ui.data.SingleSelectionUi
import org.expenny.core.ui.mapper.CurrencyMapper
import org.expenny.feature.currencies.list.navigation.CurrenciesListNavArgs
import org.expenny.core.resources.R
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.core.ui.base.ExpennyViewModel
import org.expenny.feature.currencies.list.contract.CurrenciesListAction
import org.expenny.feature.currencies.list.contract.CurrenciesListEvent
import org.expenny.feature.currencies.list.contract.CurrenciesListState
import org.expenny.feature.currencies.navArgs
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class CurrenciesListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getCurrencies: GetCurrenciesUseCase,
    private val currencyMapper: CurrencyMapper,
) : ExpennyViewModel<CurrenciesListAction>(), ContainerHost<CurrenciesListState, CurrenciesListEvent> {

    override val container = container<CurrenciesListState, CurrenciesListEvent>(
        initialState = CurrenciesListState(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            launch { subscribeToCurrencies() }
            setNavArgs()
        }
    }

    override fun onAction(action: CurrenciesListAction) {
        when (action) {
            is CurrenciesListAction.OnCurrencyClick -> handleOnCurrencyClick(action)
            is CurrenciesListAction.OnCurrencyAddClick -> handleOnCurrencyAddClick()
            is CurrenciesListAction.OnBackClick -> handleOnBackClick()
        }
    }

    private fun handleOnCurrencyClick(action: CurrenciesListAction.OnCurrencyClick) = intent {
        if (state.selection != null) {
            postSideEffect(CurrenciesListEvent.NavigateBackWithResult(LongNavArg(action.id)))
        } else {
            if (action.isMain) {
                postSideEffect(CurrenciesListEvent.ShowMessage(fromRes(R.string.readonly_main_currency_message)))
            } else {
                postSideEffect(CurrenciesListEvent.NavigateToEditCurrency(action.id))
            }
        }
    }

    private fun handleOnCurrencyAddClick() = intent {
        postSideEffect(CurrenciesListEvent.NavigateToCreateCurrency)
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(CurrenciesListEvent.NavigateBack)
    }

    private fun subscribeToCurrencies() = intent {
        repeatOnSubscription {
            getCurrencies().collect {
                reduce { state.copy(currencies = currencyMapper(it)) }
            }
        }
    }

    private fun setNavArgs() {
        savedStateHandle.navArgs<CurrenciesListNavArgs>().also { args ->
            intent {
                if (args.selection != null) {
                    reduce {
                        state.copy(
                            toolbarTitle = fromRes(R.string.select_currency_label),
                            selection = SingleSelectionUi(args.selection.value)
                        )
                    }
                } else {
                    reduce {
                        state.copy(toolbarTitle = fromRes(R.string.currencies_label))
                    }
                }
            }
        }
    }
}