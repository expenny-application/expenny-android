package org.expenny.core.ui.mapper

import org.expenny.core.model.budgetgroup.BudgetGroup
import org.expenny.core.model.currency.CurrencyAmount
import org.expenny.core.ui.data.PeriodicBudgetUi
import javax.inject.Inject

class PeriodicBudgetMapper @Inject constructor(
    private val amountMapper: AmountMapper,
    private val categoryMapper: CategoryMapper,
) {

    operator fun invoke(model: BudgetGroup.Periodic): PeriodicBudgetUi {
        val leftAmount = amountMapper(CurrencyAmount(model.totalLeftValue, model.currency))
        val spentAmount = amountMapper(CurrencyAmount(model.totalSpentValue, model.currency))
        val limitAmount = amountMapper(CurrencyAmount(model.totalLimitValue, model.currency))
        val categories = categoryMapper(model.budgets.map { it.category })

        return with(model) {
            PeriodicBudgetUi(
                id = id,
                leftAmount = leftAmount,
                spentValue = spentAmount,
                limitValue = limitAmount,
                progressValue = totalProgressPercentage.toFloat().div(100f),
                intervalType = intervalType,
                categories = categories,
            )
        }
    }

    operator fun invoke(model: List<BudgetGroup.Periodic>): List<PeriodicBudgetUi> {
        return model.map { invoke(it) }
    }
}