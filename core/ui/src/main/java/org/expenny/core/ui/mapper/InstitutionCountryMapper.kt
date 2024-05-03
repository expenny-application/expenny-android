package org.expenny.core.ui.mapper

import org.expenny.core.model.institution.InstitutionCountry
import org.expenny.core.ui.data.InstitutionCountryUi
import javax.inject.Inject

class InstitutionCountryMapper @Inject constructor() {

    operator fun invoke(model: InstitutionCountry): InstitutionCountryUi {
        return InstitutionCountryUi(
            code = model.code,
            country = model.country,
            flag = model.flag,
            institutionsCount = model.institutionsCount
        )
    }

    operator fun invoke(model: List<InstitutionCountry>): List<InstitutionCountryUi> {
        return model.map { invoke(it) }
    }
}