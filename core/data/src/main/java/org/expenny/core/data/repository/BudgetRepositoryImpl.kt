package org.expenny.core.data.repository


import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.expenny.core.common.extensions.mapFlatten
import org.expenny.core.data.mapper.DataMapper.toEntity
import org.expenny.core.data.mapper.DataMapper.toModel
import org.expenny.core.database.ExpennyDatabase
import org.expenny.core.database.model.crossref.BudgetGroupBudgetCrossRef
import org.expenny.core.datastore.ExpennyDataStore
import org.expenny.core.domain.repository.BudgetRepository
import org.expenny.core.model.budget.Budget
import org.expenny.core.model.budget.BudgetCreate
import org.expenny.core.model.budget.BudgetUpdate
import javax.inject.Inject

class BudgetRepositoryImpl@Inject constructor(
    private val database: ExpennyDatabase,
    private val preferences: ExpennyDataStore,
): BudgetRepository {
    private val budgetDao = database.budgetDao()
    private val budgetGroupBudgetDao = database.budgetGroupBudgetDao()

    override fun getBudgets(): Flow<List<Budget>> {
        return combine(
            preferences.getCurrentProfileId().filterNotNull(),
            budgetDao.selectAll()
        ) { profileId, budgets ->
            budgets.filter { it.budget.profileId == profileId }
        }.mapFlatten { toModel() }
    }

    override fun getBudgets(budgetGroupId: Long): Flow<List<Budget>> {
        return budgetGroupBudgetDao.selectAllByBudgetGroupId(budgetGroupId)
            .map { budgetGroupToBudgetList ->
                budgetGroupToBudgetList.mapNotNull { budgetDao.selectById(it.budgetId).first()?.toModel() }
            }
    }

    override fun getBudget(id: Long): Flow<Budget?> {
        return budgetDao.selectById(id).map { it?.toModel() }
    }

    override suspend fun createBudget(budget: BudgetCreate): Long {
        return database.withTransaction {
            val budgetLimitId = budgetDao.insert(budget.toEntity())
            budgetGroupBudgetDao.insert(
                BudgetGroupBudgetCrossRef(
                    budgetGroupId = budget.budgetGroupId,
                    budgetId = budgetLimitId
                )
            )
            return@withTransaction budgetLimitId
        }
    }

    override suspend fun updateBudget(budget: BudgetUpdate) {
        budgetDao.update(budget.toEntity())
    }

    override suspend fun deleteBudget(id: Long) {
        database.withTransaction {
            budgetGroupBudgetDao.deleteByBudgetId(id)
            budgetDao.deleteById(id)
        }
    }
}