package org.expenny.feature.currencyunits

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.expenny.core.common.viewmodel.*
import org.expenny.core.domain.usecase.currencyunit.GetAvailableCurrencyUnitsUseCase
import org.expenny.core.domain.usecase.currencyunit.GetCurrencyUnitsUseCase
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.core.ui.data.selection.SingleSelection
import org.expenny.core.ui.data.ui.CurrencyUnitUi
import org.expenny.core.ui.mapper.CurrencyUnitMapper
import org.expenny.feature.currencyunits.navigation.CurrencyUnitsListNavArgs
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import java.util.SortedMap
import javax.inject.Inject

@HiltViewModel
class CurrencyUnitsListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAvailableCurrencyUnits: GetAvailableCurrencyUnitsUseCase,
    private val getCurrencyUnits: GetCurrencyUnitsUseCase,
    private val currencyUnitMapper: CurrencyUnitMapper
) : ExpennyActionViewModel<Action>(), ContainerHost<State, Event> {

    private val searchQuery = MutableStateFlow("")
    private val includeOnlyAvailable = savedStateHandle.navArgs<CurrencyUnitsListNavArgs>().includeOnlyAvailable

    override val container = container<State, Event>(
        initialState = State(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            setInitialData()
            launch { subscribeToCurrencyUnits() }
        }
    }

    override fun onAction(action: Action) {
        when (action) {
            is Action.OnSearchQueryChange -> handleOnSearchQueryChange(action)
            is Action.OnCurrencyUnitSelect -> handleOnCurrencyUnitSelect(action)
            is Action.OnCloseClick -> handleOnCloseClick()
        }
    }

    private fun handleOnSearchQueryChange(action: Action.OnSearchQueryChange) {
        searchQuery.value = action.query
        blockingIntent {
            reduce {
                state.copy(searchQuery = action.query)
            }
        }
    }

    private fun handleOnCurrencyUnitSelect(action: Action.OnCurrencyUnitSelect) = intent {
        postSideEffect(Event.NavigateBackWithResult(LongNavArg(action.id)))
    }

    private fun handleOnCloseClick() = intent {
        postSideEffect(Event.NavigateBack)
    }

    private fun subscribeToCurrencyUnits() = intent {
        repeatOnSubscription {
            searchQuery
                .flatMapLatest {
                    if (includeOnlyAvailable) getAvailableCurrencyUnits(it) else getCurrencyUnits(it)
                }
                .distinctUntilChanged()
                .collect {
                    reduce {
                        state.copy(currencyUnits = currencyUnitMapper(it).sortAndGroup())
                    }
                }
        }
    }

    private fun setInitialData() {
        savedStateHandle.navArgs<CurrencyUnitsListNavArgs>().also { args ->
            intent {
                reduce {
                    state.copy(selection = SingleSelection(args.selection.value))
                }
            }
        }
    }

    private fun List<CurrencyUnitUi>?.sortAndGroup(): SortedMap<String, List<CurrencyUnitUi>> {
        return orEmpty()
            .groupBy { it.code.first().toString() }
            .toSortedMap(compareBy { it })
    }
}