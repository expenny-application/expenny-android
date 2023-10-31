package org.expenny.core.model.account

import org.expenny.core.model.record.Record

data class AccountWithRecords(
    val account: Account,
    val records: List<Record>,
)
