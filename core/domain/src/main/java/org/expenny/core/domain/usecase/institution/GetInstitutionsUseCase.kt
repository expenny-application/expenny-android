package org.expenny.core.domain.usecase.institution

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import org.expenny.core.datastore.BuildConfig
import org.expenny.core.domain.repository.InstitutionRepository
import org.expenny.core.model.institution.Institution
import org.expenny.core.common.utils.RemoteResult
import javax.inject.Inject

class GetInstitutionsUseCase @Inject constructor(
    private val institutionRepository: InstitutionRepository
) {

    operator fun invoke(params: Params) : Flow<RemoteResult<List<Institution>>> {
        return if (BuildConfig.DEBUG) {
            institutionRepository.getSandboxInstitutions()
        } else {
            institutionRepository.getInstitutions(params.countryCode)
        }
    }

    data class Params(
        val countryCode: String?
    )
}