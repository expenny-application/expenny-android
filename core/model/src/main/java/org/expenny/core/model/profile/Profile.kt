package org.expenny.core.model.profile

import org.expenny.core.model.currency.CurrencyUnit

data class Profile(
    val id: Long,
    val name: String,
    val currencyUnit: CurrencyUnit,
)
