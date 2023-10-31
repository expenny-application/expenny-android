package org.expenny.core.database.model.crossref

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "record_label_ref",
    primaryKeys = ["recordId", "labelId"],
    indices = [Index(value = arrayOf("labelId"))]
)
data class RecordLabelCrossRef(
    val recordId: Long,
    val labelId: Long
)