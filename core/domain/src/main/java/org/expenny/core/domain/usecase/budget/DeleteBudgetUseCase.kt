package org.expenny.core.domain.usecase.budget

import kotlinx.coroutines.flow.first
import org.expenny.core.domain.repository.BudgetRepository
import org.expenny.core.model.budget.BudgetUpdate
import java.time.LocalDate
import javax.inject.Inject

class DeleteBudgetUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
) {

    suspend operator fun invoke(
        budgetId: Long,
        dateRange: ClosedRange<LocalDate>,
    ) {
        val budget = budgetRepository.getBudget(budgetId).first()!!

        if (budget.isOpened && budget.startDate < dateRange.start) {
            // close budget
            budgetRepository.updateBudget(
                BudgetUpdate(
                    id = budgetId,
                    limitValue = budget.limitValue,
                    endDate = dateRange.start.minusDays(1)
                )
            )
        } else {
            // delete budget
            budgetRepository.deleteBudget(budgetId)
        }
    }
}