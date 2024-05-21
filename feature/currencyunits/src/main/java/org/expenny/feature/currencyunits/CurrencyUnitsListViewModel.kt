package org.expenny.feature.currencyunits

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.expenny.core.domain.usecase.currencyunit.GetAvailableCurrencyUnitsUseCase
import org.expenny.core.domain.usecase.currencyunit.GetCurrencyUnitsUseCase
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.core.ui.data.SingleSelectionUi
import org.expenny.core.ui.data.CurrencyUnitUi
import org.expenny.core.ui.mapper.CurrencyUnitMapper
import org.expenny.core.ui.base.ExpennyViewModel
import org.expenny.feature.currencyunits.contract.CurrencyUnitsListAction
import org.expenny.feature.currencyunits.contract.CurrencyUnitsListEvent
import org.expenny.feature.currencyunits.contract.CurrencyUnitsListState
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
) : ExpennyViewModel<CurrencyUnitsListAction>(), ContainerHost<CurrencyUnitsListState, CurrencyUnitsListEvent> {

    private val searchQuery = MutableStateFlow("")
    private val includeOnlyAvailable = savedStateHandle.navArgs<CurrencyUnitsListNavArgs>().includeOnlyAvailable

    override val container = container<CurrencyUnitsListState, CurrencyUnitsListEvent>(
        initialState = CurrencyUnitsListState(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            setInitialData()
            launch { subscribeToCurrencyUnits() }
        }
    }

    override fun onAction(action: CurrencyUnitsListAction) {
        when (action) {
            is CurrencyUnitsListAction.OnSearchQueryChange -> handleOnSearchQueryChange(action)
            is CurrencyUnitsListAction.OnCurrencyUnitSelect -> handleOnCurrencyUnitSelect(action)
            is CurrencyUnitsListAction.OnCloseClick -> handleOnCloseClick()
        }
    }

    private fun handleOnSearchQueryChange(action: CurrencyUnitsListAction.OnSearchQueryChange) {
        searchQuery.value = action.query
        blockingIntent {
            reduce {
                state.copy(searchQuery = action.query)
            }
        }
    }

    private fun handleOnCurrencyUnitSelect(action: CurrencyUnitsListAction.OnCurrencyUnitSelect) = intent {
        postSideEffect(CurrencyUnitsListEvent.NavigateBackWithResult(LongNavArg(action.id)))
    }

    private fun handleOnCloseClick() = intent {
        postSideEffect(CurrencyUnitsListEvent.NavigateBack)
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
                    state.copy(selection = SingleSelectionUi(args.selection.value))
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