package org.expenny.core.domain.usecase.budget

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.expenny.core.domain.repository.BudgetGroupRepository
import org.expenny.core.domain.repository.BudgetRepository
import org.expenny.core.domain.usecase.profile.GetCurrentProfileUseCase
import org.expenny.core.model.budget.BudgetCreate
import org.expenny.core.model.budget.BudgetUpdate
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

class UpdateBudgetUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val getCurrentProfile: GetCurrentProfileUseCase
) {

    suspend operator fun invoke(
        id: Long,
        budgetGroupId: Long,
        limitValue: BigDecimal,
        isOpened: Boolean,
        dateRange: ClosedRange<LocalDate>,
    ) {
        val profile = getCurrentProfile().first()!!
        val budget = budgetRepository.getBudget(id).first()!!
        val endDate = if (isOpened) null else dateRange.endInclusive

        if (budget.isOpened && budget.startDate < dateRange.start) {
            // close budget
            budgetRepository.updateBudget(
                BudgetUpdate(
                    id = id,
                    limitValue = budget.limitValue,
                    endDate = dateRange.start.minusDays(1)
                )
            )
            // create new budget
            budgetRepository.createBudget(
                BudgetCreate(
                    profileId = profile.id,
                    budgetGroupId = budgetGroupId,
                    categoryId = budget.category.id,
                    limitValue = limitValue,
                    startDate = dateRange.start,
                    endDate = endDate
                )
            )
        } else {
            // update budget
            budgetRepository.updateBudget(
                BudgetUpdate(
                    id = id,
                    limitValue = limitValue,
                    endDate = endDate
                )
            )
        }
    }
}