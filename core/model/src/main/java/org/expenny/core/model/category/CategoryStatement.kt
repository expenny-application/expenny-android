package org.expenny.core.model.category

import org.expenny.core.model.currency.CurrencyAmount
import org.expenny.core.model.record.Record

data class CategoryStatement(
    val amount: CurrencyAmount,
    val transactions: List<Record.Transaction>,
    val category: Category?,
)
