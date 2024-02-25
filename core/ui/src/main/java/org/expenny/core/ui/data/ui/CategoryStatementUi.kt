package org.expenny.core.ui.data.ui

data class CategoryStatementUi(
    val amount: AmountUi,
    val transactionsCount: Int,
    val category: CategoryUi?,
)
