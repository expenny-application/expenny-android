package org.expenny.core.ui.mapper

import org.expenny.core.model.account.AccountRecords
import org.expenny.core.ui.data.AccountUi
import javax.inject.Inject

class AccountMapper @Inject constructor(
    private val amountMapper: AmountMapper,
) {

    operator fun invoke(model: AccountRecords): AccountUi {
        return AccountUi(
            id = model.account.id,
            type = model.account.type,
            name = model.account.name,
            balance = amountMapper(model.account.totalBalance),
            recordsCount = model.records.size
        )
    }

    operator fun invoke(model: List<AccountRecords>): List<AccountUi> {
        return model.map { invoke(it) }
    }
}