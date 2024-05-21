package org.expenny.core.domain.usecase.currency

import org.expenny.core.domain.repository.CurrencyRepository
import org.expenny.core.model.currency.CurrencyUpdate
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class UpdateCurrencyUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    private val updateCurrencySyncWorkState: UpdateCurrencySyncWorkStateUseCase
) {

    suspend operator fun invoke(params: Params) {
        currencyRepository.updateCurrency(
            CurrencyUpdate(
                id = params.id,
                baseToQuoteRate = params.baseToQuoteRate,
                isSubscribedToRateUpdates = params.isSubscribedToRateUpdates,
            )
        ).also { updateCurrencySyncWorkState() }
    }

    data class Params(
        val id: Long,
        val baseToQuoteRate: BigDecimal,
        val isSubscribedToRateUpdates: Boolean
    )
}