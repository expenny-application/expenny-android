package org.expenny.core.domain.repository

import kotlinx.coroutines.flow.Flow
import org.expenny.core.model.institution.Institution
import org.expenny.core.model.institution.InstitutionRequisition
import org.expenny.core.common.utils.RemoteResult

interface InstitutionRepository {

    fun getInstitutions(countryCode: String?): Flow<RemoteResult<List<Institution>>>

    fun getSandboxInstitutions(): Flow<RemoteResult<List<Institution>>>
}