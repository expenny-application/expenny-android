package org.expenny.core.domain.repository

import kotlinx.coroutines.flow.Flow
import org.expenny.core.model.budget.Budget
import org.expenny.core.model.budget.BudgetCreate
import org.expenny.core.model.budget.BudgetUpdate

interface BudgetRepository {

    fun getBudgets(): Flow<List<Budget>>

    fun getBudgets(budgetGroupId: Long): Flow<List<Budget>>

    fun getBudget(id: Long): Flow<Budget?>

    suspend fun createBudget(budget: BudgetCreate): Long

    suspend fun updateBudget(budget: BudgetUpdate)

    suspend fun deleteBudget(id: Long)
}