package org.expenny.core.model.account

import org.expenny.core.common.types.LocalAccountType
import org.expenny.core.model.currency.Currency
import org.expenny.core.model.currency.CurrencyAmount
import org.expenny.core.model.profile.Profile
import java.time.LocalDateTime

data class Account(
    val id: Long,
    val profile: Profile,
    val name: String,
    val description: String,
    val type: LocalAccountType,
    val currency: Currency,
    val startBalance: CurrencyAmount,
    val totalBalance: CurrencyAmount,
    val createdAt: LocalDateTime,
) {
    val displayName: String
        get() = "$name • ${currency.unit.code}"
}
