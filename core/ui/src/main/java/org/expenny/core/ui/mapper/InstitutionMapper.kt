package org.expenny.core.ui.mapper

import org.expenny.core.model.institution.Institution
import org.expenny.core.ui.data.InstitutionUi
import javax.inject.Inject

class InstitutionMapper @Inject constructor() {

    operator fun invoke(model: Institution): InstitutionUi {
        return InstitutionUi(
            id = model.id,
            name = model.name,
            bic = model.bic,
            logoUrl = model.logoUrl
        )
    }

    operator fun invoke(model: List<Institution>): List<InstitutionUi> {
        return model.map { invoke(it) }
    }
}