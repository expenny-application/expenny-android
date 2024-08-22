package org.expenny.core.model.budget

import java.math.BigDecimal
import java.time.LocalDate

data class BudgetUpdate(
    val id: Long,
    val limitValue: BigDecimal,
    val endDate: LocalDate?
)
