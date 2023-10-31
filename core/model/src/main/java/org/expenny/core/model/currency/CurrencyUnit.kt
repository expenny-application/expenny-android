package org.expenny.core.model.currency

data class CurrencyUnit(
    val id: Long,
    val name: String,
    val code: String,
    val scale: Int
)
