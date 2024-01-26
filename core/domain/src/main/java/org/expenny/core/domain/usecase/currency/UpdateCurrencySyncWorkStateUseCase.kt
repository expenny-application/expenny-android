package org.expenny.core.domain.usecase.currency

import kotlinx.coroutines.flow.first
import org.expenny.core.domain.repository.CurrencyRepository
import org.expenny.core.domain.repository.WorkRepository
import javax.inject.Inject

class UpdateCurrencySyncWorkStateUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    private val workRepository: WorkRepository,
) {

    suspend operator fun invoke() {
        val isWorkEnqueued = workRepository.isCurrencySyncWorkEnqueued()
        val hasSubscribedCurrencies = currencyRepository.getCurrencies().first().any {
            it.isSubscribedToUpdates
        }

        if (!isWorkEnqueued && hasSubscribedCurrencies) {
            workRepository.enqueueCurrencySyncWork()
        } else if (isWorkEnqueued && !hasSubscribedCurrencies) {
            workRepository.cancelCurrencySyncWork()
        }
    }
}