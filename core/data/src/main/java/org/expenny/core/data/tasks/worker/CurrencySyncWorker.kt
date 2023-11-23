package org.expenny.core.data.tasks.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import org.expenny.core.domain.repository.CurrencyRateRepository
import org.expenny.core.domain.repository.CurrencyRepository
import org.expenny.core.model.currency.Currency
import org.expenny.core.model.currency.CurrencyRate
import org.expenny.core.model.currency.CurrencyUpdate
import org.expenny.core.model.resource.ResourceResult
import java.time.LocalDate

@HiltWorker
class CurrencySyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val currencyRateRepository: CurrencyRateRepository,
    private val currencyRepository: CurrencyRepository
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val eligibleCurrencies = currencyRepository.getEligibleCurrencies()

        val latestRates = eligibleCurrencies
            .groupBy { it.profile.currencyUnit.code }
            .flatMap { (baseCurrencyCode, quoteCurrencies) ->
                val quoteCurrencyCodes = quoteCurrencies.map { it.unit.code }
                getLatestRates(baseCurrencyCode, quoteCurrencyCodes)
            }

        val currenciesToUpdate = eligibleCurrencies.mapNotNull { currency ->
            latestRates.getByQuoteCurrencyOrNull(currency.unit.code)?.let { currencyRate ->
                CurrencyUpdate(
                    id = currency.id,
                    baseToQuoteRate = currencyRate.rate,
                    isSubscribedToRateUpdates = currency.isSubscribedToRateUpdates
                )
            }
        }

        currencyRepository.updateCurrencies(currenciesToUpdate)

        return Result.success()
    }

    private suspend fun CurrencyRepository.getEligibleCurrencies(): List<Currency> {
        return getCurrencies().first().filter {
            it.isSubscribedToRateUpdates && !it.isMain && it.updatedAt.toLocalDate() != LocalDate.now()
        }
    }

    private fun List<CurrencyRate>.getByQuoteCurrencyOrNull(quoteCurrencyCode: String): CurrencyRate? {
        return firstOrNull { it.quoteCurrencyUnit.code == quoteCurrencyCode }
    }

    private suspend fun getLatestRates(base: String, quotes: List<String>): List<CurrencyRate> {
        return currencyRateRepository.getLatestRatesFlow(base, quotes)
            .filterIsInstance<ResourceResult.Success<List<CurrencyRate>>>()
            .first()
            .data
            .orEmpty()
    }
}