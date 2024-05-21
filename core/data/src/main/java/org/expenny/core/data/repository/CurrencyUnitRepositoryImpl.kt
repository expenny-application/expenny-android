package org.expenny.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.expenny.core.data.mapper.DataMapper.toModel
import org.expenny.core.common.utils.remoteResultMediator
import org.expenny.core.domain.repository.CurrencyUnitRepository
import org.expenny.core.model.currency.CurrencyUnit
import org.expenny.core.common.utils.RemoteResult
import org.expenny.core.network.EcbService
import org.expenny.core.network.EcbService.Companion.ECB_BASE_UNIT
import java.util.Currency
import javax.inject.Inject

class CurrencyUnitRepositoryImpl @Inject constructor(
    private val ecbService: EcbService,
) : CurrencyUnitRepository {

    private val currencyUnits
        get() = listOf<Currency>(
            Currency.getInstance("ALL"),
            Currency.getInstance("AUD"),
            Currency.getInstance("BAM"),
            Currency.getInstance("BYN"),
            Currency.getInstance("BGN"),
            Currency.getInstance("CHF"),
            Currency.getInstance("CZK"),
            Currency.getInstance("DKK"),
            Currency.getInstance("GEL"),
            Currency.getInstance("HUF"),
            Currency.getInstance("INR"),
            Currency.getInstance("EUR"),
            Currency.getInstance("MKD"),
            Currency.getInstance("MDL"),
            Currency.getInstance("NOK"),
            Currency.getInstance("PLN"),
            Currency.getInstance("RSD"),
            Currency.getInstance("RON"),
            Currency.getInstance("RUB"),
            Currency.getInstance("CNY"),
            Currency.getInstance("TRY"),
            Currency.getInstance("SEK"),
            Currency.getInstance("UAH"),
            Currency.getInstance("USD"),
            Currency.getInstance("GBP"),
        )

    override fun getCurrencyUnits(): Flow<List<CurrencyUnit>> {
        return flowOf(currencyUnits.map { it.toModel() }.sortedBy { it.code })
    }

    override fun getCurrencyUnit(id: Long): CurrencyUnit? {
        return currencyUnits.map { it.toModel() }.firstOrNull { it.id == id }
    }

    override fun getSubscribableCurrencyUnits(): Flow<RemoteResult<List<CurrencyUnit>>> {
        return remoteResultMediator {
            val ecbCodes = ecbService
                .getEurBaseData()
                .distinctBy { it.quoteCurrency }
                .map { it.quoteCurrency }

            currencyUnits
                .filter { ecbCodes.contains(it.currencyCode) || ECB_BASE_UNIT == it.currencyCode }
                .map { it.toModel() }
        }
    }
}