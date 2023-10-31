package org.expenny.core.model.expenses

import org.expenny.core.model.currency.CurrencyAmount

data class CategoryExpensesData(
    val totalAmount: CurrencyAmount,
    val expenses: List<CategoryExpenses>,
)