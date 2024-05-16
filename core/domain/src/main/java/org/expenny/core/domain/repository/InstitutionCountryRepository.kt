package org.expenny.core.domain.repository

import kotlinx.coroutines.flow.Flow
import org.expenny.core.model.institution.InstitutionCountry
import org.expenny.core.common.utils.RemoteResult

interface InstitutionCountryRepository {

    fun getInstitutionCountries() : Flow<RemoteResult<List<InstitutionCountry>>>
}