package org.expenny.core.domain.usecase.institution

import kotlinx.coroutines.flow.Flow
import org.expenny.core.domain.repository.InstitutionCountryRepository
import org.expenny.core.model.institution.InstitutionCountry
import org.expenny.core.model.resource.RemoteResult
import javax.inject.Inject

class GetInstitutionCountriesUseCase @Inject constructor(
    private val institutionCountryRepository: InstitutionCountryRepository
) {

    operator fun invoke() : Flow<RemoteResult<List<InstitutionCountry>>> {
        return institutionCountryRepository.getInstitutionCountries()
    }
}