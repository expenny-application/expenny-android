package org.expenny.feature.profilesetup.contract

import org.expenny.core.common.models.StringResource
import org.expenny.core.ui.data.CheckboxInputUi
import org.expenny.core.ui.data.InputUi
import org.expenny.core.ui.data.DecimalInputUi
import java.math.BigDecimal

internal data class ProfileSetupState(
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

internal sealed interface ProfileSetupAction {
    class OnCurrencyUnitSelect(val id: Long) : ProfileSetupAction
    class OnNameChange(val name: String) : ProfileSetupAction
    class OnAccountNameChange(val accountName: String) : ProfileSetupAction
    class OnAccountBalanceChange(val accountBalance: BigDecimal) : ProfileSetupAction
    class OnSetupCashBalanceCheckboxChange(val isChecked: Boolean) : ProfileSetupAction
    data object OnSelectCurrencyUnitClick : ProfileSetupAction
    data object OnConfirmationDialogConfirm : ProfileSetupAction
    data object OnAbortDialogConfirm : ProfileSetupAction
    data object OnConfirmationDialogDismiss : ProfileSetupAction
    data object OnAbortDialogDismiss : ProfileSetupAction
    data object OnCtaClick : ProfileSetupAction
    data object OnBackClick : ProfileSetupAction
}

internal sealed interface ProfileSetupEvent {
    class ShowMessage(val message: StringResource) : ProfileSetupEvent
    class NavigateToCurrencyUnitsSelectionList(val selectedId: Long?) : ProfileSetupEvent
    data object NavigateToHome : ProfileSetupEvent
    data object NavigateBack : ProfileSetupEvent
}