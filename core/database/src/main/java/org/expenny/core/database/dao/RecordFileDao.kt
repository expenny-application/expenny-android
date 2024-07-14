package org.expenny.core.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.expenny.core.database.model.crossref.RecordFileCrossRef

@Dao
interface RecordFileDao {

    @Transaction
    @Query("SELECT * FROM record_file_ref WHERE record_file_ref.recordId == :recordId")
    fun selectAllByRecordId(recordId: Long): Flow<List<RecordFileCrossRef>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recordFile: RecordFileCrossRef): Long

    @Query("DELETE FROM record_file_ref WHERE record_file_ref.recordId == :recordId")
    suspend fun deleteByRecordId(recordId: Long)

    @Query("DELETE FROM record_file_ref WHERE record_file_ref.fileId == :fileId")
    suspend fun deleteByFileId(fileId: Long)

    @Query("DELETE FROM record_file_ref WHERE EXISTS(SELECT * FROM record WHERE record.profileId == :profileId AND record.recordId == record_file_ref.recordId)")
    suspend fun deleteByProfileId(profileId: Long)

    @Query("DELETE FROM record_file_ref")
    suspend fun deleteAll()
}