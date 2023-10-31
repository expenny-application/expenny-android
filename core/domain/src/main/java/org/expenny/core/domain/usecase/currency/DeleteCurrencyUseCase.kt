package org.expenny.core.domain.usecase.currency

import org.expenny.core.domain.repository.CurrencyRepository
import javax.inject.Inject

class DeleteCurrencyUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {

    suspend operator fun invoke(currencyId: Long) {
        currencyRepository.deleteCurrency(currencyId)
    }
}