package org.expenny.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.expenny.core.database.model.crossref.BudgetGroupBudgetCrossRef

@Dao
interface BudgetGroupBudgetDao {

    @Transaction
    @Query("SELECT * FROM budget_group_budget_ref WHERE budget_group_budget_ref.budgetGroupId == :budgetGroupId")
    fun selectAllByBudgetGroupId(budgetGroupId: Long): Flow<List<BudgetGroupBudgetCrossRef>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budgetGroupBudget: BudgetGroupBudgetCrossRef): Long

    @Query("DELETE FROM budget_group_budget_ref WHERE budget_group_budget_ref.budgetGroupId == :budgetGroupId")
    suspend fun deleteByBudgetGroupId(budgetGroupId: Long)

    @Query("DELETE FROM budget_group_budget_ref WHERE budget_group_budget_ref.budgetId == :budgetId")
    suspend fun deleteByBudgetId(budgetId: Long)

    @Query("DELETE FROM budget_group_budget_ref WHERE EXISTS(SELECT * FROM budget_group WHERE budget_group.profileId == :profileId AND budget_group.budgetGroupId == budget_group_budget_ref.budgetGroupId)")
    suspend fun deleteByProfileId(profileId: Long)

    @Query("DELETE FROM budget_group_budget_ref")
    suspend fun deleteAll()
}