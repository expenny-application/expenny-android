package org.expenny.core.model.record

import android.net.Uri
import org.expenny.core.common.types.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime

sealed interface RecordUpdate {
    val id: Long
    val accountId: Long
    val attachments: List<Uri>
    val description: String
    val labels: List<String>
    val amount: BigDecimal
    val date: LocalDateTime

    data class Transaction(
        override val id: Long,
        override val accountId: Long,
        override val attachments: List<Uri>,
        override val description: String,
        override val labels: List<String>,
        override val amount: BigDecimal,
        override val date: LocalDateTime,
        val categoryId: Long?,
        val type: TransactionType,
    ) : RecordUpdate

    data class Transfer(
        override val id: Long,
        override val accountId: Long,
        override val attachments: List<Uri>,
        override val description: String,
        override val labels: List<String>,
        override val amount: BigDecimal,
        override val date: LocalDateTime,
        val transferAmount: BigDecimal,
        val transferAccountId: Long,
    ) : RecordUpdate
}
