package org.expenny.core.data.repository

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import org.expenny.core.common.extensions.mapFlatten
import org.expenny.core.data.mapper.DataMapper.toEntity
import org.expenny.core.data.mapper.DataMapper.toModel
import org.expenny.core.database.ExpennyDatabase
import org.expenny.core.domain.repository.CategoryRepository
import org.expenny.core.domain.repository.LocalRepository
import org.expenny.core.model.category.Category
import org.expenny.core.model.category.CategoryCreate
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val database: ExpennyDatabase,
    private val localRepository: LocalRepository
) : CategoryRepository {
    private val categoryDao = database.categoryDao()

    override fun getCategoriesFlow(): Flow<List<Category>> {
        return combine(
            localRepository.getCurrentProfileId().filterNotNull(),
            categoryDao.selectAll()
        ) { profileId, accounts ->
            accounts.filter { it.category.profileId == profileId }
        }.mapFlatten { toModel() }
    }

    override suspend fun getCategory(id: Long): Category? {
        return categoryDao.selectById(id)?.toModel()
    }

    override suspend fun createCategories(categories: List<CategoryCreate>) {
        database.withTransaction {
            categories.forEach {
                categoryDao.insert(it.toEntity())
            }
        }
    }
}