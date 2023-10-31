package org.expenny.core.domain.repository

import kotlinx.coroutines.flow.Flow
import org.expenny.core.model.currency.CurrencyRate
import org.expenny.core.model.resource.ResourceResult
import java.time.LocalDate

interface CurrencyRateRepository {

    fun getLatestRateFlow(base: String, quote: String): Flow<ResourceResult<CurrencyRate>>

    fun getLatestRatesFlow(base: String, quotes: List<String>): Flow<ResourceResult<List<CurrencyRate>>>

    fun getRatesFlow(fromDate: LocalDate, quotes: List<String>): Flow<ResourceResult<List<CurrencyRate>>>
}