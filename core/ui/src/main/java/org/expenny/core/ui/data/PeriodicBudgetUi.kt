package org.expenny.core.ui.data

import org.expenny.core.common.types.IntervalType

data class PeriodicBudgetUi(
    val id: Long,
    val leftAmount: AmountUi,
    val spentValue: AmountUi,
    val limitValue: AmountUi,
    val progressValue: Float,
    val categories: List<CategoryUi>,
    val intervalType: IntervalType,
)
