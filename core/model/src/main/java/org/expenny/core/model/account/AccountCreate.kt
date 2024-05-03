package org.expenny.core.model.account

import org.expenny.core.common.types.LocalAccountType
import java.math.BigDecimal

data class AccountCreate(
    val profileId: Long,
    val currencyId: Long,
    val name: String,
    val type: LocalAccountType,
    val description: String,
    val startBalance: BigDecimal,
    val totalBalance: BigDecimal
)
