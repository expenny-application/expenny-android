package org.expenny.feature.budgets.list.navigation

import org.expenny.core.common.types.BudgetType
import org.expenny.core.common.types.IntervalType

interface BudgetsListNavigator {
    fun navigateToPeriodicBudgetOverviewScreen(id: Long, intervalType: IntervalType, budgetType: BudgetType)
    fun navigateToCreateOnetimeBudgetScreen()
    fun navigateBack()
}