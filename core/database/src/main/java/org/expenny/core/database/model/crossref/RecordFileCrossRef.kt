package org.expenny.core.database.model.crossref

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "record_file_ref",
    primaryKeys = ["recordId", "fileId"],
    indices = [Index(value = arrayOf("fileId"))]
)
data class RecordFileCrossRef(
    val recordId: Long,
    val fileId: Long
)
