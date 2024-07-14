package org.expenny.core.ui.data

import java.math.BigDecimal

data class BudgetUi(
    val id: Long,
    val category: CategoryUi,
    val limit: AmountUi,
    val spentValue: BigDecimal,
    val leftValue: BigDecimal,
    val progressValue: Float,
    val progressPercentage: Int
)
