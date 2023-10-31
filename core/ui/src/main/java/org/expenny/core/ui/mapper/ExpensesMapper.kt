package org.expenny.core.ui.mapper

import android.content.Context
import androidx.compose.ui.graphics.Color
import dagger.hilt.android.qualifiers.ApplicationContext
import org.expenny.core.model.expenses.CategoryExpenses
import org.expenny.core.ui.data.ui.ExpensesUi
import org.expenny.core.resources.R
import javax.inject.Inject

class ExpensesMapper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val amountMapper: AmountMapper
) {

    operator fun invoke(model: CategoryExpenses): ExpensesUi {
        return ExpensesUi(
            totalAmount = amountMapper(model.amount),
            expensesCount = model.expensesCount,
            value = model.amount.value,
            label = model.category?.name ?: context.getString(R.string.unknown_label),
            color = model.category?.colorValue?.let { Color(it) }
        )
    }

    operator fun invoke(model: List<CategoryExpenses>): List<ExpensesUi> {
        return model.map { invoke(it) }
    }
}