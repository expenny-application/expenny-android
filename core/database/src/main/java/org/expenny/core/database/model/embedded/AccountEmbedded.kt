package org.expenny.core.database.model.embedded

import androidx.room.Embedded
import androidx.room.Relation
import org.expenny.core.database.model.entity.AccountEntity
import org.expenny.core.database.model.entity.ProfileEntity
import org.expenny.core.database.model.entity.SettlementCurrencyEntity

data class AccountEmbedded(
    @Embedded
    val account: AccountEntity,

    @Relation(
        entity = SettlementCurrencyEntity::class,
        parentColumn = "currencyId",
        entityColumn = "currencyId",
    )
    val accountCurrency: SettlementCurrencyEmbedded,

    @Relation(
        parentColumn = "profileId",
        entityColumn = "profileId",
    )
    val accountProfile: ProfileEntity,
)
