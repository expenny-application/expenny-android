package org.expenny.feature.records.details.navigation

import org.expenny.core.common.types.RecordType

data class RecordDetailsNavArgs(
    val recordId: Long? = null,
    val recordType: RecordType = RecordType.Expense
)
