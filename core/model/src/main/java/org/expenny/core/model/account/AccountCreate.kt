package org.expenny.core.model.account

import org.expenny.core.common.types.AccountType
import java.math.BigDecimal

data class AccountCreate(
    val profileId: Long,
    val currencyId: Long,
    val name: String,
    val type: AccountType,
    val description: String,
    val startBalance: BigDecimal,
    val totalBalance: BigDecimal
)
