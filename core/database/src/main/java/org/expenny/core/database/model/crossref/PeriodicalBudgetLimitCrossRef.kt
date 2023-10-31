package org.expenny.core.database.model.crossref

import androidx.room.Entity

@Entity(
    tableName = "periodical_budget_limit_ref",
    primaryKeys = ["periodicalBudgetId", "budgetLimitId"]
)
data class PeriodicalBudgetLimitCrossRef(
    val periodicalBudgetId: Long,
    val budgetLimitId: Long
)
