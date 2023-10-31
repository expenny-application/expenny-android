package org.expenny.core.domain.repository

import kotlinx.coroutines.flow.Flow
import org.expenny.core.model.category.Category
import org.expenny.core.model.category.CategoryCreate

interface CategoryRepository {

    fun getCategoriesFlow(): Flow<List<Category>>

    suspend fun getCategory(id: Long): Category?

    suspend fun createCategories(categories: List<CategoryCreate>)
}