package org.expenny.core.database.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate

@Entity(tableName = "budget_limit")
data class BudgetLimitEntity(
    @PrimaryKey(autoGenerate = true)
    val budgetLimitId: Long = 0,
    val profileId: Long,
    val limit: BigDecimal,
    val fromDate: LocalDate,
    val toDate: LocalDate,
)
