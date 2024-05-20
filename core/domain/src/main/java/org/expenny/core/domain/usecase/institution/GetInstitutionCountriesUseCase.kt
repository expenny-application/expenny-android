package org.expenny.core.domain.usecase.institution

import kotlinx.coroutines.flow.Flow
import org.expenny.core.domain.repository.InstitutionCountryRepository
import org.expenny.core.common.utils.RemoteResult
import org.expenny.core.model.common.Country
import javax.inject.Inject

class GetInstitutionCountriesUseCase @Inject constructor(
    private val institutionCountryRepository: InstitutionCountryRepository
) {

    operator fun invoke() : Flow<RemoteResult<List<Country>>> {
        return institutionCountryRepository.getInstitutionCountries()
    }
}