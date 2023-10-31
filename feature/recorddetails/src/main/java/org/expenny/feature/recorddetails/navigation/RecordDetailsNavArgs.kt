package org.expenny.feature.recorddetails.navigation

import org.expenny.core.common.types.RecordType

data class RecordDetailsNavArgs(
    val recordId: Long? = null,
    val isClone: Boolean = false,
    val recordType: RecordType? = null
)

