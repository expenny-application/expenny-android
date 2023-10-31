package org.expenny.core.data.repository

import androidx.room.withTransaction
import org.expenny.core.database.ExpennyDatabase
import org.expenny.core.database.model.crossref.RecordFileCrossRef
import org.expenny.core.domain.repository.RecordFileRepository
import javax.inject.Inject

class RecordFileRepositoryImpl @Inject constructor(
    private val database: ExpennyDatabase,
) : RecordFileRepository {
    val recordFileDao = database.recordFileDao()
    val fileDao = database.fileDao()

    override suspend fun createRecordFiles(recordId: Long, ids: List<Long>) {
        ids.forEach { fileId ->
            recordFileDao.insert(
                RecordFileCrossRef(
                    recordId = recordId,
                    fileId = fileId
                )
            )
        }
    }

    override suspend fun deleteRecordFiles(recordId: Long) {
        database.withTransaction {
            val filesIds = recordFileDao.selectAllByRecordId(recordId).map { it.fileId }

            recordFileDao.deleteAllByRecordId(recordId)
            fileDao.deleteAll(filesIds)
        }
    }
}