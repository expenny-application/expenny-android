package org.expenny.core.ui.mapper

import org.expenny.core.common.extensions.join
import org.expenny.core.model.currency.CurrencyUnit
import org.expenny.core.ui.data.CurrencyUnitUi
import javax.inject.Inject

class CurrencyUnitMapper @Inject constructor() {

    operator fun invoke(model: CurrencyUnit): CurrencyUnitUi {
        return with(model) {
            CurrencyUnitUi(
                id = id,
                name = name,
                code = code,
                preview = code.join(name)
            )
        }
    }

    operator fun invoke(model: List<CurrencyUnit>): List<CurrencyUnitUi> {
        return model.map { invoke(it) }
    }
}