package org.expenny.core.domain.repository

import androidx.annotation.IntRange
import kotlinx.coroutines.flow.Flow
import org.expenny.core.common.types.RecordType
import org.expenny.core.model.record.Record
import org.expenny.core.model.record.RecordCreate
import org.expenny.core.model.record.RecordUpdate
import org.threeten.extra.LocalDateRange

interface RecordRepository {

    fun getRecordsDesc(): Flow<List<Record>>

    fun getRecordsDesc(
        labelIds: List<Long>,
        accountIds: List<Long>,
        categoryIds: List<Long>,
        types: List<RecordType>,
        dateRange: LocalDateRange?,
        withoutCategory: Boolean,
    ): Flow<List<Record>>

    fun getRecord(id: Long): Flow<Record?>

    suspend fun createRecord(data: RecordCreate)

    suspend fun updateRecord(data: RecordUpdate)

    suspend fun deleteRecords(vararg ids: Long)
}