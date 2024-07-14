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
import org.expenny.core.domain.repository.CategoryRepository
import org.expenny.core.model.category.Category
import org.expenny.core.model.category.CategoryCreate
import org.expenny.core.model.category.CategoryUpdate
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val database: ExpennyDatabase,
    private val preferences: ExpennyDataStore
) : CategoryRepository {
    private val categoryDao = database.categoryDao()
    private val recordDao = database.recordDao()

    override fun getCategories(): Flow<List<Category>> {
        return combine(
            preferences.getCurrentProfileId().filterNotNull(),
            categoryDao.selectAll()
        ) { profileId, accounts ->
            accounts.filter { it.category.profileId == profileId }
        }.mapFlatten { toModel() }
    }

    override fun getCategory(id: Long): Flow<Category?> {
        return categoryDao.selectById(id).map { it?.toModel() }
    }

    override suspend fun deleteCategory(id: Long) {
        database.withTransaction {
            recordDao.updateCategoryId(oldCategoryId = id, newCategoryId = null)
            categoryDao.delete(id)
        }
    }

    override suspend fun createCategories(categories: List<CategoryCreate>) {
        database.withTransaction {
            categories.forEach {
                categoryDao.insert(it.toEntity())
            }
        }
    }

    override suspend fun createCategory(category: CategoryCreate): Long {
        return categoryDao.insert(category.toEntity())
    }

    override suspend fun updateCategory(category: CategoryUpdate) {
        categoryDao.update(category.toEntity())
    }
}