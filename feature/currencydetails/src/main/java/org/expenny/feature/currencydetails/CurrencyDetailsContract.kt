package org.expenny.feature.currencydetails

import org.expenny.core.common.models.StringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.data.field.InputField
import org.expenny.core.ui.data.field.MonetaryInputField
import java.math.BigDecimal

data class State(
    val toolbarTitle: StringResource = StringResource.fromRes(R.string.add_currency_label),
    val showCurrencyRatesSection: Boolean = false,
    val isUpdatable: Boolean = false,
    val isSubscribableToUpdates: Boolean = false,
    val isSubscribedToUpdates: Boolean = false,
    val showInfoButton: Boolean = false,
    val showDeleteButton: Boolean = false,
    val dialog: Dialog? = null,
    val baseCurrency: String = "",
    val quoteCurrency: String = "",
    val lastUpdateInput: InputField = InputField(),
    val currencyUnitInput: InputField = InputField(isRequired = true),
    val baseToQuoteRateInput: MonetaryInputField = MonetaryInputField(isRequired = true),
    val quoteToBaseRateInput: MonetaryInputField = MonetaryInputField(isRequired = true),
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
    class OnSubscribeToUpdatesCheckboxChange(val value: Boolean) : Action
    data object OnUpdateClick : Action
    data object OnSelectCurrencyUnitClick : Action
    data object OnDeleteCurrencyDialogConfirm : Action
    data object OnDialogDismiss : Action
    data object OnSaveClick : Action
    data object OnDeleteClick : Action
    data object OnInfoClick : Action
    data object OnBackClick : Action
}