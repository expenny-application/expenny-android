package org.expenny.core.domain.usecase.institution

import kotlinx.coroutines.flow.Flow
import org.expenny.core.domain.repository.InstitutionRepository
import org.expenny.core.model.institution.Institution
import org.expenny.core.common.utils.RemoteResult
import javax.inject.Inject

class GetInstitutionsUseCase @Inject constructor(
    private val institutionRepository: InstitutionRepository
) {

    operator fun invoke(params: Params): Flow<RemoteResult<List<Institution>>> {
        return institutionRepository.getInstitutions(params.countryCode)
    }

    data class Params(
        val countryCode: String?
    )
}