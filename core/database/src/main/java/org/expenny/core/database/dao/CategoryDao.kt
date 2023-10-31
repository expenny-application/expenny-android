package org.expenny.core.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.expenny.core.database.model.embedded.AccountEmbedded
import org.expenny.core.database.model.embedded.CategoryEmbedded
import org.expenny.core.database.model.entity.CategoryEntity

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(categoryEntity: CategoryEntity): Long

    @Transaction
    @Query("SELECT * FROM category WHERE category.categoryId = :id")
    suspend fun selectById(id: Long): CategoryEmbedded?

    @Transaction
    @Query("SELECT * FROM category")
    fun selectAll(): Flow<List<CategoryEmbedded>>
}