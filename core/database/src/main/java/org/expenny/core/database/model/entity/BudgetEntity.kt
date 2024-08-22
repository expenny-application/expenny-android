package org.expenny.core.database.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDate

@Entity(tableName = "budget")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val budgetId: Long = 0,
    val profileId: Long,
    val categoryId: Long,
    val limitValue: BigDecimal,
    val startDate: LocalDate,
    val endDate: LocalDate?
) {

    data class Update(
        val budgetId: Long,
        val limitValue: BigDecimal,
        val endDate: LocalDate?
    )
}
