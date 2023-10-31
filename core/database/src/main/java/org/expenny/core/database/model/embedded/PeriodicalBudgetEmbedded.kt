package org.expenny.core.database.model.embedded

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.expenny.core.database.model.crossref.PeriodicalBudgetAccountCrossRef
import org.expenny.core.database.model.crossref.PeriodicalBudgetLimitCrossRef
import org.expenny.core.database.model.entity.*

data class PeriodicalBudgetEmbedded(
    @Embedded
    val budget: PeriodicalBudgetEntity,

    @Relation(
        entity = CategoryEntity::class,
        parentColumn = "categoryId",
        entityColumn = "categoryId",
    )
    val category: CategoryEmbedded,

    @Relation(
        entity = SettlementCurrencyEntity::class,
        parentColumn = "currencyId",
        entityColumn = "currencyId",
    )
    val currency: SettlementCurrencyEmbedded,

    @Relation(
        entity = AccountEntity::class,
        parentColumn = "periodicalBudgetId",
        entityColumn = "accountId",
        associateBy = Junction(PeriodicalBudgetAccountCrossRef::class)
    )
    val accounts: List<AccountEmbedded>,

    @Relation(
        parentColumn = "budgetLimitId",
        entityColumn = "budgetLimitId",
    )
    val defaultLimit: BudgetLimitEntity,

    @Relation(
        parentColumn = "periodicalBudgetId",
        entityColumn = "budgetLimitId",
        associateBy = Junction(PeriodicalBudgetLimitCrossRef::class)
    )
    val otherLimits: List<BudgetLimitEntity>
)