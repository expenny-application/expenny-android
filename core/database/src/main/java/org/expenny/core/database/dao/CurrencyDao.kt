package org.expenny.core.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.expenny.core.database.model.embedded.SettlementCurrencyEmbedded
import org.expenny.core.database.model.entity.SettlementCurrencyEntity

@Dao
interface CurrencyDao {

    @Transaction
    @Query("SELECT * FROM settlement_currency WHERE settlement_currency.currencyId = :id")
    fun selectById(id: Long): Flow<SettlementCurrencyEmbedded?>

    @Transaction
    @Query("SELECT * FROM settlement_currency WHERE settlement_currency.profileId = :profileId LIMIT 1")
    fun selectMain(profileId: Long): Flow<SettlementCurrencyEmbedded?>

    @Transaction
    @Query("SELECT * FROM settlement_currency")
    fun selectAll(): Flow<List<SettlementCurrencyEmbedded>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(currency: SettlementCurrencyEntity): Long

    @Update(entity = SettlementCurrencyEntity::class)
    suspend fun update(currency: SettlementCurrencyEntity.Update)

    @Query("DELETE FROM settlement_currency WHERE settlement_currency.currencyId == :id")
    suspend fun delete(id: Long)
}