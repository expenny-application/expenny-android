package org.expenny.feature.currencyunits.contract

import org.expenny.core.common.utils.Constants
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.core.ui.data.SingleSelectionUi
import org.expenny.core.ui.data.CurrencyUnitUi
import java.util.SortedMap

data class CurrencyUnitsListState(
    val searchQuery: String = "",
    val selection: SingleSelectionUi<Long> = SingleSelectionUi(Constants.NULL_ID),
    val currencyUnits: SortedMap<String, List<CurrencyUnitUi>> = sortedMapOf()
)

sealed interface CurrencyUnitsListAction {
    class OnCurrencyUnitSelect(val id: Long) : CurrencyUnitsListAction
    class OnSearchQueryChange(val query: String) : CurrencyUnitsListAction
    object OnCloseClick : CurrencyUnitsListAction
}

sealed interface CurrencyUnitsListEvent {
    class NavigateBackWithResult(val result: LongNavArg) : CurrencyUnitsListEvent
    object NavigateBack : CurrencyUnitsListEvent
}