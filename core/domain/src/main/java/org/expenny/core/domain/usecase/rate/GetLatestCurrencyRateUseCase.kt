package org.expenny.core.domain.usecase.rate

import kotlinx.coroutines.flow.Flow
import org.expenny.core.domain.repository.CurrencyRateRepository
import org.expenny.core.model.currency.CurrencyRate
import org.expenny.core.model.resource.RemoteResult
import javax.inject.Inject

class GetLatestCurrencyRateUseCase @Inject constructor(
    private val currencyRateRepository: CurrencyRateRepository
) {

    operator fun invoke(base: String, quote: String): Flow<RemoteResult<CurrencyRate>> {
        return currencyRateRepository.getLatestRateFlow(base, quote)
    }
}