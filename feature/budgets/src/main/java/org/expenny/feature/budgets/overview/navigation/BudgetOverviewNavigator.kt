package org.expenny.feature.budgets.overview.navigation

import org.expenny.core.common.types.BudgetType
import java.time.LocalDate

interface BudgetOverviewNavigator {
    fun navigateBack()
    fun navigateToCurrencySelectionListScreen(selectedId: Long?)
    fun navigateToBudgetLimitDetailsScreen(
        budgetId: Long? = null,
        budgetGroupId: Long,
        budgetType: BudgetType,
        startDate: LocalDate,
        endDate: LocalDate,
        excludeCategoriesIds: LongArray,
    )
}