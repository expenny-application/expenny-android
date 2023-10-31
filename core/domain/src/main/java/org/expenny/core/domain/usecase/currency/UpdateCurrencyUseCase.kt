package org.expenny.core.domain.usecase.currency

import org.expenny.core.domain.repository.CurrencyRepository
import org.expenny.core.model.currency.CurrencyUpdate
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class UpdateCurrencyUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository,
) {

    suspend operator fun invoke(params: Params) {
        val quoteRate = params.quoteToBaseRate
        val baseRate = BigDecimal.ONE.divide(quoteRate, quoteRate.scale(), RoundingMode.HALF_UP)

        currencyRepository.updateCurrency(
            CurrencyUpdate(
                id = params.id,
                baseToQuoteRate = baseRate,
                isSubscribedToRateUpdates = params.isSubscribedToRateUpdates,
            )
        )
    }

    data class Params(
        val id: Long,
        val quoteToBaseRate: BigDecimal,
        val isSubscribedToRateUpdates: Boolean
    )
}