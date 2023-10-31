package org.expenny.core.model.expenses

import org.expenny.core.model.currency.CurrencyAmount
import org.expenny.core.model.category.Category

data class CategoryExpenses(
    val amount: CurrencyAmount,
    val expensesCount: Int,
    val category: Category?
)
