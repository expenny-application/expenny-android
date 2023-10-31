package org.expenny.core.domain.usecase.currencyunit

import org.expenny.core.domain.repository.CurrencyUnitRepository
import org.expenny.core.model.currency.CurrencyUnit
import javax.inject.Inject

class GetCurrencyUnitUseCase @Inject constructor(
    private val currencyUnitRepository: CurrencyUnitRepository,
) {

    operator fun invoke(params: Params): CurrencyUnit? {
        return currencyUnitRepository.getCurrencyUnit(params.id)
    }

    data class Params(
        val id: Long
    )
}