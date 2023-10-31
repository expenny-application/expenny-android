package org.expenny.core.data.repository

import androidx.room.withTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import org.expenny.core.common.extensions.mapFlatten
import org.expenny.core.data.mapper.DataMapper.toEntity
import org.expenny.core.data.mapper.DataMapper.toModel
import org.expenny.core.database.ExpennyDatabase
import org.expenny.core.model.account.Account
import org.expenny.core.domain.repository.AccountRepository
import org.expenny.core.domain.repository.LocalRepository
import org.expenny.core.model.account.AccountCreate
import org.expenny.core.model.account.AccountUpdate
import java.math.BigDecimal
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val database: ExpennyDatabase,
    private val localRepository: LocalRepository
): AccountRepository {
    private val accountDao = database.accountDao()
    private val recordDao = database.recordDao()

    override fun getAccountsFlow(): Flow<List<Account>> {
        return combine(
            localRepository.getCurrentProfileId().filterNotNull(),
            accountDao.selectAll()
        ) { profileId, accounts ->
            accounts.filter { it.account.profileId == profileId }
        }.mapFlatten { toModel() }
    }

    override suspend fun getAccount(id: Long): Account? {
        return accountDao.selectById(id)?.toModel()
    }

    override suspend fun createAccount(account: AccountCreate): Long {
        return withContext(Dispatchers.IO) {
            accountDao.insert(account.toEntity())
        }
    }

    override suspend fun updateAccount(account: AccountUpdate) {
        withContext(Dispatchers.IO) {
            accountDao.update(account.toEntity())
        }
    }

    override suspend fun updateAccountBalance(id: Long, amendment: BigDecimal) {
        val currentBalance = accountDao.selectById(id)!!.account.totalBalance
        val newBalance = currentBalance + amendment

        accountDao.updateTotalBalance(id, newBalance)
    }

    override suspend fun deleteAccount(id: Long) {
        database.withTransaction {
            // todo add deletion of all other associated data
            accountDao.delete(id)
            recordDao.deleteByAccountId(id)
        }
    }
}