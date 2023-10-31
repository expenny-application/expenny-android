package org.expenny.core.data.repository

import android.icu.util.Currency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.expenny.core.data.mapper.DataMapper.toModel
import org.expenny.core.domain.repository.CurrencyUnitRepository
import org.expenny.core.model.currency.CurrencyUnit
import javax.inject.Inject

class CurrencyUnitRepositoryImpl @Inject constructor() : CurrencyUnitRepository {

    private val currencyUnits
        get() = listOf<Currency>(
            Currency.getInstance("ALL"),
            Currency.getInstance("AMD"),
            Currency.getInstance("AUD"),
            Currency.getInstance("AZN"),
            Currency.getInstance("BYN"),
            Currency.getInstance("BYR"),
            Currency.getInstance("BAM"),
            Currency.getInstance("BGN"),
            Currency.getInstance("CZK"),
            Currency.getInstance("DKK"),
            Currency.getInstance("HRK"),
            Currency.getInstance("GEL"),
            Currency.getInstance("HUF"),
            Currency.getInstance("ISK"),
            Currency.getInstance("EUR"),
            Currency.getInstance("MDL"),
            Currency.getInstance("MKD"),
            Currency.getInstance("NOK"),
            Currency.getInstance("PLN"),
            Currency.getInstance("RON"),
            Currency.getInstance("RUB"),
            Currency.getInstance("RSD"),
            Currency.getInstance("SEK"),
            Currency.getInstance("CHF"),
            Currency.getInstance("TRY"),
            Currency.getInstance("UAH"),
            Currency.getInstance("USD"),
            Currency.getInstance("GBP"),
        )

    override fun getCurrencyUnits(): Flow<List<CurrencyUnit>> {
        return flowOf(currencyUnits.map { it.toModel() })
    }

    override fun getCurrencyUnit(id: Long): CurrencyUnit? {
        return currencyUnits.map { it.toModel() }.firstOrNull { it.id == id }
    }
}