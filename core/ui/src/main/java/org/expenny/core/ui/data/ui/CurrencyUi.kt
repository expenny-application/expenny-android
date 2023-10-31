package org.expenny.core.ui.data.ui

data class CurrencyUi(
    val id: Long,
    val code: String,
    val name: String,
    val rate: AmountUi?,
    val isMain: Boolean,
    val preview: String
)
