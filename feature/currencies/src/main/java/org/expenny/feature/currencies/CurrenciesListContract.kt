package org.expenny.feature.currencies

import org.expenny.core.common.utils.StringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.core.ui.data.selection.SingleSelection
import org.expenny.core.ui.data.ui.CurrencyUi

data class State(
    val toolbarTitle: StringResource = StringResource.fromRes(R.string.currencies_label),
    val selection: SingleSelection<Long>? = null,
    val currencies: List<CurrencyUi> = emptyList(),
)

sealed interface Event {
    class ShowMessage(val message: StringResource) : Event
    class NavigateBackWithResult(val selection: LongNavArg) : Event
    class NavigateToEditCurrency(val id: Long) : Event
    object NavigateBack : Event
    object NavigateToCreateCurrency : Event
}

sealed interface Action {
    class OnCurrencyClick(val id: Long, val isMain: Boolean) : Action
    object OnCurrencyAddClick : Action
    object OnBackClick : Action
}