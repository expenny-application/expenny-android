package org.expenny.core.domain.usecase.budget

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.expenny.core.domain.repository.BudgetRepository
import org.expenny.core.domain.usecase.profile.GetCurrentProfileUseCase
import org.expenny.core.model.budget.BudgetCreate
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

class CreateBudgetUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val getCurrentProfile: GetCurrentProfileUseCase
) {

    suspend operator fun invoke(
        budgetGroupId: Long,
        categoryId: Long,
        limitValue: BigDecimal,
        isOpened: Boolean,
        dateRange: ClosedRange<LocalDate>
    ): Long {
        val profileId = getCurrentProfile().first()!!.id
        val endDate = if (isOpened) null else dateRange.endInclusive

        return budgetRepository.createBudget(
            BudgetCreate(
                profileId = profileId,
                budgetGroupId = budgetGroupId,
                categoryId = categoryId,
                limitValue = limitValue,
                startDate = dateRange.start,
                endDate = endDate
            )
        )
    }
}