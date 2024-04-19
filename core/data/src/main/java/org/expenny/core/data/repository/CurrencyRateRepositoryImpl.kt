package org.expenny.core.data.repository

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.expenny.core.common.utils.Constants.CURRENCY_RATE_SCALE
import org.expenny.core.data.mapper.DataMapper.toModel
import org.expenny.core.data.utils.remoteResultMediator
import org.expenny.core.domain.repository.CurrencyRateRepository
import org.expenny.core.model.currency.CurrencyRate
import org.expenny.core.network.EcbService
import org.expenny.core.network.EcbService.Companion.ECB_BASE_UNIT
import org.expenny.core.network.dto.EcbRateReferenceDto
import java.math.BigDecimal
import java.math.RoundingMode.HALF_UP
import java.time.LocalDate
import java.util.Currency
import javax.inject.Inject

class CurrencyRateRepositoryImpl @Inject constructor(
    private val ecbService: EcbService,
) : CurrencyRateRepository {

    override fun getLatestRateFlow(
        base: String,
        quote: String
    ) = remoteResultMediator {
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
    ) = remoteResultMediator {
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
    ) = remoteResultMediator {
        ecbService
            .getEurBaseData(fromDate, *quotes.toTypedArray())
            .mapToCurrencyRates()
    }

    private suspend fun List<EcbRateReferenceDto>.mapToCurrencyRates(): List<CurrencyRate> = coroutineScope {
        val initialReferences = this@mapToCurrencyRates

        val referencesMap = async { initialReferences.getDateToRatesMap() }.await()
        val distinctCurrencies = initialReferences.getDistinctCurrenciesList()
        val distinctDates = referencesMap.keys.toList()
        val combinations = mutableListOf<CurrencyRate>()

        // Generate combinations in parallel for each date
        distinctDates.map { date ->
            async {
                for (base in distinctCurrencies) {
                    for (quote in distinctCurrencies) {
                        if (base != quote) {
                            val rate = if (base == ECB_BASE_UNIT) {
                                referencesMap[date]?.get(base to quote) ?: BigDecimal.ONE
                            } else {
                                val baseRate = referencesMap[date]?.get(ECB_BASE_UNIT to base) ?: BigDecimal.ONE
                                val quoteRate = referencesMap[date]?.get(ECB_BASE_UNIT to quote) ?: BigDecimal.ONE
                                quoteRate.divideByRate(baseRate)
                            }

                            combinations.add(
                                CurrencyRate(
                                    baseCurrencyUnit = Currency.getInstance(base).toModel(),
                                    quoteCurrencyUnit = Currency.getInstance(quote).toModel(),
                                    rate = rate,
                                    date = date
                                )
                            )
                        }
                    }
                }
            }
        }.awaitAll()

        return@coroutineScope combinations
    }

    private fun List<EcbRateReferenceDto>.getDistinctCurrenciesList() =
        flatMap { listOf(it.baseCurrency, it.quoteCurrency) }.distinct()

    private fun List<EcbRateReferenceDto>.getDateToRatesMap() =
        groupBy { it.date }
            .mapValues { (_, references) ->
                references
                    .associateBy { it.baseCurrency to it.quoteCurrency }
                    .mapValues { (_, reference) -> reference.value }
            }

    private fun BigDecimal.divideByRate(rate: BigDecimal): BigDecimal {
        return divide(rate, CURRENCY_RATE_SCALE, HALF_UP)
    }
}