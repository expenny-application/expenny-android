package org.expenny.core.database.model.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.threeten.extra.LocalDateRange
import java.math.BigDecimal
import java.time.LocalDate

@Entity(
    tableName = "budget_group",
    indices = [
        Index(value = arrayOf("intervalType", "name", "dateRange", "profileId"), unique = true)
    ]
)
data class BudgetGroupEntity(
    @PrimaryKey(autoGenerate = true)
    val budgetGroupId: Long = 0,
    val profileId: Long,
    val currencyId: Long,
    val name: String?,
    val intervalType: String?,
    val dateRange: ClosedRange<LocalDate>?,
) {

    data class Update(
        val budgetGroupId: Long,
        val name: String,
        val dateRange: ClosedRange<LocalDate>,
    )
}
