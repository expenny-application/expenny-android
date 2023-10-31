package org.expenny.core.domain.repository

interface RecordLabelRepository {

    suspend fun createRecordLabels(recordId: Long, ids: List<Long>)

    suspend fun deleteRecordLabels(recordId: Long)
}