package org.expenny.core.ui.mapper

import org.expenny.core.model.account.Account
import org.expenny.core.ui.data.ui.AccountNameUi
import javax.inject.Inject

class AccountNameMapper @Inject constructor() {

    operator fun invoke(model: Account): AccountNameUi {
        return AccountNameUi(
            id = model.id,
            displayName = "${model.name} • ${model.currency.unit.code}"
        )
    }

    operator fun invoke(model: List<Account>): List<AccountNameUi> {
        return model.map { invoke(it) }
    }
}