package org.expenny.core.domain.usecase.requisition

import kotlinx.coroutines.flow.Flow
import org.expenny.core.domain.repository.InstitutionRequisitionRepository
import org.expenny.core.common.utils.RemoteResult
import javax.inject.Inject

class DeleteRequisitionUseCase @Inject constructor(
    private val institutionRequisitionRepository: InstitutionRequisitionRepository
) {

    operator fun invoke(params: Params): Flow<RemoteResult<Boolean>> {
        return institutionRequisitionRepository.deleteInstitutionRequisition(params.requisitionId)
    }

    data class Params(val requisitionId: String)
}