package org.expenny.core.ui.data

import org.expenny.core.common.types.IntervalType
import java.math.BigDecimal

data class PeriodicBudgetUi(
    val id: Long,
    val leftAmount: AmountUi,
    val spentValue: BigDecimal,
    val limitValue: BigDecimal,
    val progressValue: Float,
    val categories: List<CategoryUi>,
    val intervalType: IntervalType,
)
