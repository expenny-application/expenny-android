package org.expenny.feature.records.model

import org.expenny.core.common.types.RecordType

data class SelectionFilterDataUi(
    val accounts: List<Pair<Long, String>> = emptyList(),
    val categories: List<Pair<Long, String>> = emptyList(),
    val labels: List<Pair<Long, String>> = emptyList(),
    val recordTypes: List<RecordType> = emptyList()
)
