package org.expenny.core.model.account

import org.expenny.core.common.types.AccountType
import java.math.BigDecimal

data class AccountUpdate(
    val id: Long,
    val currencyId: Long,
    val name: String,
    val type: AccountType,
    val description: String,
    val startBalance: BigDecimal,
    val totalBalance: BigDecimal,
)
