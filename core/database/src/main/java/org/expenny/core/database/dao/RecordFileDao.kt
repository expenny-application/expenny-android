package org.expenny.core.database.dao

import androidx.room.*
import org.expenny.core.database.model.crossref.RecordFileCrossRef

@Dao
interface RecordFileDao {

    @Transaction
    @Query("SELECT * FROM record_file_ref WHERE record_file_ref.recordId == :recordId")
    fun selectAllByRecordId(recordId: Long): List<RecordFileCrossRef>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recordFile: RecordFileCrossRef): Long

    @Query("DELETE FROM record_file_ref WHERE record_file_ref.recordId == :recordId")
    suspend fun deleteAllByRecordId(recordId: Long)

    @Query("DELETE FROM record_file_ref WHERE record_file_ref.fileId == :fileId")
    suspend fun deleteAllByFileId(fileId: Long)
}