package org.expenny.core.database.model.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "file")
data class FileEntity(
    @PrimaryKey(autoGenerate = true)
    val fileId: Long = 0,
    val profileId: Long,
    val uri: Uri,
)
