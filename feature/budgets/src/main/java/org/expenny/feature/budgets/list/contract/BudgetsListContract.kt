package org.expenny.feature.budgets.list.contract

import org.expenny.core.common.models.StringResource
import org.expenny.core.common.types.BudgetType
import org.expenny.core.common.types.IntervalType
import org.expenny.core.common.types.ItemActionType
import org.expenny.core.ui.data.PeriodicBudgetUi

data class BudgetsListState(
    val budgetTypes: List<BudgetType> = BudgetType.entries,
    val selectedBudgetType: BudgetType = BudgetType.Periodic,
    val periodicBudgets: List<PeriodicBudgetUi> = emptyList(),
    val dialog: Dialog? = null,
) {
    val showAddButton: Boolean
        get() = selectedBudgetType == BudgetType.Onetime

    private val budgetIntervalTypes: List<IntervalType> =
        listOf(IntervalType.Week, IntervalType.Month, IntervalType.Quarter, IntervalType.Year)

    val availableBudgetIntervalTypes: List<IntervalType>
        get() = budgetIntervalTypes.filter { budgetIntervalType ->
            !periodicBudgets.map { it.intervalType }.contains(budgetIntervalType)
        }

    sealed interface Dialog {
        data class DeleteBudgetDialog(val id: Long) : Dialog

        data class BudgetActionsDialog(val id: Long, val type: BudgetType, val intervalType: IntervalType?) : Dialog {
            val actions: List<ItemActionType> = listOf(
                ItemActionType.View, ItemActionType.Share, ItemActionType.Delete
            )
        }
    }
}

sealed interface BudgetsListAction {
    class OnPeriodicBudgetLongClick(val id: Long, val intervalType: IntervalType) : BudgetsListAction
    class OnPeriodicBudgetClick(val id: Long, val type: IntervalType) : BudgetsListAction
    class OnPeriodicBudgetDeleteClick(val id: Long) : BudgetsListAction
    class OnPeriodicBudgetCreateClick(val type: IntervalType) : BudgetsListAction
    class OnBudgetTypeChange(val type: BudgetType) : BudgetsListAction
    class OnBudgetActionSelect(val action: ItemActionType) : BudgetsListAction
    data object OnDeleteBudgetDialogConfirm : BudgetsListAction
    data object OnDialogDismiss : BudgetsListAction
    data object OnOnetimeBudgetCreateClick : BudgetsListAction
    data object OnBackClick : BudgetsListAction
}

sealed interface BudgetsListEvent {
    class NavigateToBudgetOverview(val id: Long, val intervalType: IntervalType?) : BudgetsListEvent
    class ShowError(val error: StringResource) : BudgetsListEvent
    class ShowMessage(val message: StringResource) : BudgetsListEvent
    data object NavigateToOnetimeBudgetCreate : BudgetsListEvent
    data object NavigateBack : BudgetsListEvent
}