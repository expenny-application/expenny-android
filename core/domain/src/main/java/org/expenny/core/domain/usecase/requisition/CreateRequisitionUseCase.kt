package org.expenny.core.domain.usecase.requisition

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.expenny.core.domain.repository.InstitutionRequisitionRepository
import org.expenny.core.model.institution.InstitutionRequisition
import org.expenny.core.common.utils.RemoteResult
import org.expenny.core.domain.usecase.institution.GetInstitutionSyncEnabledUseCase
import javax.inject.Inject

class CreateRequisitionUseCase @Inject constructor(
    private val institutionRequisitionRepository: InstitutionRequisitionRepository,
    private val getInstitutionSyncEnabled: GetInstitutionSyncEnabledUseCase,
) {

    operator fun invoke(params: Params) : Flow<RemoteResult<InstitutionRequisition>> {
        return if (getInstitutionSyncEnabled()) {
            institutionRequisitionRepository.createInstitutionRequisition(params.institutionId)
        } else {
            flowOf(RemoteResult.Error(null))
        }
    }

    data class Params(val institutionId: String)
}