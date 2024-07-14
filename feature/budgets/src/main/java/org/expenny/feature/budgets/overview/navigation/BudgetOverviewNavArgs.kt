package org.expenny.feature.budgets.overview.navigation

import org.expenny.core.common.types.BudgetType
import org.expenny.core.common.types.IntervalType

data class BudgetOverviewNavArgs(
    val budgetGroupId: Long,
    val intervalType: IntervalType? = null,
    val budgetType: BudgetType
)
