package org.expenny.feature.budgets.limit.navigation

import org.expenny.core.common.types.BudgetType
import java.time.LocalDate

data class BudgetLimitDetailsNavArgs(
    val budgetId: Long? = null,
    val budgetGroupId: Long,
    val budgetType: BudgetType,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val excludeCategoriesIds: LongArray,
)
