package org.expenny.feature.currencies

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.expenny.core.common.utils.StringResource.Companion.fromRes
import org.expenny.core.common.viewmodel.*
import org.expenny.core.domain.usecase.currency.GetCurrenciesUseCase
import org.expenny.core.ui.data.selection.SingleSelection
import org.expenny.core.ui.mapper.CurrencyMapper
import org.expenny.feature.currencies.navigation.CurrenciesListNavArgs
import org.expenny.core.resources.R
import org.expenny.core.ui.data.navargs.LongNavArg
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
) : ExpennyActionViewModel<Action>(), ContainerHost<State, Event> {

    override val container = container<State, Event>(
        initialState = State(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            launch { subscribeToCurrencies() }
            setNavArgs()
        }
    }

    override fun onAction(action: Action) {
        when (action) {
            is Action.OnCurrencyClick -> handleOnCurrencyClick(action)
            is Action.OnCurrencyAddClick -> handleOnCurrencyAddClick()
            is Action.OnBackClick -> handleOnBackClick()
        }
    }

    private fun handleOnCurrencyClick(action: Action.OnCurrencyClick) = intent {
        if (state.selection != null) {
            postSideEffect(Event.NavigateBackWithResult(LongNavArg(action.id)))
        } else {
            if (action.isMain) {
                postSideEffect(Event.ShowMessage(fromRes(R.string.readonly_main_currency_message)))
            } else {
                postSideEffect(Event.NavigateToEditCurrency(action.id))
            }
        }
    }

    private fun handleOnCurrencyAddClick() = intent {
        postSideEffect(Event.NavigateToCreateCurrency)
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(Event.NavigateBack)
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
                            selection = SingleSelection(args.selection.value)
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