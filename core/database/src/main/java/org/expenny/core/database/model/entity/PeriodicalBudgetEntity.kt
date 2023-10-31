package org.expenny.core.database.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "periodical_budget")
data class PeriodicalBudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val periodicalBudgetId: Long = 0,
    val profileId: Long,
    val currencyId: Long,
    val categoryId: Long,
    val budgetLimitId: Long,
    val type: String,
    val name: String,
    val startDate: LocalDate,
    val closeDate: LocalDate?,
    val description: String
)
