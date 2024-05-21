package org.expenny.feature.accounts.details.contract

import org.expenny.core.common.models.StringResource
import org.expenny.core.common.types.LocalAccountType
import org.expenny.core.resources.R
import org.expenny.core.ui.data.InputUi
import org.expenny.core.ui.data.DecimalInputUi
import java.math.BigDecimal

data class AccountDetailsState(
    val toolbarTitle: StringResource = StringResource.fromRes(R.string.add_account_label),
    val types: List<LocalAccountType> = LocalAccountType.entries,
    val showDeleteButton: Boolean = false,
    val showAdditionsSection: Boolean = true,
    val showDeleteDialog: Boolean = false,
    val selectedType: LocalAccountType = LocalAccountType.entries[0],
    val selectedCurrency: String = "",
    val currencyInput: InputUi = InputUi(),
    val nameInput: InputUi = InputUi(),
    val descriptionInput: InputUi = InputUi(isRequired = false),
    val startBalanceInput: DecimalInputUi = DecimalInputUi(isRequired = false),
)

sealed interface AccountDetailsAction {
    class OnNameChange(val name: String) : AccountDetailsAction
    class OnTypeChange(val type: LocalAccountType) : AccountDetailsAction
    class OnDescriptionChange(val description: String) : AccountDetailsAction
    class OnCurrencySelect(val id: Long) : AccountDetailsAction
    class OnStartBalanceChange(val balance: BigDecimal) : AccountDetailsAction
    class OnAdditionsSectionVisibilityChange(val isVisible: Boolean) : AccountDetailsAction
    object OnDeleteAccountDialogConfirm : AccountDetailsAction
    object OnDeleteAccountDialogDismiss : AccountDetailsAction
    object OnSelectCurrencyClick : AccountDetailsAction
    object OnSaveClick : AccountDetailsAction
    object OnDeleteClick : AccountDetailsAction
    object OnBackClick : AccountDetailsAction
}

sealed interface AccountDetailsEvent {
    class ShowMessage(val message: StringResource) : AccountDetailsEvent
    class NavigateToCurrenciesSelectionList(val selectedId: Long?) : AccountDetailsEvent
    object RequestNameInputFocus : AccountDetailsEvent
    object NavigateBackToAccountsList : AccountDetailsEvent
    object NavigateBack : AccountDetailsEvent
}