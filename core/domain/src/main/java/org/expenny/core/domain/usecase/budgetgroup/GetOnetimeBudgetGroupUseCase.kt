package org.expenny.core.domain.usecase.budgetgroup

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.expenny.core.model.budgetgroup.BudgetGroup
import javax.inject.Inject

class GetOnetimeBudgetGroupUseCase @Inject constructor(
    private val getOnetimeBudgets: GetOnetimeBudgetGroupsUseCase
) {

    operator fun invoke(
        budgetGroupId: Long
    ): Flow<BudgetGroup.Onetime?> {
        return getOnetimeBudgets().map { budgets ->
            budgets.firstOrNull { it.id == budgetGroupId }
        }
    }
}