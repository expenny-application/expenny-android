package org.expenny.core.model.institution

data class Institution(
    val id: String,
    val name: String,
    val bic: String,
    val transactionTotalDays: String,
    val logoUrl: String,
)