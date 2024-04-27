package org.expenny.feature.currencyunits

import org.expenny.core.common.utils.Constants
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.core.ui.data.SingleSelectionUi
import org.expenny.core.ui.data.CurrencyUnitUi
import java.util.SortedMap

data class State(
    val searchQuery: String = "",
    val selection: SingleSelectionUi<Long> = SingleSelectionUi(Constants.NULL_ID),
    val currencyUnits: SortedMap<String, List<CurrencyUnitUi>> = sortedMapOf()
)

sealed interface Action {
    class OnCurrencyUnitSelect(val id: Long) : Action
    class OnSearchQueryChange(val query: String) : Action
    object OnCloseClick : Action
}

sealed interface Event {
    class NavigateBackWithResult(val result: LongNavArg) : Event
    object NavigateBack : Event
}