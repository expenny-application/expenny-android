package org.expenny.feature.budgets.limit.contract

import org.expenny.core.common.models.StringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.data.CheckboxInputUi
import org.expenny.core.ui.data.DecimalInputUi
import org.expenny.core.ui.data.InputUi
import org.expenny.core.ui.data.navargs.LongNavArg
import java.math.BigDecimal

data class BudgetLimitDetailsState(
    val toolbarTitle: StringResource = StringResource.fromRes(R.string.add_budget_limit_label),
    val showDeleteButton: Boolean = false,
    val categoryInput: InputUi = InputUi(),
    val limitInput: DecimalInputUi = DecimalInputUi(),
    val currency: String = "",
    val showDeleteDialog: Boolean = false,
    val showEnablePeriodicLimitCheckbox: Boolean = false,
    val enablePeriodicLimitCheckboxInput: CheckboxInputUi = CheckboxInputUi(),
)

sealed interface BudgetLimitDetailsAction {
    class OnLimitChange(val limit: BigDecimal) : BudgetLimitDetailsAction
    class OnCategorySelect(val selection: LongNavArg) : BudgetLimitDetailsAction
    class OnEnablePeriodicLimitCheckboxChange(val isChecked: Boolean) : BudgetLimitDetailsAction
    data object OnSelectCategoryClick : BudgetLimitDetailsAction
    data object OnDeleteDialogDismiss : BudgetLimitDetailsAction
    data object OnDeleteDialogConfirm : BudgetLimitDetailsAction
    data object OnSaveClick : BudgetLimitDetailsAction
    data object OnDeleteClick : BudgetLimitDetailsAction
    data object OnBackClick : BudgetLimitDetailsAction
}

sealed interface BudgetLimitDetailsEvent {
    class ShowError(val message: StringResource) : BudgetLimitDetailsEvent
    class ShowMessage(val message: StringResource) : BudgetLimitDetailsEvent
    class NavigateToCategorySelectionList(val selection: LongNavArg, val excludeIds: LongArray? = null) :
        BudgetLimitDetailsEvent
    data object RequestLimitInputFocus : BudgetLimitDetailsEvent
    data object NavigateBack : BudgetLimitDetailsEvent
}