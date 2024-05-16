package org.expenny.core.model.institution

import org.expenny.core.model.currency.CurrencyUnit
import java.math.BigDecimal
import java.time.LocalDateTime

data class InstitutionAccount(
    val id: String,
    val resourceId: String,
    val institutionId: String,
    val status: String,
    val iban: String,
    val currency: CurrencyUnit,
    val name: String,
    val ownerName: String,
    val balance: BigDecimal,
    val lastAccessedAt: LocalDateTime,
    val createdAt: LocalDateTime,
)
