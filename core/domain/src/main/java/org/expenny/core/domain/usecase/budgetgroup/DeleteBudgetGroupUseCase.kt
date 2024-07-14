package org.expenny.core.domain.usecase.budgetgroup

import org.expenny.core.domain.repository.BudgetGroupRepository
import javax.inject.Inject

class DeleteBudgetGroupUseCase @Inject constructor(
    private val budgetGroupRepository: BudgetGroupRepository
) {

    suspend operator fun invoke(id: Long) {
        budgetGroupRepository.deleteBudgetGroup(id)
    }
}