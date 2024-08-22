package org.expenny.core.domain.usecase.budgetgroup

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.expenny.core.domain.repository.BudgetGroupRepository
import org.expenny.core.domain.usecase.budget.GetBudgetsUseCase
import org.expenny.core.model.budgetgroup.BudgetGroup
import javax.inject.Inject

class GetOnetimeBudgetGroupsUseCase @Inject constructor(
    private val budgetGroupRepository: BudgetGroupRepository,
    private val getBudgets: GetBudgetsUseCase
) {

    operator fun invoke(): Flow<List<BudgetGroup.Onetime>> {
        return budgetGroupRepository.getBudgetGroups().map { budgetGroups ->
            budgetGroups
                .filterIsInstance<BudgetGroup.Onetime>()
                .map { budgetGroup ->
                    val budgets = getBudgets(budgetGroup.id, budgetGroup.dateRange).first()

                    BudgetGroup.Onetime(
                        id = budgetGroup.id,
                        name = budgetGroup.name,
                        profile = budgetGroup.profile,
                        currency = budgetGroup.currency,
                        dateRange = budgetGroup.dateRange,
                        budgets = budgets,
                    )
                }
        }
    }
}