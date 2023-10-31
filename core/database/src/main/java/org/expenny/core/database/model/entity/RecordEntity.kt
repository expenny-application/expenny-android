package org.expenny.core.database.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.expenny.core.common.types.RecordType
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(tableName = "record")
data class RecordEntity(
    @PrimaryKey(autoGenerate = true)
    val recordId: Long = 0,
    val profileId: Long,
    val extId: String? = null,
    val categoryId: Long? = null,
    val transferAccountId: Long? = null,
    val accountId: Long,
    val type: RecordType,
    val transferAmount: BigDecimal? = null,
    val amount: BigDecimal,
    val date: LocalDateTime,
    val description: String,
    val subject: String,
    val transferFee: BigDecimal? = null
) {

    class Update(
        val recordId: Long,
        val categoryId: Long? = null,
        val transferAccountId: Long? = null,
        val accountId: Long,
        val type: RecordType,
        val transferAmount: BigDecimal? = null,
        val amount: BigDecimal,
        val date: LocalDateTime,
        val description: String,
        val subject: String,
        val transferFee: BigDecimal? = null
    )
}