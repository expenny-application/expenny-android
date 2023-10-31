package org.expenny.core.database.model.embedded

import androidx.room.Embedded
import androidx.room.Relation
import org.expenny.core.database.model.entity.LabelEntity
import org.expenny.core.database.model.entity.ProfileEntity

data class LabelEmbedded(
    @Embedded
    val label: LabelEntity,

    @Relation(
        parentColumn = "profileId",
        entityColumn = "profileId",
    )
    val labelProfile: ProfileEntity,
)
