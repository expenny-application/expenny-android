package org.expenny.core.database.model.embedded

import androidx.room.Embedded
import androidx.room.Relation
import org.expenny.core.database.model.entity.BudgetEntity
import org.expenny.core.database.model.entity.CategoryEntity
import org.expenny.core.database.model.entity.ProfileEntity

data class BudgetEmbedded(
    @Embedded
    val budget: BudgetEntity,

    @Relation(
        parentColumn = "profileId",
        entityColumn = "profileId",
    )
    val profile: ProfileEntity,

    @Relation(
        entity = CategoryEntity::class,
        parentColumn = "categoryId",
        entityColumn = "categoryId",
    )
    val category: CategoryEmbedded,
)