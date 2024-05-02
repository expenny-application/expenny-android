package org.expenny.core.domain.repository

import kotlinx.coroutines.flow.Flow
import org.expenny.core.model.institution.Institution
import org.expenny.core.model.resource.RemoteResult

interface InstitutionRepository {

    fun getInstitutions(countryCode: String): Flow<RemoteResult<List<Institution>>>
}