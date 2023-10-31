package org.expenny.core.data.repository

import android.content.Context
import androidx.room.withTransaction
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import org.expenny.core.common.extensions.mapFlatten
import org.expenny.core.data.mapper.DataMapper.toModel
import org.expenny.core.data.mapper.DataMapper.toEntity
import org.expenny.core.data.worker.CurrencyRateUpdateWorker
import org.expenny.core.database.ExpennyDatabase
import org.expenny.core.domain.repository.CurrencyRepository
import org.expenny.core.domain.repository.LocalRepository
import org.expenny.core.model.currency.Currency
import org.expenny.core.model.currency.CurrencyCreate
import org.expenny.core.model.currency.CurrencyUpdate
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val database: ExpennyDatabase,
    private val localRepository: LocalRepository,
) : CurrencyRepository {
    private val settlementCurrencyDao = database.currencyDao()
    private val accountDao = database.accountDao()
    private val recordDao = database.recordDao()

    override fun getCurrencies(): Flow<List<Currency>> {
        return combine(
            localRepository.getCurrentProfileId(),
            settlementCurrencyDao.selectAll()
        ) { profileId, currencies ->
            currencies.filter { it.currency.profileId == profileId }
        }.mapFlatten { toModel() }
    }

    override fun getCurrency(id: Long): Flow<Currency?> {
        return settlementCurrencyDao.selectById(id).map { it?.toModel() }
    }

    override fun getMainCurrency(): Flow<Currency?> {
        return localRepository.getCurrentProfileId()
            .filterNotNull()
            .flatMapLatest { settlementCurrencyDao.selectMain(it) }
            .map { it?.toModel() }
    }

    override suspend fun createCurrency(currency: CurrencyCreate): Long {
        return settlementCurrencyDao.insert(currency.toEntity())
    }

    override suspend fun updateCurrency(currency: CurrencyUpdate) {
        settlementCurrencyDao.update(currency.toEntity())
    }

    override suspend fun deleteCurrency(id: Long) {
        // todo add deletion of all other associated data
        database.withTransaction {
            settlementCurrencyDao.delete(id)

            accountDao.selectByCurrencyId(id).first().map { it.account.accountId }.forEach { accountId ->
                accountDao.delete(accountId)
                recordDao.deleteByAccountId(accountId)
            }
        }
    }
}