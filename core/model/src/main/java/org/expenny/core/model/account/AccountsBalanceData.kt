package org.expenny.core.model.account

import org.expenny.core.model.currency.CurrencyAmount
import org.expenny.core.model.record.Record

data class AccountsBalanceData(
    val accounts: List<Account>,
    val balance: CurrencyAmount,
    val lastRecord: Record?
)
