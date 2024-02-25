package org.expenny.core.model.category

import org.expenny.core.model.currency.CurrencyAmount

data class CategoryExpenses(
    val amount: CurrencyAmount,
    val expenses: List<CategoryStatement>,
)