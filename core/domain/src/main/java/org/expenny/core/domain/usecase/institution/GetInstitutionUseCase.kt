package org.expenny.core.domain.usecase.institution

import kotlinx.coroutines.flow.Flow
import org.expenny.core.common.utils.RemoteResult
import org.expenny.core.domain.repository.InstitutionRepository
import org.expenny.core.model.institution.Institution
import javax.inject.Inject

class GetInstitutionUseCase @Inject constructor(
    private val institutionRepository: InstitutionRepository
) {

    operator fun invoke(params: Params) : Flow<RemoteResult<Institution>> {
        return institutionRepository.getInstitution(params.institutionId)
    }

    data class Params(
        val institutionId: String
    )
}