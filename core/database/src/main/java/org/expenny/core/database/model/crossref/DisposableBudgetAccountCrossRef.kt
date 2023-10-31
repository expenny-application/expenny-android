package org.expenny.core.database.model.crossref

import androidx.room.Entity

@Entity(
    tableName = "budget_account_ref",
    primaryKeys = ["disposableBudgetId", "accountId"]
)
data class DisposableBudgetAccountCrossRef(
    val disposableBudgetId: Long,
    val accountId: Long
)