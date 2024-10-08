package org.expenny.core.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.expenny.core.database.model.embedded.CategoryEmbedded
import org.expenny.core.database.model.entity.CategoryEntity

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: CategoryEntity): Long

    @Update(entity = CategoryEntity::class)
    suspend fun update(category: CategoryEntity.Update)

    @Transaction
    @Query("SELECT * FROM category WHERE category.categoryId = :id")
    fun selectById(id: Long): Flow<CategoryEmbedded?>

    @Transaction
    @Query("SELECT * FROM category")
    fun selectAll(): Flow<List<CategoryEmbedded>>

    @Query("DELETE FROM category WHERE category.profileId == :id")
    suspend fun deleteByProfileId(id: Long)

    @Query("DELETE FROM category WHERE category.categoryId == :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM category")
    suspend fun deleteAll()
}