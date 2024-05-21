package org.expenny.core.domain.repository

import kotlinx.coroutines.flow.Flow
import org.expenny.core.common.utils.RemoteResult
import org.expenny.core.model.common.Country

interface InstitutionCountryRepository {

    fun getInstitutionCountries() : Flow<RemoteResult<List<Country>>>
}