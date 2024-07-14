package org.expenny.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.expenny.core.database.model.embedded.BudgetGroupEmbedded
import org.expenny.core.database.model.embedded.CategoryEmbedded
import org.expenny.core.database.model.entity.BudgetGroupEntity

@Dao
interface BudgetGroupDao {

    @Transaction
    @Query("SELECT * FROM budget_group")
    fun selectAll(): Flow<List<BudgetGroupEmbedded>>

    @Transaction
    @Query("SELECT * FROM budget_group WHERE budget_group.budgetGroupId = :id")
    fun selectById(id: Long): Flow<BudgetGroupEmbedded?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(budget: BudgetGroupEntity): Long

    @Update(entity = BudgetGroupEntity::class)
    suspend fun update(budget: BudgetGroupEntity.Update)

    @Query("DELETE FROM budget_group WHERE budget_group.budgetGroupId == :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM budget_group")
    suspend fun deleteAll()

    @Query("DELETE FROM budget_group WHERE budget_group.profileId == :id")
    suspend fun deleteByProfileId(id: Long)
}