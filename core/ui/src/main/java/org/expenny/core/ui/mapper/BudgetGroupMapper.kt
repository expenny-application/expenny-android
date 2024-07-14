package org.expenny.core.ui.mapper

import org.expenny.core.model.budgetgroup.BudgetGroup
import org.expenny.core.model.currency.CurrencyAmount
import org.expenny.core.ui.data.BudgetUi
import org.expenny.core.ui.data.BudgetGroupUi
import javax.inject.Inject

class BudgetGroupMapper @Inject constructor(
    private val amountMapper: AmountMapper,
    private val categoryMapper: CategoryMapper,
) {

    operator fun invoke(model: BudgetGroup.Periodic): BudgetGroupUi {
        return with(model) {
            BudgetGroupUi(
                leftAmount = amountMapper(CurrencyAmount(model.totalLeftValue, model.currency)),
                spentValue = totalSpentValue,
                limitValue = totalLimitValue,
                progressValue = totalProgressPercentage.toFloat().div(100f),
                budgets = model.budgets.map {
                    BudgetUi(
                        id = it.id,
                        category = categoryMapper(it.category),
                        limit = amountMapper(CurrencyAmount(it.limitValue, model.currency)),
                        spentValue = it.spentValue,
                        leftValue = it.leftValue,
                        progressValue = it.progressPercentage.toFloat().div(100f),
                        progressPercentage = it.progressPercentage.toInt()
                    )
                }
            )
        }
    }
}