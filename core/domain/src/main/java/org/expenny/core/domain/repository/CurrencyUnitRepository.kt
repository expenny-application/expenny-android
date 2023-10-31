package org.expenny.core.domain.repository

import kotlinx.coroutines.flow.Flow
import org.expenny.core.model.currency.CurrencyUnit

interface CurrencyUnitRepository {

    fun getCurrencyUnits(): Flow<List<CurrencyUnit>>

    fun getCurrencyUnit(id: Long): CurrencyUnit?
}