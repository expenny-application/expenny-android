package org.expenny.core.domain.repository

import kotlinx.coroutines.flow.Flow
import org.expenny.core.model.account.Account
import org.expenny.core.model.account.AccountCreate
import org.expenny.core.model.account.AccountUpdate
import java.math.BigDecimal

interface AccountRepository {

    fun getAccounts(): Flow<List<Account>>

    fun getAccount(id: Long): Flow<Account?>

    suspend fun createAccount(account: AccountCreate): Long

    suspend fun updateAccount(account: AccountUpdate)

    suspend fun updateAccountBalance(id: Long, amendment: BigDecimal)

    suspend fun deleteAccount(id: Long)
}