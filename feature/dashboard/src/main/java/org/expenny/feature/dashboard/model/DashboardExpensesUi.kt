package org.expenny.feature.dashboard.model

import org.expenny.core.common.extensions.isWhole
import org.expenny.core.common.extensions.percentageOf
import org.expenny.core.ui.data.AmountUi
import org.expenny.core.ui.data.ExpensesUi

data class DashboardExpensesUi(
    val totalAmount: AmountUi? = null,
    val selectedEntry: ExpensesUi? = null,
    val entries: List<ExpensesUi> = emptyList(),
) {
    val selectedEntryIndex: Int
        get() = selectedEntry?.let { entries.indexOf(it) } ?: -1

    val expensesCount: Int
        get() = selectedEntry?.expensesCount ?: entries.sumOf { it.expensesCount }

    val expensesLegend: List<ExpensesUi>
        get() = entries.reversed().take(5)

    val expensesAmount: AmountUi?
        get() = selectedEntry?.totalAmount ?: totalAmount

    val expensePercentage: AmountUi?
        get() {
            if (selectedEntry == null || totalAmount == null) return null

            val percentageValue = selectedEntry.value.percentageOf(totalAmount.value)
            val percentageDisplayValue = buildString {
                if (!percentageValue.isWhole()) append("≈")
                append("$percentageValue%")
            }

            return AmountUi(
                value = percentageValue,
                displayValue = percentageDisplayValue,
            )
        }
}