package org.expenny.core.domain.repository

interface RecordFileRepository {

    suspend fun createRecordFiles(recordId: Long, ids: List<Long>)

    suspend fun deleteRecordFiles(recordId: Long)
}