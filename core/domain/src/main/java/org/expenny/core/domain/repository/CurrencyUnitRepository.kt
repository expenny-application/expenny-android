package org.expenny.core.domain.repository

import kotlinx.coroutines.flow.Flow
import org.expenny.core.model.currency.CurrencyUnit
import org.expenny.core.model.resource.RemoteResult

interface CurrencyUnitRepository {

    fun getCurrencyUnits(): Flow<List<CurrencyUnit>>

    fun getCurrencyUnit(id: Long): CurrencyUnit?
    
    fun getSubscribableCurrencyUnits(): Flow<RemoteResult<List<CurrencyUnit>>>
}