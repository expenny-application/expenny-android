package org.expenny.core.model.account

import org.expenny.core.common.types.LocalAccountType
import java.math.BigDecimal

data class AccountUpdate(
    val id: Long,
    val currencyId: Long,
    val name: String,
    val type: LocalAccountType,
    val description: String,
    val startBalance: BigDecimal,
    val totalBalance: BigDecimal,
)
