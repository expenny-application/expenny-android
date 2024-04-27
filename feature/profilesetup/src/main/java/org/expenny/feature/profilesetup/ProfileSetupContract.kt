package org.expenny.feature.profilesetup

import org.expenny.core.common.models.StringResource
import org.expenny.core.ui.data.CheckboxInputUi
import org.expenny.core.ui.data.InputUi
import org.expenny.core.ui.data.DecimalInputUi
import java.math.BigDecimal

internal data class State(
    val isCtaEnabled: Boolean = false,
    val showAbortDialog: Boolean = false,
    val showConfirmationDialog: Boolean = false,
    val showSetupCashBalanceInputs: Boolean = false,
    val showSetupCashBalanceCheckbox: Boolean = false,
    val setupCashBalanceCheckbox: CheckboxInputUi = CheckboxInputUi(),
    val selectedCurrency: String = "",
    val selectCurrencyInput: InputUi = InputUi(),
    val nameInput: InputUi = InputUi(),
    val accountNameInput: InputUi = InputUi(),
    val accountBalanceInput: DecimalInputUi = DecimalInputUi(),
)

internal sealed interface Action {
    class OnCurrencyUnitSelect(val id: Long) : Action
    class OnNameChange(val name: String) : Action
    class OnAccountNameChange(val accountName: String) : Action
    class OnAccountBalanceChange(val accountBalance: BigDecimal) : Action
    class OnSetupCashBalanceCheckboxChange(val isChecked: Boolean) : Action
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