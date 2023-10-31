package org.expenny.core.database.model.embedded

import androidx.room.Embedded
import androidx.room.Relation
import org.expenny.core.database.model.entity.CategoryEntity
import org.expenny.core.database.model.entity.ProfileEntity

data class CategoryEmbedded(
    @Embedded
    val category: CategoryEntity,

    @Relation(
        parentColumn = "profileId",
        entityColumn = "profileId",
    )
    val categoryProfile: ProfileEntity,
)