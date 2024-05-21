package org.expenny.core.model.institution

data class Institution(
    val id: String,
    val name: String,
    val bic: String,
    val historyDays: Int,
    val logoUrl: String,
    val countryCodes: List<String>
)