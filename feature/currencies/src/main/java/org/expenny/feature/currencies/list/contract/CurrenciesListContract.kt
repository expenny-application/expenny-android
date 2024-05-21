package org.expenny.feature.currencies.list.contract

import org.expenny.core.common.models.StringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.core.ui.data.SingleSelectionUi
import org.expenny.core.ui.data.CurrencyUi

data class CurrenciesListState(
    val toolbarTitle: StringResource = StringResource.fromRes(R.string.currencies_label),
    val selection: SingleSelectionUi<Long>? = null,
    val currencies: List<CurrencyUi> = emptyList(),
)

sealed interface CurrenciesListEvent {
    class ShowMessage(val message: StringResource) : CurrenciesListEvent
    class NavigateBackWithResult(val selection: LongNavArg) : CurrenciesListEvent
    class NavigateToEditCurrency(val id: Long) : CurrenciesListEvent
    object NavigateBack : CurrenciesListEvent
    object NavigateToCreateCurrency : CurrenciesListEvent
}

sealed interface CurrenciesListAction {
    class OnCurrencyClick(val id: Long, val isMain: Boolean) : CurrenciesListAction
    object OnCurrencyAddClick : CurrenciesListAction
    object OnBackClick : CurrenciesListAction
}