package org.expenny.core.database.dao

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow
import org.expenny.core.database.model.embedded.AccountEmbedded
import org.expenny.core.database.model.entity.AccountEntity
import java.math.BigDecimal

@Dao
interface AccountDao {

    @RawQuery
    fun select(query: SupportSQLiteQuery): Flow<List<AccountEmbedded>>

    @Transaction
    @Query("SELECT * FROM account")
    fun selectAll(): Flow<List<AccountEmbedded>>

    @Query("SELECT * FROM account WHERE account.accountId = :id")
    fun selectById(id: Long): Flow<AccountEmbedded?>

    @Transaction
    @Query("SELECT * FROM account WHERE account.currencyId = :id")
    fun selectByCurrencyId(id: Long): Flow<List<AccountEmbedded>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(account: AccountEntity): Long

    @Update(entity = AccountEntity::class)
    suspend fun update(account: AccountEntity.Update)

    @Query("UPDATE account SET totalBalance = :totalBalance WHERE accountId = :id")
    suspend fun updateTotalBalance(id: Long, totalBalance: BigDecimal)

    @Query("DELETE FROM account WHERE account.profileId == :id")
    suspend fun deleteByProfileId(id: Long)

    @Query("DELETE FROM account WHERE account.accountId == :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM account")
    suspend fun deleteAll()

    fun selectById(vararg accountIds: Long): Flow<List<AccountEmbedded>> {
        val queryString = buildString {
            append("SELECT * FROM account")
            if (accountIds.isNotEmpty()) {
                append(" WHERE account.accountId IN (${Array(accountIds.size){'?'}.joinToString()})")
            }
        }

        return select(SimpleSQLiteQuery(queryString, accountIds.toTypedArray()))
    }
}