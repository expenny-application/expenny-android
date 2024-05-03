package org.expenny.core.domain.usecase.institution

import kotlinx.coroutines.flow.Flow
import org.expenny.core.domain.repository.InstitutionRepository
import org.expenny.core.model.resource.RemoteResult
import javax.inject.Inject

class DeleteInstitutionRequisitionUseCase @Inject constructor(
    private val institutionRepository: InstitutionRepository
) {

    operator fun invoke(params: Params): Flow<RemoteResult<Boolean>> {
        return institutionRepository.deleteInstitutionRequisition(params.requisitionId)
    }

    data class Params(val requisitionId: String)
}