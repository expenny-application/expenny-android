package org.expenny.feature.currencydetails

import org.expenny.core.common.utils.StringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.data.field.InputField
import org.expenny.core.ui.data.field.MonetaryInputField
import java.math.BigDecimal

data class State(
    val toolbarTitle: StringResource = StringResource.fromRes(R.string.add_currency_label),
    val showRatesInputFields: Boolean = false,
    val showSubscribeToRatesUpdatesCheckbox: Boolean = false,
    val subscribeToRatesUpdates: Boolean = false,
    val showInfoButton: Boolean = false,
    val showDeleteButton: Boolean = false,
    val dialog: Dialog? = null,
    val currencyUnitInput: InputField = InputField(required = true),
    val baseCurrency: String = "",
    val quoteCurrency: String = "",
    val baseToQuoteRateInput: MonetaryInputField = MonetaryInputField(required = true),
    val quoteToBaseRateInputField: MonetaryInputField = MonetaryInputField(required = true),
) {
    sealed interface Dialog {
        data object InfoDialog : Dialog
        data object DeleteDialog : Dialog
        data object LoadingDialog : Dialog
    }
}

sealed interface Event {
    class ShowMessage(val message: StringResource) : Event
    class NavigateToCurrencyUnitsSelectionList(val selectedId: Long?) : Event
    data object NavigateBack : Event
}

sealed interface Action {
    class OnBaseToQuoteRateChange(val rate: BigDecimal) : Action
    class OnQuoteToBaseRateChange(val rate: BigDecimal) : Action
    class OnCurrencyUnitSelect(val id: Long) : Action
    class OnSubscribeToRatesUpdateCheckboxChange(val value: Boolean) : Action
    data object OnUpdateRateClick : Action
    data object OnSelectCurrencyUnitClick : Action
    data object OnDeleteCurrencyDialogConfirm : Action
    data object OnDialogDismiss : Action
    data object OnSaveClick : Action
    data object OnDeleteClick : Action
    data object OnInfoClick : Action
    data object OnBackClick : Action
}