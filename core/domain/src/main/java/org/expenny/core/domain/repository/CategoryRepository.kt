package org.expenny.core.domain.repository

import kotlinx.coroutines.flow.Flow
import org.expenny.core.model.category.Category
import org.expenny.core.model.category.CategoryCreate
import org.expenny.core.model.category.CategoryUpdate

interface CategoryRepository {

    fun getCategories(): Flow<List<Category>>

    fun getCategory(id: Long): Flow<Category?>

    suspend fun deleteCategory(id: Long)

    suspend fun createCategories(categories: List<CategoryCreate>)

    suspend fun createCategory(category: CategoryCreate): Long

    suspend fun updateCategory(category: CategoryUpdate)
}