package org.expenny.core.domain.usecase.budgetgroup

import kotlinx.coroutines.flow.first
import org.expenny.core.common.types.IntervalType
import org.expenny.core.domain.repository.BudgetGroupRepository
import org.expenny.core.domain.usecase.currency.GetMainCurrencyUseCase
import org.expenny.core.domain.usecase.profile.GetCurrentProfileUseCase
import org.expenny.core.model.budgetgroup.BudgetGroupCreate
import javax.inject.Inject

class CreatePeriodicBudgetGroupUseCase @Inject constructor(
    private val budgetGroupRepository: BudgetGroupRepository,
    private val getCurrentProfile: GetCurrentProfileUseCase,
    private val getMainCurrency: GetMainCurrencyUseCase
) {

    suspend operator fun invoke(params: Params): Long {
        val profileId = getCurrentProfile().first()!!.id
        val currencyId = getMainCurrency().first()!!.id

        return budgetGroupRepository.createBudgetGroup(
            BudgetGroupCreate(
                profileId = profileId,
                currencyId = currencyId,
                intervalType = params.intervalType,
                name = null,
                dateRange = null
            )
        )
    }

    data class Params(
        val intervalType: IntervalType
    )
}