package org.expenny.core.common.types

enum class RecordType {
    Expense,
    Income,
    Transfer;

    companion object {
        val RecordType.transactionType: TransactionType?
            get() = when (this) {
                Expense -> TransactionType.Outgoing
                Income -> TransactionType.Incoming
                else -> null
            }
    }
}