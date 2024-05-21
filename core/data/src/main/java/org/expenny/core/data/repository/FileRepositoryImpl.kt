package org.expenny.core.data.repository

import org.expenny.core.data.mapper.DataMapper.toEntity
import org.expenny.core.database.ExpennyDatabase
import org.expenny.core.domain.repository.FileRepository
import org.expenny.core.model.file.FileCreate
import javax.inject.Inject

class FileRepositoryImpl @Inject constructor(
    private val database: ExpennyDatabase,
) : FileRepository {
    val fileDao = database.fileDao()

    override suspend fun createFile(data: FileCreate): Long {
        return fileDao.insert(data.toEntity())
    }

    override suspend fun deleteFile(id: Long) {
        fileDao.delete(id)
    }
}