package org.expenny.core.database.model.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.OffsetDateTime

@Entity(
    tableName = "settlement_currency",
    indices = [
        Index(value = arrayOf("code", "profileId"), unique = true)
    ]
)
data class SettlementCurrencyEntity(
    @PrimaryKey(autoGenerate = true)
    val currencyId: Long = 0,
    val profileId: Long,
    val code: String,
    val baseToQuoteRate: BigDecimal,
    val isSubscribedToRateUpdates: Boolean = false,
    val updatedAt: OffsetDateTime,
) {

    data class Update(
        val currencyId: Long,
        val baseToQuoteRate: BigDecimal,
        val isSubscribedToRateUpdates: Boolean,
        val updatedAt: OffsetDateTime = OffsetDateTime.now(),
    )
}