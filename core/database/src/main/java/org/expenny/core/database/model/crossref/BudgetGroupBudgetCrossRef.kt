package org.expenny.core.database.model.crossref

import androidx.room.Entity

@Entity(
    tableName = "budget_group_budget_ref",
    primaryKeys = ["budgetGroupId", "budgetId"],
)
data class BudgetGroupBudgetCrossRef(
    val budgetGroupId: Long,
    val budgetId: Long
)
