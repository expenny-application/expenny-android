package org.expenny.core.domain.usecase.currency

import kotlinx.coroutines.flow.first
import org.expenny.core.domain.usecase.profile.GetCurrentProfileUseCase
import org.expenny.core.domain.repository.CurrencyRepository
import org.expenny.core.domain.repository.CurrencyUnitRepository
import org.expenny.core.model.currency.CurrencyCreate
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class CreateCurrencyUseCase @Inject constructor(
    private val getCurrentProfile: GetCurrentProfileUseCase,
    private val currencyRepository: CurrencyRepository,
    private val currencyUnitRepository: CurrencyUnitRepository
) {

    suspend operator fun invoke(params: Params): Long {
        val currencyCode = currencyUnitRepository.getCurrencyUnit(params.currencyUnitId)!!.code
        val profileId = getCurrentProfile().first()!!.id
        val quoteRate = params.quoteToBaseRate
        val baseRate = BigDecimal.ONE.divide(quoteRate, quoteRate.scale(), RoundingMode.HALF_UP)

        return currencyRepository.createCurrency(
            CurrencyCreate(
                profileId = profileId,
                code = currencyCode,
                baseToQuoteRate = baseRate
            )
        )
    }

    data class Params(
        val currencyUnitId: Long,
        val quoteToBaseRate: BigDecimal
    )
}