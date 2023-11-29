package org.expenny.core.domain.repository

import kotlinx.coroutines.flow.Flow
import org.expenny.core.model.currency.Currency
import org.expenny.core.model.currency.CurrencyCreate
import org.expenny.core.model.currency.CurrencyUpdate

interface CurrencyRepository {

    fun getCurrencies(): Flow<List<Currency>>

    fun getCurrency(id: Long): Flow<Currency?>

    fun getMainCurrency(): Flow<Currency?>

    suspend fun createCurrency(currency: CurrencyCreate): Long

    suspend fun updateCurrency(currency: CurrencyUpdate)

    suspend fun updateCurrencies(currencies: List<CurrencyUpdate>)

    suspend fun deleteCurrency(id: Long)
}