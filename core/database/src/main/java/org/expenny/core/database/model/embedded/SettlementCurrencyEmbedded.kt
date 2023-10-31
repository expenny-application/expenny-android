package org.expenny.core.database.model.embedded

import androidx.room.Embedded
import androidx.room.Relation
import org.expenny.core.database.model.entity.ProfileEntity
import org.expenny.core.database.model.entity.SettlementCurrencyEntity

data class SettlementCurrencyEmbedded(
    @Embedded
    val currency: SettlementCurrencyEntity,

    @Relation(
        parentColumn = "profileId",
        entityColumn = "profileId",
    )
    val currencyProfile: ProfileEntity,
)
