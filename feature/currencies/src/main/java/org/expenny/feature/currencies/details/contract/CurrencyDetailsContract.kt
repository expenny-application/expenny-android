package org.expenny.feature.currencies.details.contract

import org.expenny.core.common.models.StringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.data.InputUi
import org.expenny.core.ui.data.DecimalInputUi
import java.math.BigDecimal

data class CurrencyDetailsState(
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
    val lastUpdateInput: InputUi = InputUi(),
    val currencyUnitInput: InputUi = InputUi(isRequired = true),
    val baseToQuoteRateInput: DecimalInputUi = DecimalInputUi(isRequired = true),
    val quoteToBaseRateInput: DecimalInputUi = DecimalInputUi(isRequired = true),
) {
    sealed interface Dialog {
        data object InfoDialog : Dialog
        data object DeleteDialog : Dialog
        data object LoadingDialog : Dialog
    }
}

sealed interface CurrencyDetailsAction {
    class OnBaseToQuoteRateChange(val rate: BigDecimal) : CurrencyDetailsAction
    class OnQuoteToBaseRateChange(val rate: BigDecimal) : CurrencyDetailsAction
    class OnCurrencyUnitSelect(val id: Long) : CurrencyDetailsAction
    class OnSubscribeToUpdatesCheckboxChange(val value: Boolean) : CurrencyDetailsAction
    data object OnUpdateClick : CurrencyDetailsAction
    data object OnSelectCurrencyUnitClick : CurrencyDetailsAction
    data object OnDeleteCurrencyDialogConfirm : CurrencyDetailsAction
    data object OnDialogDismiss : CurrencyDetailsAction
    data object OnSaveClick : CurrencyDetailsAction
    data object OnDeleteClick : CurrencyDetailsAction
    data object OnInfoClick : CurrencyDetailsAction
    data object OnBackClick : CurrencyDetailsAction
}

sealed interface CurrencyDetailsEvent {
    class ShowMessage(val message: StringResource) : CurrencyDetailsEvent
    class NavigateToCurrencyUnitsSelectionList(val selectedId: Long?) : CurrencyDetailsEvent
    data object NavigateBack : CurrencyDetailsEvent
}