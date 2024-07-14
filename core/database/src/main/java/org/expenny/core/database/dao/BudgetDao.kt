package org.expenny.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.expenny.core.database.model.embedded.BudgetEmbedded
import org.expenny.core.database.model.entity.BudgetEntity

@Dao
interface BudgetDao {

    @Transaction
    @Query("SELECT * FROM budget")
    fun selectAll(): Flow<List<BudgetEmbedded>>

    @Query("SELECT * FROM budget WHERE budget.budgetId == :id")
    fun selectById(id: Long): Flow<BudgetEmbedded?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(budget: BudgetEntity): Long

    @Update(entity = BudgetEntity::class)
    suspend fun update(budget: BudgetEntity.Update)

    @Query("DELETE FROM budget WHERE budget.budgetId == :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM budget")
    suspend fun deleteAll()

    @Query("DELETE FROM budget WHERE budget.profileId == :id")
    suspend fun deleteByProfileId(id: Long)
}