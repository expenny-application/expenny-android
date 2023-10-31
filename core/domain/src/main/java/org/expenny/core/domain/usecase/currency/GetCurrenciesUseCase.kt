package org.expenny.core.domain.usecase.currency

import kotlinx.coroutines.flow.Flow
import org.expenny.core.domain.repository.CurrencyRepository
import org.expenny.core.model.currency.Currency
import javax.inject.Inject

class GetCurrenciesUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {

    operator fun invoke() : Flow<List<Currency>> {
        return currencyRepository.getCurrencies()
    }
}