package org.expenny.core.model.record

import android.net.Uri
import org.expenny.core.common.types.RecordType
import org.expenny.core.common.types.TransactionType
import org.expenny.core.model.account.Account
import org.expenny.core.model.category.Category
import org.expenny.core.model.currency.CurrencyAmount
import org.expenny.core.model.profile.Profile
import java.time.LocalDateTime

sealed interface Record {
    val id: Long
    val profile: Profile
    val account: Account
    val labels: List<String>
    val receipts: List<Uri>
    val description: String
    val amount: CurrencyAmount
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

    data class Transfer(
        override val id: Long,
        override val profile: Profile,
        override val account: Account,
        override val labels: List<String>,
        override val receipts: List<Uri>,
        override val description: String,
        override val amount: CurrencyAmount,
        override val date: LocalDateTime,
        val transferAccount: Account,
        val transferAmount: CurrencyAmount,
        val fee: CurrencyAmount,
    ) : Record

    data class Transaction(
        override val id: Long,
        override val profile: Profile,
        override val account: Account,
        override val labels: List<String>,
        override val receipts: List<Uri>,
        override val description: String,
        override val amount: CurrencyAmount,
        override val date: LocalDateTime,
        val type: TransactionType,
        val category: Category?
    ) : Record {
        val transactionAmount: CurrencyAmount = when(type) {
            TransactionType.Outgoing -> amount.copy(amountValue = amount.value.negate())
            TransactionType.Incoming -> amount
        }
    }
}

