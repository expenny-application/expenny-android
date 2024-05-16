package org.expenny.core.domain.usecase.rate

import kotlinx.coroutines.flow.Flow
import org.expenny.core.domain.repository.CurrencyRateRepository
import org.expenny.core.model.currency.CurrencyRate
import org.expenny.core.common.utils.RemoteResult
import javax.inject.Inject

class GetLatestCurrencyRateUseCase @Inject constructor(
    private val currencyRateRepository: CurrencyRateRepository
) {

    operator fun invoke(params: Params): Flow<RemoteResult<CurrencyRate>> {
        return currencyRateRepository.getLatestRateFlow(params.base, params.quote)
    }

    data class Params(
        val base: String,
        val quote: String
    )
}