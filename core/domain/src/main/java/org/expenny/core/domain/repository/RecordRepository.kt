package org.expenny.core.domain.repository

import kotlinx.coroutines.flow.Flow
import org.expenny.core.model.record.Record
import org.expenny.core.model.record.RecordCreate
import org.expenny.core.model.record.RecordUpdate

interface RecordRepository {

    fun getRecordsDesc(): Flow<List<Record>>

    fun getRecord(id: Long): Flow<Record?>

    suspend fun createRecord(data: RecordCreate)

    suspend fun updateRecord(data: RecordUpdate)

    suspend fun deleteRecords(vararg ids: Long)
}