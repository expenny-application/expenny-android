package org.expenny.core.domain.usecase.institution

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.expenny.core.domain.repository.InstitutionRepository
import org.expenny.core.model.institution.InstitutionRequisition
import org.expenny.core.model.resource.RemoteResult
import javax.inject.Inject

class CreateInstitutionRequisitionUseCase @Inject constructor(
    private val institutionRepository: InstitutionRepository,
    private val getInstitutionSyncEnabled: GetInstitutionSyncEnabledUseCase,
) {

    operator fun invoke(params: Params) : Flow<RemoteResult<InstitutionRequisition>> {
        return if (getInstitutionSyncEnabled()) {
            institutionRepository.createInstitutionRequisition(params.institutionId)
        } else {
            flowOf(RemoteResult.Error(null))
        }
    }

    data class Params(val institutionId: String)
}