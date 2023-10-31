package org.expenny.feature.getstarted

import org.expenny.core.common.utils.StringResource
import org.expenny.core.ui.data.field.CheckBoxField
import org.expenny.core.ui.data.field.InputField
import org.expenny.core.ui.data.field.MonetaryInputField
import java.math.BigDecimal

internal data class State(
    val enableSubmitButton: Boolean = false,
    val showAbortDialog: Boolean = false,
    val showConfirmationDialog: Boolean = false,
    val showSetupCashBalanceInputFields: Boolean = false,
    val showSetupCashBalanceCheckBox: Boolean = false,
    val setupCashBalanceCheckBox: CheckBoxField = CheckBoxField(),
    val selectedCurrency: String = "",
    val selectCurrencyInput: InputField = InputField(),
    val nameInput: InputField = InputField(),
    val accountNameInput: InputField = InputField(),
    val accountBalanceInput: MonetaryInputField = MonetaryInputField(),
)

internal sealed interface Action {
    class OnCurrencyUnitSelect(val id: Long) : Action
    class OnNameChange(val name: String) : Action
    class OnAccountNameChange(val accountName: String) : Action
    class OnAccountBalanceChange(val accountBalance: BigDecimal) : Action
    class OnSetupCashBalanceCheckBoxChange(val isChecked: Boolean) : Action
    object OnSelectCurrencyUnitClick : Action
    object OnConfirmationDialogConfirm : Action
    object OnAbortDialogConfirm : Action
    object OnConfirmationDialogDismiss : Action
    object OnAbortDialogDismiss : Action
    object OnGetStartedClick : Action
    object OnBackClick : Action
}

internal sealed interface Event {
    class ShowMessage(val message: StringResource) : Event
    class NavigateToCurrencyUnitsSelectionList(val selectedId: Long?) : Event
    object NavigateToApp : Event
    object NavigateBack : Event
}