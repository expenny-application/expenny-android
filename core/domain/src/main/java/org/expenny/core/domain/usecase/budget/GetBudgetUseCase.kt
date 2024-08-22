package org.expenny.core.domain.usecase.budget

import kotlinx.coroutines.flow.Flow
import org.expenny.core.domain.repository.BudgetRepository
import org.expenny.core.model.budget.Budget
import javax.inject.Inject

class GetBudgetUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {

    operator fun invoke(id: Long): Flow<Budget?> {
        return budgetRepository.getBudget(id)
    }
}