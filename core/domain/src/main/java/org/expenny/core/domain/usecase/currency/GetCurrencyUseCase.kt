package org.expenny.core.domain.usecase.currency

import kotlinx.coroutines.flow.Flow
import org.expenny.core.domain.repository.CurrencyRepository
import org.expenny.core.model.currency.Currency
import javax.inject.Inject

class GetCurrencyUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository,
) {

    operator fun invoke(params: Params): Flow<Currency?> {
        return currencyRepository.getCurrency(params.id)
    }

    data class Params(
        val id: Long
    )
}