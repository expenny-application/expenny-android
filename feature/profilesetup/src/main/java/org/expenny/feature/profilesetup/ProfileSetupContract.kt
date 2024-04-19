package org.expenny.feature.profilesetup

import org.expenny.core.common.models.StringResource
import org.expenny.core.ui.data.field.CheckBoxField
import org.expenny.core.ui.data.field.InputField
import org.expenny.core.ui.data.field.MonetaryInputField
import java.math.BigDecimal

internal data class State(
    val isCtaEnabled: Boolean = false,
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
    data object OnSelectCurrencyUnitClick : Action
    data object OnConfirmationDialogConfirm : Action
    data object OnAbortDialogConfirm : Action
    data object OnConfirmationDialogDismiss : Action
    data object OnAbortDialogDismiss : Action
    data object OnCtaClick : Action
    data object OnBackClick : Action
}

internal sealed interface Event {
    class ShowMessage(val message: StringResource) : Event
    class NavigateToCurrencyUnitsSelectionList(val selectedId: Long?) : Event
    data object NavigateToHome : Event
    data object NavigateBack : Event
}