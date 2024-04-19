package org.expenny.feature.accountdetails

import org.expenny.core.common.models.StringResource
import org.expenny.core.common.types.AccountType
import org.expenny.core.resources.R
import org.expenny.core.ui.data.field.InputField
import org.expenny.core.ui.data.field.MonetaryInputField
import java.math.BigDecimal

data class State(
    val toolbarTitle: StringResource = StringResource.fromRes(R.string.add_account_label),
    val types: List<AccountType> = AccountType.values().toList(),
    val showDeleteButton: Boolean = false,
    val showAdditionsSection: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val selectedType: AccountType = AccountType.values()[0],
    val selectedCurrency: String = "",
    val currencyInput: InputField = InputField(),
    val nameInput: InputField = InputField(),
    val descriptionInput: InputField = InputField(isRequired = false),
    val startBalanceInput: MonetaryInputField = MonetaryInputField(isRequired = false),
)

sealed interface Action {
    class OnNameChange(val name: String) : Action
    class OnTypeChange(val type: AccountType) : Action
    class OnDescriptionChange(val description: String) : Action
    class OnCurrencySelect(val id: Long) : Action
    class OnStartBalanceChange(val balance: BigDecimal) : Action
    class OnAdditionsSectionVisibilityChange(val isVisible: Boolean) : Action
    object OnDeleteAccountDialogConfirm : Action
    object OnDeleteAccountDialogDismiss : Action
    object OnSelectCurrencyClick : Action
    object OnSaveClick : Action
    object OnDeleteClick : Action
    object OnBackClick : Action
}

sealed interface Event {
    class ShowMessage(val message: StringResource) : Event
    class NavigateToCurrenciesSelectionList(val selectedId: Long?) : Event
    object RequestNameInputFocus : Event
    object NavigateBack : Event
}