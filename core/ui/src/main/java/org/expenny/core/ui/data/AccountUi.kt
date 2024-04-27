package org.expenny.core.ui.data

import org.expenny.core.common.types.AccountType

data class AccountUi(
    val id: Long,
    val type: AccountType,
    val name: String,
    val recordsCount: Int,
    val balance: AmountUi,
)

