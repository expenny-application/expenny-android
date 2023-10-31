package org.expenny.core.domain.usecase.rate

import kotlinx.coroutines.flow.Flow
import org.expenny.core.domain.repository.CurrencyRateRepository
import org.expenny.core.model.currency.CurrencyRate
import org.expenny.core.model.resource.ResourceResult
import java.time.LocalDate
import javax.inject.Inject

class GetCurrencyRatesUseCase @Inject constructor(
    private val currencyRateRepository: CurrencyRateRepository
) {

    operator fun invoke(fromDate: LocalDate, quotes: List<String> = emptyList()): Flow<ResourceResult<List<CurrencyRate>>> {
        return currencyRateRepository.getRatesFlow(fromDate, quotes)
    }
}