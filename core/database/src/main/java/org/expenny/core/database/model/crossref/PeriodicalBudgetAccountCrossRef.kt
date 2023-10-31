package org.expenny.core.database.model.crossref

import androidx.room.Entity

@Entity(
    tableName = "periodical_budget_account_ref",
    primaryKeys = ["periodicalBudgetId", "accountId"]
)
data class PeriodicalBudgetAccountCrossRef(
    val periodicalBudgetId: Long,
    val accountId: Long
)
