package org.expenny.core.ui.mapper

import android.content.Context
import androidx.compose.ui.graphics.Color
import dagger.hilt.android.qualifiers.ApplicationContext
import org.expenny.core.model.category.CategoryStatement
import org.expenny.core.resources.R
import org.expenny.core.ui.data.ExpensesUi
import javax.inject.Inject

class ExpensesMapper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val amountMapper: AmountMapper
) {

    operator fun invoke(model: CategoryStatement): ExpensesUi {
        return ExpensesUi(
            totalAmount = amountMapper(model.amount),
            expensesCount = model.transactions.size,
            value = model.amount.value,
            label = model.category?.name ?: context.getString(R.string.unknown_label),
            color = model.category?.colorArgb?.let { Color(it) }
        )
    }

    operator fun invoke(model: List<CategoryStatement>): List<ExpensesUi> {
        return model.map { invoke(it) }
    }
}