package org.expenny.core.ui.mapper

import org.expenny.core.model.currency.CurrencyAmount
import org.expenny.core.ui.data.ui.AmountUi
import javax.inject.Inject

class AmountMapper @Inject constructor() {

    operator fun invoke(model: CurrencyAmount): AmountUi {
        return AmountUi(value = model.value, displayValue = model.toString())
    }
}