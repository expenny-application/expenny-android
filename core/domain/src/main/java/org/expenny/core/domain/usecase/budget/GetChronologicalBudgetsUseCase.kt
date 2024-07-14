package org.expenny.core.domain.usecase.budget

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.expenny.core.domain.repository.BudgetRepository
import org.expenny.core.model.budget.Budget
import javax.inject.Inject

class GetChronologicalBudgetsUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {

    operator fun invoke(budgetGroupId: Long): Flow<List<Budget>> {
        return budgetRepository.getBudgets(budgetGroupId).map { budgets ->
            budgets.sortedBy { it.startDate }
        }
    }
}