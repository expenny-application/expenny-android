package org.expenny.core.domain.repository

import kotlinx.coroutines.flow.Flow
import org.expenny.core.model.currency.CurrencyRate
import org.expenny.core.common.utils.RemoteResult
import java.time.LocalDate

interface CurrencyRateRepository {

    fun getLatestRateFlow(base: String, quote: String): Flow<RemoteResult<CurrencyRate>>

    fun getLatestRatesFlow(base: String, quotes: List<String>): Flow<RemoteResult<List<CurrencyRate>>>

    fun getRatesFlow(fromDate: LocalDate, quotes: List<String>): Flow<RemoteResult<List<CurrencyRate>>>
}