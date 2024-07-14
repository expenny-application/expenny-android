package org.expenny.core.domain.repository

import kotlinx.coroutines.flow.Flow
import org.expenny.core.model.budgetgroup.BudgetGroup
import org.expenny.core.model.budgetgroup.BudgetGroupCreate
import org.expenny.core.model.budgetgroup.BudgetGroupUpdate

interface BudgetGroupRepository {

    fun getBudgetGroups(): Flow<List<BudgetGroup>>

    fun getBudgetGroup(id: Long): Flow<BudgetGroup?>

    suspend fun createBudgetGroup(budgetGroup: BudgetGroupCreate): Long

    suspend fun updateBudgetGroup(budgetGroup: BudgetGroupUpdate)

    suspend fun deleteBudgetGroup(id: Long)
}