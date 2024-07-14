package org.expenny.core.data.repository

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import org.expenny.core.common.extensions.mapFlatten
import org.expenny.core.data.mapper.DataMapper.toEntity
import org.expenny.core.data.mapper.DataMapper.toModel
import org.expenny.core.database.ExpennyDatabase
import org.expenny.core.datastore.ExpennyDataStore
import org.expenny.core.domain.repository.BudgetGroupRepository
import org.expenny.core.model.budgetgroup.BudgetGroup
import org.expenny.core.model.budgetgroup.BudgetGroupCreate
import org.expenny.core.model.budgetgroup.BudgetGroupUpdate
import javax.inject.Inject

class BudgetGroupRepositoryImpl @Inject constructor(
    private val database: ExpennyDatabase,
    private val preferences: ExpennyDataStore
): BudgetGroupRepository {
    private val budgetGroupDao = database.budgetGroupDao()
    private val budgetGroupLimitDao = database.budgetGroupBudgetDao()

    override fun getBudgetGroups(): Flow<List<BudgetGroup>> {
        return combine(
            preferences.getCurrentProfileId().filterNotNull(),
            budgetGroupDao.selectAll()
        ) { profileId, budgetGroups ->
            budgetGroups.filter { it.group.profileId == profileId }
        }.mapFlatten { toModel() }
    }

    override fun getBudgetGroup(id: Long): Flow<BudgetGroup?> {
        return budgetGroupDao.selectById(id).map { it?.toModel() }
    }

    override suspend fun createBudgetGroup(budgetGroup: BudgetGroupCreate): Long {
        return budgetGroupDao.insert(budgetGroup.toEntity())
    }

    override suspend fun updateBudgetGroup(budgetGroup: BudgetGroupUpdate) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteBudgetGroup(id: Long) {
        database.withTransaction {
            budgetGroupLimitDao.deleteByBudgetGroupId(id)
            budgetGroupDao.deleteById(id)
        }
    }
}