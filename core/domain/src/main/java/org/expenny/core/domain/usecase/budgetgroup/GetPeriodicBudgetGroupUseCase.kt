package org.expenny.core.domain.usecase.budgetgroup

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.expenny.core.domain.repository.BudgetGroupRepository
import org.expenny.core.domain.usecase.budget.GetBudgetsUseCase
import org.expenny.core.model.budgetgroup.BudgetGroup
import java.time.LocalDate
import javax.inject.Inject

class GetPeriodicBudgetGroupUseCase @Inject constructor(
    private val budgetGroupRepository: BudgetGroupRepository,
    private val getBudgets: GetBudgetsUseCase
) {

    operator fun invoke(
        budgetGroupId: Long,
        dateRange: ClosedRange<LocalDate>
    ): Flow<BudgetGroup.Periodic?> {
        return budgetGroupRepository.getBudgetGroups().map { budgetGroups ->
            budgetGroups
                .filterIsInstance<BudgetGroup.Periodic>()
                .firstOrNull { it.id == budgetGroupId }
                ?.let { budgetGroup ->
                    val budgets = getBudgets(budgetGroup.id, dateRange).first()

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