package org.expenny.core.database.model.embedded

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.expenny.core.database.model.crossref.DisposableBudgetAccountCrossRef
import org.expenny.core.database.model.entity.*

data class DisposableBudgetEmbedded(
    @Embedded
    val budget: DisposableBudgetEntity,

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
        parentColumn = "disposableBudgetId",
        entityColumn = "accountId",
        associateBy = Junction(DisposableBudgetAccountCrossRef::class)
    )
    val accounts: List<AccountEmbedded>,

    @Relation(
        parentColumn = "budgetLimitId",
        entityColumn = "budgetLimitId",
    )
    val limit: BudgetLimitEntity,
)
