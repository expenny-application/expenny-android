package org.expenny.feature.records.model

import org.expenny.core.ui.data.ui.ItemUi

data class SelectionFilterDataUi(
    val accounts: List<ItemUi> = emptyList(),
    val categories: List<ItemUi> = emptyList(),
    val labels: List<ItemUi> = emptyList(),
    val recordTypes: List<ItemUi> = emptyList()
)
