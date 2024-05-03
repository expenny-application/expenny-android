package org.expenny.core.domain.usecase.institution

import kotlinx.coroutines.flow.Flow
import org.expenny.core.domain.repository.InstitutionRepository
import org.expenny.core.model.institution.InstitutionRequisition
import org.expenny.core.model.resource.RemoteResult
import javax.inject.Inject

class GetInstitutionRequisitionUseCase @Inject constructor(
    private val institutionRepository: InstitutionRepository
) {

    operator fun invoke(params: Params) : Flow<RemoteResult<InstitutionRequisition>> {
        return institutionRepository.getInstitutionRequisition(params.requisitionId)
    }

    data class Params(val requisitionId: String)
}