package org.expenny.core.database.model.embedded

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.expenny.core.database.model.crossref.BudgetGroupBudgetCrossRef
import org.expenny.core.database.model.entity.BudgetGroupEntity
import org.expenny.core.database.model.entity.BudgetEntity
import org.expenny.core.database.model.entity.ProfileEntity
import org.expenny.core.database.model.entity.SettlementCurrencyEntity

data class BudgetGroupEmbedded(
    @Embedded
    val group: BudgetGroupEntity,

    @Relation(
        parentColumn = "profileId",
        entityColumn = "profileId",
    )
    val profile: ProfileEntity,

    @Relation(
        entity = SettlementCurrencyEntity::class,
        parentColumn = "currencyId",
        entityColumn = "currencyId",
    )
    val currency: SettlementCurrencyEmbedded,

    @Relation(
        entity = BudgetEntity::class,
        parentColumn = "budgetGroupId",
        entityColumn = "budgetId",
        associateBy = Junction(BudgetGroupBudgetCrossRef::class)
    )
    val budgets: List<BudgetEmbedded>
)
