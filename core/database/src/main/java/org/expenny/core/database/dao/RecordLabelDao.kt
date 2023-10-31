package org.expenny.core.database.dao

import androidx.room.*
import org.expenny.core.database.model.crossref.RecordLabelCrossRef

@Dao
interface RecordLabelDao {

    @Transaction
    @Query("SELECT * FROM record_label_ref WHERE record_label_ref.recordId == :recordId")
    fun selectAllByRecordId(recordId: Long): List<RecordLabelCrossRef>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recordLabel: RecordLabelCrossRef): Long

    @Query("DELETE FROM record_label_ref WHERE record_label_ref.recordId == :recordId")
    suspend fun deleteAllByRecordId(recordId: Long)

    @Query("DELETE FROM record_label_ref WHERE record_label_ref.labelId == :labelId")
    suspend fun deleteAllByLabelId(labelId: Long)
}