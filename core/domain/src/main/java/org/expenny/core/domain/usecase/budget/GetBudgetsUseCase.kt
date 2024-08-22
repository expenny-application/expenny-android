package org.expenny.core.domain.usecase.budget

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.expenny.core.common.extensions.toDateTimeRange
import org.expenny.core.common.types.TransactionType
import org.expenny.core.domain.repository.BudgetGroupRepository
import org.expenny.core.domain.usecase.category.GetCategoriesStatementsUseCase
import org.expenny.core.model.budget.Budget
import org.expenny.core.model.budgetgroup.BudgetGroup
import org.expenny.core.model.currency.Currency
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

class GetBudgetsUseCase @Inject constructor(
    private val budgetGroupRepository: BudgetGroupRepository,
    private val getCategoriesStatements: GetCategoriesStatementsUseCase,
) {

    operator fun invoke(
        budgetGroupId: Long,
        dateRange: ClosedRange<LocalDate>,
        accountIds: List<Long> = emptyList(),
    ): Flow<List<Budget>> {
        return budgetGroupRepository.getBudgetGroups().map { budgetGroups ->
            val budgetGroup = budgetGroups.firstByIdOrNull(budgetGroupId)

            if (budgetGroup != null) {
                val categoriesStatements = getCategoriesStatements(accountIds, dateRange, budgetGroup.currency)
                val getSpentValue: (Long) -> BigDecimal? = { categoryId ->
                    categoriesStatements.firstOrNull { it.category?.id == categoryId }?.amount?.value
                }
                budgetGroup.getBudgetDetailsList(dateRange, getSpentValue)
            } else {
                emptyList()
            }
        }
    }

    private fun List<BudgetGroup>.firstByIdOrNull(id: Long) = firstOrNull { it.id == id }

    private fun BudgetGroup.getBudgetDetailsList(
        dateRange: ClosedRange<LocalDate>,
        getSpentValue: (categoryId: Long) -> BigDecimal?,
    ): List<Budget> =
        budgets
            .filter {
                val isEnclosedBudget = it.endDate != null && it.startDate == dateRange.start && it.endDate == dateRange.endInclusive
                val isOpenedBudget = it.endDate == null && it.startDate <= dateRange.start

                return@filter isEnclosedBudget || isOpenedBudget
            }
            .map { budget ->
                val defaultSpentValue = BigDecimal.ZERO.setScale(currency.unit.scale)
                val spentValue = getSpentValue(budget.category.id) ?: defaultSpentValue

                return@map budget.copy(spentValue = spentValue)
            }.sortedByDescending { it.progressPercentage }

    private suspend fun getCategoriesStatements(
        accountIds: List<Long>,
        dateRange: ClosedRange<LocalDate>,
        currency: Currency
    ) = getCategoriesStatements(
            accountIds = accountIds,
            dateTimeRange = dateRange.toDateTimeRange(),
            transactionType = TransactionType.Outgoing
        ).first().map {
            it.copy(amount = it.amount.convertTo(currency).abs())
        }
}