package org.expenny.core.domain.usecase.institution

import kotlinx.coroutines.flow.Flow
import org.expenny.core.domain.repository.InstitutionRepository
import org.expenny.core.domain.repository.InstitutionRequisitionRepository
import org.expenny.core.common.utils.RemoteResult
import javax.inject.Inject

class DeleteInstitutionRequisitionUseCase @Inject constructor(
    private val institutionRequisitionRepository: InstitutionRequisitionRepository
) {

    operator fun invoke(params: Params): Flow<RemoteResult<Boolean>> {
        return institutionRequisitionRepository.deleteInstitutionRequisition(params.requisitionId)
    }

    data class Params(val requisitionId: String)
}