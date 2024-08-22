package org.expenny.core.model.budgetgroup

import java.time.LocalDate

data class BudgetGroupUpdate(
    val id: Long,
    val name: String,
    val dateRange: ClosedRange<LocalDate>,
)
