package org.expenny.core.ui.data.ui

import androidx.compose.ui.graphics.Color
import java.math.BigDecimal

data class ExpensesUi(
    val totalAmount: AmountUi,
    val expensesCount: Int,
    override val value: BigDecimal,
    override val label: String?,
    override val color: Color?
) : ChartEntryUi
