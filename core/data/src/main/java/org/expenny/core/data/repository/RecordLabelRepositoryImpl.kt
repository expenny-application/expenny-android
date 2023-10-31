package org.expenny.core.data.repository

import org.expenny.core.database.ExpennyDatabase
import org.expenny.core.database.model.crossref.RecordLabelCrossRef
import org.expenny.core.domain.repository.RecordLabelRepository
import javax.inject.Inject

class RecordLabelRepositoryImpl @Inject constructor(
    private val database: ExpennyDatabase,
) : RecordLabelRepository {
    private val recordLabelDao = database.recordLabelDao()

    override suspend fun createRecordLabels(recordId: Long, ids: List<Long>) {
        ids.forEach { labelId ->
            recordLabelDao.insert(
                RecordLabelCrossRef(
                    recordId = recordId,
                    labelId = labelId
                )
            )
        }
    }

    override suspend fun deleteRecordLabels(recordId: Long) {
        recordLabelDao.deleteAllByRecordId(recordId)
    }
}