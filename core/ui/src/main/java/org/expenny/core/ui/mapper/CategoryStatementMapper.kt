package org.expenny.core.ui.mapper

import org.expenny.core.model.category.CategoryStatement
import org.expenny.core.ui.data.CategoryStatementUi
import javax.inject.Inject

class CategoryStatementMapper @Inject constructor(
    private val amountMapper: AmountMapper,
    private val categoryMapper: CategoryMapper
) {

    operator fun invoke(model: CategoryStatement): CategoryStatementUi {
        return CategoryStatementUi(
            amount = amountMapper(model.amount),
            transactionsCount = model.transactions.size,
            category = model.category?.let { categoryMapper(it) }
        )
    }

    operator fun invoke(model: List<CategoryStatement>): List<CategoryStatementUi> {
        return model.map { invoke(it) }
    }
}