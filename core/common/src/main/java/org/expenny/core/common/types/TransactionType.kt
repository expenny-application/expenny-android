package org.expenny.core.common.types

enum class TransactionType {
    Outgoing,
    Incoming;

    companion object {
        val TransactionType.recordType: RecordType
            get() = when (this) {
                Outgoing -> RecordType.Expense
                Incoming -> RecordType.Income
            }
    }
}