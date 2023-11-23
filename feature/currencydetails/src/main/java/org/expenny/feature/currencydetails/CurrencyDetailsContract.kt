package org.expenny.feature.currencydetails

import org.expenny.core.common.utils.StringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.data.field.InputField
import org.expenny.core.ui.data.field.MonetaryInputField
import java.math.BigDecimal

data class State(
    val toolbarTitle: StringResource = StringResource.fromRes(R.string.add_currency_label),
    val showRatesDisclaimerMessage: Boolean = false,
    val showRatesInputFields: Boolean = false,
    val showSubscribeToRatesUpdatesCheckbox: Boolean = false,
    val subscribeToRatesUpdates: Boolean = false,
    val showDeleteButton: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val currencyUnitInput: InputField = InputField(required = true),
    val baseCurrency: String = "",
    val quoteCurrency: String = "",
    val baseToQuoteRateInput: MonetaryInputField = MonetaryInputField(required = true),
    val quoteToBaseRateInputField: MonetaryInputField = MonetaryInputField(required = true),
    val isLoading: Boolean = false
)

sealed interface Event {
    class ShowMessage(val message: StringResource) : Event
    class NavigateToCurrencyUnitsSelectionList(val selectedId: Long?) : Event
    object NavigateBack : Event
}

sealed interface Action {
    class OnBaseToQuoteRateChange(val rate: BigDecimal) : Action
    class OnQuoteToBaseRateChange(val rate: BigDecimal) : Action
    class OnCurrencyUnitSelect(val id: Long) : Action
    class OnSubscribeToRatesUpdateCheckboxChange(val value: Boolean) : Action
    object OnSelectCurrencyUnitClick : Action
    object OnDeleteCurrencyDialogConfirm : Action
    object OnDeleteCurrencyDialogDismiss : Action
    object OnSaveClick : Action
    object OnDeleteClick : Action
    object OnBackClick : Action
}