package org.expenny.core.domain.usecase.budgetgroup

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.expenny.core.domain.repository.BudgetGroupRepository
import org.expenny.core.model.currency.Currency
import javax.inject.Inject

class GetBudgetGroupCurrencyUseCase @Inject constructor(
    private val budgetGroupRepository: BudgetGroupRepository,
) {

    operator fun invoke(id: Long): Flow<Currency?> {
        return budgetGroupRepository.getBudgetGroups().map { budgetGroups ->
            budgetGroups.firstOrNull { it.id == id }?.currency
        }
    }
}