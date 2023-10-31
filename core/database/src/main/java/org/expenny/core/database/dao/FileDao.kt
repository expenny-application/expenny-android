package org.expenny.core.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.expenny.core.database.model.embedded.FileEmbedded
import org.expenny.core.database.model.entity.FileEntity

@Dao
interface FileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(file: FileEntity): Long

    @Transaction
    @Query("SELECT * FROM file ORDER BY file.fileId DESC")
    fun selectAll(): Flow<List<FileEmbedded>>

    @Query("DELETE FROM file WHERE file.fileId == :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM file WHERE file.fileId in (:ids)")
    suspend fun deleteAll(ids: List<Long>)
}