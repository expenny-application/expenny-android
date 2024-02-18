package org.expenny.feature.currencyunits

import org.expenny.core.common.utils.Constants
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.core.ui.data.selection.SingleSelection
import org.expenny.core.ui.data.ui.CurrencyUnitUi
import java.util.SortedMap

data class State(
    val searchQuery: String = "",
    val selection: SingleSelection<Long> = SingleSelection(Constants.NULL_ID),
    val currencyUnits: SortedMap<String, List<CurrencyUnitUi>> = sortedMapOf()
)

sealed interface Action {
    class OnCurrencyUnitSelect(val id: Long) : Action
    class OnSearchQueryChange(val query: String) : Action
    data object OnCloseClick : Action
}

sealed interface Event {
    class NavigateBackWithResult(val result: LongNavArg) : Event
    data object NavigateBack : Event
}