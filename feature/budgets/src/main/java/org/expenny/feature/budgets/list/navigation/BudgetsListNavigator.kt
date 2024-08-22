package org.expenny.feature.budgets.list.navigation

import org.expenny.core.common.types.IntervalType

interface BudgetsListNavigator {
    fun navigateToBudgetOverviewScreen(id: Long, intervalType: IntervalType?)
    fun navigateToCreateOnetimeBudgetScreen()
    fun navigateBack()
}