package org.expenny.core.model.record

import android.net.Uri
import org.expenny.core.common.types.RecordType
import org.expenny.core.common.types.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime

sealed interface RecordCreate {
    val profileId: Long
    val accountId: Long
    val labelIds: List<Long>
    val receiptsUris: List<Uri>
    val description: String
    val subject: String
    val amount: BigDecimal
    val date: LocalDateTime

    val recordType: RecordType
        get() = when (this) {
            is Transfer -> RecordType.Transfer
            is Transaction -> {
                when (type) {
                    TransactionType.Outgoing -> RecordType.Expense
                    TransactionType.Incoming -> RecordType.Income
                }
            }
        }

    data class Transaction(
        override val profileId: Long,
        override val accountId: Long,
        override val labelIds: List<Long>,
        override val receiptsUris: List<Uri>,
        override val description: String,
        override val subject: String,
        override val amount: BigDecimal,
        override val date: LocalDateTime,
        val categoryId: Long?,
        val type: TransactionType,
    ) : RecordCreate

    data class Transfer(
        override val profileId: Long,
        override val accountId: Long,
        override val labelIds: List<Long>,
        override val receiptsUris: List<Uri>,
        override val description: String,
        override val subject: String,
        override val amount: BigDecimal,
        override val date: LocalDateTime,
        val transferAmount: BigDecimal,
        val transferAccountId: Long,
        val transferFee: BigDecimal,
    ) : RecordCreate
}
