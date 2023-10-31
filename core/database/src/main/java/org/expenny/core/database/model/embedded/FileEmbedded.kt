package org.expenny.core.database.model.embedded

import androidx.room.Embedded
import androidx.room.Relation
import org.expenny.core.database.model.entity.FileEntity
import org.expenny.core.database.model.entity.ProfileEntity

data class FileEmbedded(
    @Embedded
    val file: FileEntity,

    @Relation(
        parentColumn = "profileId",
        entityColumn = "profileId",
    )
    val fileProfile: ProfileEntity,
)
