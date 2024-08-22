package org.expenny.core.model.budgetgroup

import org.expenny.core.common.types.IntervalType
import java.time.LocalDate

data class BudgetGroupCreate(
    val profileId: Long,
    val currencyId: Long,
    val intervalType: IntervalType?,
    val name: String?,
    val dateRange: ClosedRange<LocalDate>?,
)
