package org.expenny.core.domain.usecase.currency

import kotlinx.coroutines.flow.first
import org.expenny.core.domain.usecase.profile.GetCurrentProfileUseCase
import org.expenny.core.domain.repository.CurrencyRepository
import org.expenny.core.model.currency.CurrencyCreate
import java.math.BigDecimal
import javax.inject.Inject

class CreateCurrencyUseCase @Inject constructor(
    private val getCurrentProfile: GetCurrentProfileUseCase,
    private val currencyRepository: CurrencyRepository,
    private val updateCurrencySyncWorkState: UpdateCurrencySyncWorkStateUseCase
) {

    suspend operator fun invoke(params: Params): Long {
        val profileId = getCurrentProfile().first()!!.id

        return currencyRepository.createCurrency(
            CurrencyCreate(
                profileId = profileId,
                code = params.currencyUnitCode,
                baseToQuoteRate = params.baseToQuoteRate,
                isSubscribedToRateUpdates = params.isSubscribedToRateUpdates
            )
        ).also { updateCurrencySyncWorkState() }
    }

    data class Params(
        val currencyUnitCode: String,
        val baseToQuoteRate: BigDecimal,
        val isSubscribedToRateUpdates: Boolean
    )
}