package org.expenny.core.data.repository

import android.icu.util.Currency
import kotlinx.coroutines.Dispatchers
import org.expenny.core.common.utils.Constants.CURRENCY_RATE_SCALE
import org.expenny.core.data.mapper.DataMapper.toModel
import org.expenny.core.data.utils.suspendCatching
import org.expenny.core.domain.repository.CurrencyRateRepository
import org.expenny.core.model.currency.CurrencyRate
import org.expenny.core.network.EcbService
import org.expenny.core.network.EcbService.Companion.ECB_BASE_UNIT
import org.expenny.core.network.dto.EcbRateReferenceDto
import java.math.BigDecimal
import java.math.RoundingMode.HALF_UP
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicInteger

class CurrencyRateRepositoryImpl @Inject constructor(
    private val ecbService: EcbService,
) : CurrencyRateRepository {

    override fun getLatestRateFlow(
        base: String,
        quote: String
    ) = suspendCatching {
        ecbService
            .getLatestEurBasedData(base, quote)
            .mapToCurrencyRates()
            .first {
                it.baseCurrencyUnit.code == base && it.quoteCurrencyUnit.code == quote
            }
    }

    override fun getLatestRatesFlow(
        base: String,
        quotes: List<String>
    ) = suspendCatching {
        ecbService
            .getLatestEurBasedData(base, *quotes.toTypedArray())
            .mapToCurrencyRates()
            .filter {
                it.baseCurrencyUnit.code == base && it.quoteCurrencyUnit.code in quotes
            }
    }

    override fun getRatesFlow(
        fromDate: LocalDate,
        quotes: List<String>
    ) = suspendCatching {
        ecbService
            .getEurBaseData(fromDate, *quotes.toTypedArray())
            .mapToCurrencyRates()
    }

    private suspend fun List<EcbRateReferenceDto>.mapToCurrencyRates(): List<CurrencyRate> {
        val numOfParallelTasks = 10
        val currentIndex = AtomicInteger(0)
        val result = mutableSetOf<CurrencyRate>()

        val deferredResult = withContext(Dispatchers.Default) {
            List(numOfParallelTasks) { taskIndex ->
                async {
                    val startIndex = currentIndex.getAndAdd(size / numOfParallelTasks)
                    val endIndex = if (taskIndex == numOfParallelTasks - 1) size else startIndex + (size / numOfParallelTasks)

                    val sublist = when {
                        startIndex == 0 && endIndex == 0 -> this@mapToCurrencyRates
                        else -> this@mapToCurrencyRates.subList(startIndex, endIndex)
                    }

                    val processedSublist = sublist
                        .groupBy { it.date }
                        .map { (date, refs) ->
                            val currencies = refs.asSequence()
                                .flatMap { listOf(it.baseCurrency, it.quoteCurrency) }
                                .distinct()
                                .toList()

                            val currencyPairs = buildList {
                                for (i in currencies.indices) {
                                    for (j in currencies.indices) {
                                        if (i != j) {
                                            add(currencies[i] to currencies[j])
                                        }
                                    }
                                }
                            }

                            currencyPairs.map { (base, quote) ->
                                val eurToBaseRate = getRateByCurrencyCode(base)
                                val eurToQuoteRate = getRateByCurrencyCode(quote)
                                val baseToQuoteRate = eurToQuoteRate.divideByRate(eurToBaseRate)

                                CurrencyRate(
                                    baseCurrencyUnit = Currency.getInstance(base).toModel(),
                                    quoteCurrencyUnit = Currency.getInstance(quote).toModel(),
                                    rate = baseToQuoteRate,
                                    date = date
                                )
                            }
                        }.flatten()

                    synchronized(result) {
                        result.addAll(processedSublist)
                    }
                }
            }
        }

        deferredResult.awaitAll()

        return result.sortedBy { it.date }
    }

    private fun BigDecimal.divideByRate(rate: BigDecimal): BigDecimal {
        return divide(rate, CURRENCY_RATE_SCALE, HALF_UP)
    }

    private fun List<EcbRateReferenceDto>.getRateByCurrencyCode(code: String): BigDecimal {
        return when (code) {
            ECB_BASE_UNIT -> BigDecimal.ONE
            else -> first { it.quoteCurrency == code }.value
        }
    }
}