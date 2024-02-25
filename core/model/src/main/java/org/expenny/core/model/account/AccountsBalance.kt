package org.expenny.core.model.account

import org.expenny.core.model.currency.CurrencyAmount
import org.expenny.core.model.record.Record

data class AccountsBalance(
    val accounts: List<Account>,
    val amount: CurrencyAmount,
    val lastRecord: Record?
)
