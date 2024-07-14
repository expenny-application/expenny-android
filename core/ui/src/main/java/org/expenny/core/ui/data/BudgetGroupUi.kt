package org.expenny.core.ui.data

import java.math.BigDecimal

data class BudgetGroupUi(
    val leftAmount: AmountUi? = null,
    val currentValue: BigDecimal = BigDecimal.ZERO,
    val limitValue: BigDecimal = BigDecimal.ZERO,
    val progressValue: Float = 0f,
    val budgets: List<BudgetUi> = emptyList()
)
