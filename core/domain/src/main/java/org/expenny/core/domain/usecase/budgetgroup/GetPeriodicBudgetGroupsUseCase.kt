package org.expenny.core.domain.usecase.budgetgroup

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.expenny.core.domain.repository.BudgetGroupRepository
import org.expenny.core.domain.usecase.budget.GetBudgetsUseCase
import org.expenny.core.model.budgetgroup.BudgetGroup
import javax.inject.Inject

class GetPeriodicBudgetGroupsUseCase @Inject constructor(
    private val budgetGroupRepository: BudgetGroupRepository,
    private val getBudgets: GetBudgetsUseCase
) {

    operator fun invoke(): Flow<List<BudgetGroup.Periodic>> {
        return budgetGroupRepository.getBudgetGroups().map { budgetGroups ->
            budgetGroups
                .filterIsInstance<BudgetGroup.Periodic>()
                .map { budgetGroup ->
                    val budgets = getBudgets(
                        budgetGroupId = budgetGroup.id,
                        dateRange = budgetGroup.intervalType.dateRangeAtZeroOffset
                    ).first()

                    BudgetGroup.Periodic(
                        id = budgetGroup.id,
                        profile = budgetGroup.profile,
                        currency = budgetGroup.currency,
                        intervalType = budgetGroup.intervalType,
                        budgets = budgets,
                    )
                }
        }
    }
}