package org.expenny.core.ui.data

import org.expenny.core.common.types.LocalAccountType

data class AccountUi(
    val id: Long,
    val type: LocalAccountType,
    val name: String,
    val recordsCount: Int,
    val balance: AmountUi,
)

