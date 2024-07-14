package org.expenny.core.ui.mapper

import org.expenny.core.common.extensions.join
import org.expenny.core.common.extensions.toMonetaryString
import org.expenny.core.model.currency.Currency
import org.expenny.core.ui.data.AmountUi
import org.expenny.core.ui.data.CurrencyUi
import javax.inject.Inject

class CurrencyMapper @Inject constructor() {

    operator fun invoke(model: Currency): CurrencyUi {
        return with(model) {
            CurrencyUi(
                id = id,
                code = unit.code,
                name = unit.name,
                isMain = isMain,
                preview = unit.code.join(unit.name),
                rate = AmountUi(
                    value = quoteToBaseRate,
                    displayValue = "${quoteToBaseRate.toMonetaryString()} ${profile.currencyUnit.code}",
                )
            )
        }
    }

    operator fun invoke(model: List<Currency>): List<CurrencyUi> {
        return model.map { invoke(it) }
    }
}