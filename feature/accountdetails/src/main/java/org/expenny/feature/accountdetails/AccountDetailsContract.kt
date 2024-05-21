package org.expenny.feature.accountdetails

import org.expenny.core.common.models.StringResource
import org.expenny.core.common.types.LocalAccountType
import org.expenny.core.resources.R
import org.expenny.core.ui.data.InputUi
import org.expenny.core.ui.data.DecimalInputUi
import java.math.BigDecimal

data class State(
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

sealed interface Action {
    class OnNameChange(val name: String) : Action
    class OnTypeChange(val type: LocalAccountType) : Action
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
    object NavigateBackToAccountsList : Event
    object NavigateBack : Event
}