package org.expenny.feature.budgets.list.contract

import org.expenny.core.common.models.StringResource
import org.expenny.core.common.types.BudgetType
import org.expenny.core.common.types.IntervalType
import org.expenny.core.ui.data.PeriodicBudgetUi

data class BudgetsListState(
    val budgetTypes: List<BudgetType> = BudgetType.entries,
    val selectedBudgetType: BudgetType = BudgetType.Periodic,
    val periodicBudgets: List<PeriodicBudgetUi> = emptyList(),
) {
    val showAddButton: Boolean
        get() = selectedBudgetType == BudgetType.Onetime

    private val budgetIntervalTypes: List<IntervalType> =
        listOf(IntervalType.Week, IntervalType.Month, IntervalType.Quarter, IntervalType.Year)

    val availableBudgetIntervalType: List<IntervalType>
        get() = budgetIntervalTypes.filter { budgetIntervalType ->
            !periodicBudgets.map { it.intervalType }.contains(budgetIntervalType)
        }
}

sealed interface BudgetsListAction {
    class OnPeriodicBudgetClick(val id: Long, val type: IntervalType) : BudgetsListAction
    class OnPeriodicBudgetCreateClick(val type: IntervalType) : BudgetsListAction
    class OnBudgetTypeChange(val type: BudgetType) : BudgetsListAction
    data object OnOnetimeBudgetCreateClick : BudgetsListAction
    data object OnBackClick : BudgetsListAction
}

sealed interface BudgetsListEvent {
    class NavigateToBudgetOverview(val id: Long, val intervalType: IntervalType, val budgetType: BudgetType) : BudgetsListEvent
    class ShowError(val error: StringResource) : BudgetsListEvent
    data object NavigateToOnetimeBudgetCreate : BudgetsListEvent
    data object NavigateBack : BudgetsListEvent
}