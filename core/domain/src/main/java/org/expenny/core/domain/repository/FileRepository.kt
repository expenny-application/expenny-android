package org.expenny.core.domain.repository

import org.expenny.core.model.file.FileCreate

interface FileRepository {

    suspend fun createFile(data: FileCreate): Long

    suspend fun deleteFile(id: Long)
}