package org.expenny.core.model.budget

import java.math.BigDecimal
import java.time.LocalDate

data class BudgetCreate(
    val budgetGroupId: Long,
    val profileId: Long,
    val categoryId: Long,
    val limitValue: BigDecimal,
    val startDate: LocalDate,
    val endDate: LocalDate?
)
