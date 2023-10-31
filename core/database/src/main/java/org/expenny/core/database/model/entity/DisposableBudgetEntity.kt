package org.expenny.core.database.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "disposable_budget")
data class DisposableBudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val disposableBudgetId: Long = 0,
    val profileId: Long,
    val currencyId: Long,
    val categoryId: Long,
    val budgetLimitId: Long,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val description: String
)