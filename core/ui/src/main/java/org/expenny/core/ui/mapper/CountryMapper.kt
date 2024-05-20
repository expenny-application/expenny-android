package org.expenny.core.ui.mapper

import org.expenny.core.model.common.Country
import org.expenny.core.ui.data.CountryUi
import javax.inject.Inject

class CountryMapper @Inject constructor() {

    operator fun invoke(model: Country): CountryUi {
        return CountryUi(
            name = model.name,
            code = model.code,
        )
    }

    operator fun invoke(model: List<Country>): List<CountryUi> {
        return model.map { invoke(it) }
    }
}